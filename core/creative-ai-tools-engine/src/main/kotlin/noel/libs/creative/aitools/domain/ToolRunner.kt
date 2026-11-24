package noel.libs.creative.aitools.domain

import noel.libs.creative.model.*

data class ToolCall(
    val callId: String,
    val operationId: String,
    val accountId: String,
    val documentId: String,
    val revision: Long,
    val name: String,
    val arguments: Map<String, String>,
)

data class ToolResult(
    val callId: String,
    val operationId: String,
    val documentRevision: Long,
    val replayed: Boolean = false,
)

class ToolRunner(
    private val store: OperationStore,
    private val maxCalls: Int = 16,
    private val telemetry: Telemetry? = null,
) {
    fun execute(call: ToolCall, session: EditSession): Outcome<ToolResult> = store.atomic {
        if (
            call.callId.isBlank() ||
                call.operationId.isBlank() ||
                call.accountId.isBlank() ||
                call.documentId != session.document.value.id
        )
            return@atomic failure(CreativeError.Code.INVALID_INPUT, "Invalid operation identity")
        val key = operationKey(call.accountId, call.documentId, call.operationId)
        val fp =
            fingerprint(
                call.documentId,
                call.revision.toString(),
                call.name,
                *call.arguments.toSortedMap().flatMap { listOf(it.key, it.value) }.toTypedArray(),
            )
        val previous = store.get(key)
        if (previous != null) {
            if (previous.fingerprint != fp)
                return@atomic failure(
                    CreativeError.Code.CONFLICT,
                    "Operation identity reused with different input",
                )
            return@atomic if (previous.state == OperationState.COMPLETED)
                Outcome.Success(
                    ToolResult(
                        call.callId,
                        call.operationId,
                        previous.resultId?.toLongOrNull() ?: call.revision,
                        true,
                    )
                )
            else
                failure(
                    CreativeError.Code.CONFLICT,
                    "Operation requires reconciliation: ${previous.state}",
                )
        }
        val edit =
            try {
                val a = call.arguments
                fun required(keys: Set<String>) {
                    require(a.keys == keys && a.values.all { it.length <= 100_000 })
                }
                when (call.name) {
                    "move" -> {
                        required(setOf("pageId", "layerId", "x", "y"))
                        val l =
                            session.document.value.pages
                                .first { it.id == a.getValue("pageId") }
                                .layers
                                .first { it.id == a.getValue("layerId") }
                        Edit.TransformLayer(
                            a.getValue("pageId"),
                            l.id,
                            l.transform.copy(
                                x = a.getValue("x").toDouble(),
                                y = a.getValue("y").toDouble(),
                            ),
                        )
                    }
                    "setText" -> {
                        required(setOf("pageId", "layerId", "text"))
                        val l =
                            session.document.value.pages
                                .first { it.id == a.getValue("pageId") }
                                .layers
                                .first { it.id == a.getValue("layerId") && !it.locked }
                        val text = l.content as? Content.Text ?: throw IllegalArgumentException()
                        Edit.ReplaceLayer(
                            a.getValue("pageId"),
                            l.copy(
                                content = text.copy(text = a.getValue("text"), styles = emptyList())
                            ),
                        )
                    }
                    "deleteLayer" -> {
                        required(setOf("pageId", "layerId"))
                        Edit.RemoveLayer(a.getValue("pageId"), a.getValue("layerId"))
                    }
                    else ->
                        return@atomic failure(CreativeError.Code.UNSUPPORTED, "Unknown editor tool")
                }
            } catch (_: Exception) {
                return@atomic failure(CreativeError.Code.INVALID_INPUT, "Invalid tool arguments")
            }
        if (call.revision != session.document.value.revision)
            return@atomic failure(CreativeError.Code.STALE_REVISION, "Request a fresh proposal")
        store.put(OperationRecord(key, fp, OperationState.RUNNING))
        when (val result = session.commit(EditRequest(call.revision, listOf(edit)))) {
            is Outcome.Success -> {
                store.put(
                    OperationRecord(
                        key,
                        fp,
                        OperationState.COMPLETED,
                        result.value.revision.toString(),
                    )
                )
                runCatching { telemetry?.record(call.operationId, "completed") }
                Outcome.Success(ToolResult(call.callId, call.operationId, result.value.revision))
            }
            is Outcome.Failure -> {
                store.put(OperationRecord(key, fp, OperationState.FAILED))
                result
            }
        }
    }

    fun executeRun(calls: List<ToolCall>, session: EditSession): Outcome<List<ToolResult>> {
        if (calls.size > maxCalls)
            return failure(CreativeError.Code.LIMIT, "Tool-call budget exceeded")
        val results = mutableListOf<ToolResult>()
        for (call in calls) when (val result = execute(call, session)) {
            is Outcome.Success -> results += result.value
            is Outcome.Failure -> return result
        }
        return Outcome.Success(results)
    }

    fun cancel(accountId: String, documentId: String, operationId: String) {
        store.atomic {
            val key = operationKey(accountId, documentId, operationId)
            store.get(key)?.let {
                if (it.state != OperationState.COMPLETED)
                    store.put(it.copy(state = OperationState.CANCELED))
            }
        }
    }
}
