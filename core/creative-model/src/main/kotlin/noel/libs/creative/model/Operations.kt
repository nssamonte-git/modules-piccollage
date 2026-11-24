package noel.libs.creative.model

import java.security.MessageDigest

enum class OperationState {
    RUNNING,
    UNCERTAIN,
    COMPLETED,
    CANCELED,
    FAILED,
}

data class OperationRecord(
    val key: String,
    val fingerprint: String,
    val state: OperationState,
    val resultId: String? = null,
)

/** Implementations must serialize atomic blocks across all callers sharing their store. */
interface OperationStore {
    fun <T> atomic(block: () -> T): T

    fun get(key: String): OperationRecord?

    /**
     * Stores [record]. When the journal is full, a record with a new key must be refused with
     * [IllegalArgumentException]; the runners report that as [CreativeError.Code.LIMIT].
     */
    fun put(record: OperationRecord)
}

class MemoryOperationStore(private val maxEntries: Int = 4096) : OperationStore {
    private val records = mutableMapOf<String, OperationRecord>()

    @Synchronized override fun <T> atomic(block: () -> T): T = block()

    @Synchronized override fun get(key: String) = records[key]

    @Synchronized
    override fun put(record: OperationRecord) {
        require(record.key in records || records.size < maxEntries) { "Operation journal full" }
        records[record.key] = record
    }
}

fun fingerprint(vararg parts: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    parts.forEach { p ->
        val b = p.toByteArray()
        digest.update(b.size.toString().toByteArray())
        digest.update(0.toByte())
        digest.update(b)
    }
    return digest.digest().joinToString("") { "%02x".format(it) }
}

fun operationKey(account: String, document: String, id: String) = fingerprint(account, document, id)

sealed interface MessagePart {
    data class Text(val text: String) : MessagePart

    data class Image(val assetId: String, val mediaType: String) : MessagePart

    data class File(val assetId: String, val mediaType: String) : MessagePart

    data class ToolResult(val callId: String, val status: String) : MessagePart

    data class Unknown(val kind: String) : MessagePart
}
