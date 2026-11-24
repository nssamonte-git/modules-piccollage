package noel.libs.creative.history.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import noel.libs.creative.model.*

/** One synchronized document owner. Revisions remain monotonic across undo/redo. */
class HistorySession(
    initial: CreativeDocument,
    private val maxEntries: Int = 200,
    private val maxBytes: Long = 16L * 1024 * 1024,
) : EditSession {
    private data class Entry(
        val before: CreativeDocument,
        val after: CreativeDocument,
        val key: String?,
        val signature: String?,
    )

    private val current = MutableStateFlow(initial.immutableCopy().also { it.validate() })
    private val status = MutableStateFlow(HistoryState())
    override val document = current.asStateFlow()
    override val history = status.asStateFlow()
    private val past = ArrayDeque<Entry>()
    private val future = ArrayDeque<Entry>()

    init {
        require(maxEntries > 0 && maxBytes > 0)
    }

    private fun publish() {
        status.value = HistoryState(past.isNotEmpty(), future.isNotEmpty())
    }

    private fun signature(edits: List<Edit>): String? =
        edits.singleOrNull()?.let {
            if (it is Edit.TransformLayer) "${it.pageId}/${it.layerId}/transform" else null
        }

    private fun weight(d: CreativeDocument): Long =
        256 +
            d.pages.sumOf {
                128L +
                    it.layers.sumOf { l ->
                        256L +
                            when (val c = l.content) {
                                is Content.Text -> c.text.length * 2L
                                is Content.Drawing -> c.strokes.sumOf { it.points.size * 16L }
                                else -> 0L
                            } +
                            (l.mask?.strokes?.sumOf { it.points.size * 16L } ?: 0L)
                    }
            }

    @Synchronized
    override fun commit(request: EditRequest): Outcome<CreativeDocument> {
        val before = current.value
        if (before.revision != request.expectedRevision)
            return failure(CreativeError.Code.STALE_REVISION, "Document changed")
        return try {
            val staged = applyEdits(before, request.edits)
            if (staged == before) return Outcome.Success(before)
            val after = staged.copy(revision = Math.addExact(before.revision, 1))
            val sig = signature(request.edits)
            val previous = past.lastOrNull()
            if (
                previous != null &&
                    request.mergeKey != null &&
                    sig != null &&
                    previous.key == request.mergeKey &&
                    previous.signature == sig &&
                    future.isEmpty()
            ) {
                past.removeLast()
                past.addLast(previous.copy(after = after))
            } else past.addLast(Entry(before, after, request.mergeKey, sig))
            future.clear()
            while (
                past.size > maxEntries ||
                    past.sumOf { weight(it.before) + weight(it.after) } > maxBytes
            ) past.removeFirst()
            current.value = after
            publish()
            Outcome.Success(after)
        } catch (e: IllegalArgumentException) {
            failure(CreativeError.Code.INVALID_INPUT, e.message ?: "Invalid edit")
        } catch (e: NoSuchElementException) {
            failure(CreativeError.Code.INVALID_INPUT, "Missing target")
        }
    }

    @Synchronized
    override fun undo(): Outcome<CreativeDocument> {
        val entry =
            past.removeLastOrNull()
                ?: return failure(CreativeError.Code.UNAVAILABLE, "Nothing to undo")
        future.addLast(entry)
        current.value = entry.before.copy(revision = current.value.revision + 1)
        publish()
        return Outcome.Success(current.value)
    }

    @Synchronized
    override fun redo(): Outcome<CreativeDocument> {
        val entry =
            future.removeLastOrNull()
                ?: return failure(CreativeError.Code.UNAVAILABLE, "Nothing to redo")
        past.addLast(entry)
        current.value = entry.after.copy(revision = current.value.revision + 1)
        publish()
        return Outcome.Success(current.value)
    }
}
