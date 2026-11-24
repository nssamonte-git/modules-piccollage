package noel.libs.creative.aiimages.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import noel.libs.creative.model.*

data class ImageJob(
    val operationId: String,
    val accountId: String,
    val documentId: String,
    val revision: Long,
    val assetId: String,
    val sourceRevision: String,
    val width: Int,
    val height: Int,
    val operation: String,
    val prompt: String = "",
    val style: String = "",
    val maskWidth: Int? = null,
    val maskHeight: Int? = null,
)

data class GeneratedImage(
    val assetId: String,
    val mediaType: String,
    val width: Int,
    val height: Int,
    val bytes: ByteArray,
)

interface ImageProvider {
    suspend fun generate(job: ImageJob): Outcome<GeneratedImage>

    suspend fun reconcile(job: ImageJob): Outcome<GeneratedImage> =
        failure(CreativeError.Code.UNAVAILABLE, "Host must reconcile the existing remote operation")
}

interface GeneratedMediaValidator {
    fun validate(image: GeneratedImage): Outcome<GeneratedImage>
}

data class LetteringEntry(
    val operationId: String,
    val accountId: String,
    val text: String,
    val style: String,
    val outputAssetId: String?,
) {
    val fallbackText
        get() = text
}

class LetteringHistory(private val capacity: Int = 50) {
    private val entries = mutableListOf<LetteringEntry>()

    @Synchronized
    fun add(entry: LetteringEntry) {
        require(entry.text.length <= 1000)
        entries.removeAll { it.accountId == entry.accountId && it.operationId == entry.operationId }
        entries.add(0, entry)
        val excess = entries.filter { it.accountId == entry.accountId }.drop(capacity).toSet()
        entries.removeAll(excess)
    }

    @Synchronized
    fun list(accountId: String) = entries.filter { it.accountId == accountId }.toList()

    @Synchronized
    fun clear(accountId: String) {
        entries.removeAll { it.accountId == accountId }
    }
}

/** A completed result is a proposal. Accept is a separate revision-checked user action. */
class ImageJobRunner(
    private val store: OperationStore,
    private val provider: ImageProvider?,
    private val validator: GeneratedMediaValidator?,
    private val timeoutMs: Long = 60_000,
    private val telemetry: Telemetry? = null,
) {
    private val results = mutableMapOf<String, GeneratedImage>()

    private fun key(job: ImageJob) = operationKey(job.accountId, job.documentId, job.operationId)

    private fun fp(j: ImageJob) =
        fingerprint(
            j.revision.toString(),
            j.assetId,
            j.sourceRevision,
            j.width.toString(),
            j.height.toString(),
            j.operation,
            j.prompt,
            j.style,
            j.maskWidth.toString(),
            j.maskHeight.toString(),
        )

    suspend fun start(job: ImageJob): Outcome<GeneratedImage> {
        if (
            job.operationId.isBlank() ||
                job.accountId.isBlank() ||
                job.width <= 0 ||
                job.height <= 0 ||
                job.width.toLong() * job.height > 16_000_000 ||
                job.prompt.length > 100_000 ||
                job.operation !in setOf("expand", "replace", "remove-background", "lettering") ||
                (job.maskWidth != null || job.maskHeight != null) &&
                    (job.maskWidth != job.width || job.maskHeight != job.height)
        )
            return failure(CreativeError.Code.INVALID_INPUT, "Invalid image job")
        val p =
            provider ?: return failure(CreativeError.Code.UNAVAILABLE, "Host AI provider required")
        val v =
            validator ?: return failure(CreativeError.Code.UNAVAILABLE, "Media validator required")
        val k = key(job)
        val fingerprint = fp(job)
        var reconcile = false
        val early = store.atomic {
            val record = store.get(k)
            if (record != null) {
                if (record.fingerprint != fingerprint)
                    return@atomic failure(
                        CreativeError.Code.CONFLICT,
                        "Operation identity conflict",
                    )
                if (
                    record.state == OperationState.CANCELED || record.state == OperationState.FAILED
                )
                    return@atomic failure(
                        CreativeError.Code.CANCELED,
                        "Operation cannot be resubmitted",
                    )
                results[k]?.let {
                    return@atomic Outcome.Success(it.copy(bytes = it.bytes.copyOf()))
                }
                reconcile = true
            } else store.put(OperationRecord(k, fingerprint, OperationState.RUNNING))
            null
        }
        if (early != null) return early
        return try {
            val response =
                withTimeout(timeoutMs) { if (reconcile) p.reconcile(job) else p.generate(job) }
            val checked =
                when (response) {
                    is Outcome.Success -> v.validate(response.value)
                    is Outcome.Failure -> response
                }
            store.atomic {
                val state = store.get(k)?.state
                if (state == OperationState.CANCELED)
                    return@atomic failure(
                        CreativeError.Code.CANCELED,
                        "Canceled session cannot accept late output",
                    )
                if (state == OperationState.COMPLETED && checked is Outcome.Failure) {
                    results[k]?.let {
                        return@atomic Outcome.Success(it.copy(bytes = it.bytes.copyOf()))
                    }
                }
                when (checked) {
                    is Outcome.Success -> {
                        val image = checked.value.copy(bytes = checked.value.bytes.copyOf())
                        results[k] = image
                        store.put(
                            OperationRecord(k, fingerprint, OperationState.COMPLETED, image.assetId)
                        )
                        runCatching { telemetry?.record(job.operationId, "completed") }
                        Outcome.Success(image.copy(bytes = image.bytes.copyOf()))
                    }
                    is Outcome.Failure -> {
                        store.put(
                            OperationRecord(
                                k,
                                fingerprint,
                                if (reconcile) OperationState.UNCERTAIN else OperationState.FAILED,
                            )
                        )
                        checked
                    }
                }
            }
        } catch (e: CancellationException) {
            store.atomic {
                val old = store.get(k)
                if (old?.state != OperationState.CANCELED)
                    store.put(OperationRecord(k, fingerprint, OperationState.UNCERTAIN))
            }
            if (e is TimeoutCancellationException)
                failure(CreativeError.Code.REMOTE, "Timed out; reconcile before retry")
            else throw e
        } catch (e: Exception) {
            store.atomic {
                if (store.get(k)?.state != OperationState.CANCELED)
                    store.put(OperationRecord(k, fingerprint, OperationState.UNCERTAIN))
            }
            failure(CreativeError.Code.REMOTE, "Outcome unknown; reconcile before retry")
        }
    }

    fun cancel(job: ImageJob) {
        store.atomic {
            val old = store.get(key(job))
            if (old != null) store.put(old.copy(state = OperationState.CANCELED))
            results.remove(key(job))
        }
    }

    fun accept(
        job: ImageJob,
        session: EditSession,
        pageId: String,
        layerId: String,
    ): Outcome<CreativeDocument> = store.atomic {
        val image =
            results[key(job)]
                ?: return@atomic failure(CreativeError.Code.UNAVAILABLE, "No validated proposal")
        if (store.get(key(job))?.state != OperationState.COMPLETED)
            return@atomic failure(CreativeError.Code.CANCELED, "Proposal canceled")
        val d = session.document.value
        if (d.id != job.documentId || d.revision != job.revision)
            return@atomic failure(
                CreativeError.Code.STALE_REVISION,
                "Document changed; keep output as a separate proposal",
            )
        val layer =
            d.pages
                .firstOrNull { it.id == pageId }
                ?.layers
                ?.firstOrNull { it.id == layerId && !it.locked }
                ?: return@atomic failure(CreativeError.Code.INVALID_INPUT, "Missing editable layer")
        val source = (layer.content as? Content.Image)?.assetId
        if (
            source != job.assetId ||
                d.assets.none { it.id == job.assetId && it.contentRevision == job.sourceRevision }
        )
            return@atomic failure(CreativeError.Code.STALE_REVISION, "Source asset changed")
        if (d.assets.any { it.id == image.assetId })
            return@atomic failure(CreativeError.Code.CONFLICT, "Result asset identity already used")
        session.commit(
            EditRequest(
                job.revision,
                listOf(
                    Edit.AddAsset(Asset(image.assetId, image.mediaType, image.width, image.height)),
                    Edit.ReplaceLayer(pageId, layer.copy(content = Content.Image(image.assetId))),
                ),
            )
        )
    }
}
