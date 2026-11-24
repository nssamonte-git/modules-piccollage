package noel.libs.creative.documents.data

import java.io.*
import java.nio.file.*
import java.util.UUID
import noel.libs.creative.model.*

/** Durable local journal. Use one instance per private file; server idempotency is host-owned. */
class FileOperationStore(private val file: File) : OperationStore {
    private val values = mutableMapOf<String, OperationRecord>()

    init {
        if (file.exists()) {
            require(file.length() <= 4 * 1024 * 1024)
            DataInputStream(FileInputStream(file)).use { i ->
                require(i.readInt() == 1)
                val n = i.readInt()
                require(n in 0..4096)
                repeat(n) {
                    val key = i.readUTF()
                    val fp = i.readUTF()
                    val state = OperationState.valueOf(i.readUTF())
                    val result = i.readUTF().ifEmpty { null }
                    values[key] = OperationRecord(key, fp, state, result)
                }
            }
        }
    }

    @Synchronized override fun <T> atomic(block: () -> T): T = block()

    @Synchronized override fun get(key: String) = values[key]

    @Synchronized
    override fun put(record: OperationRecord) {
        require(record.key in values || values.size < 4096)
        val updated = values + (record.key to record)
        file.parentFile?.mkdirs()
        val temporary = File(file.parentFile, "journal-${UUID.randomUUID()}")
        try {
            FileOutputStream(temporary).use { f ->
                val o = DataOutputStream(f)
                o.writeInt(1)
                o.writeInt(updated.size)
                updated.values.forEach {
                    o.writeUTF(it.key)
                    o.writeUTF(it.fingerprint)
                    o.writeUTF(it.state.name)
                    o.writeUTF(it.resultId ?: "")
                }
                o.flush()
                f.fd.sync()
            }
            Files.move(
                temporary.toPath(),
                file.toPath(),
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING,
            )
            values[record.key] = record
        } finally {
            temporary.delete()
        }
    }
}
