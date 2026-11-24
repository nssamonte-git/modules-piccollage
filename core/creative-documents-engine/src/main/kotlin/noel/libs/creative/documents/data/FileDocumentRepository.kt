package noel.libs.creative.documents.data

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noel.libs.creative.model.*

/** The host supplies an app-private root and immutable source bytes. Account paths are hashed. */
class FileDocumentRepository(
    private val root: File,
    private val assets: AssetBytes,
    private val codec: DocumentCodec = DocumentCodec(),
    private val fault: (String) -> Unit = {},
) : DocumentRepository {
    private fun key(s: String) =
        MessageDigest.getInstance("SHA-256").digest(s.toByteArray()).joinToString("") {
            "%02x".format(it)
        }

    private fun directory(account: String, id: String): File {
        require(account.isNotBlank() && id.isNotBlank())
        return File(File(root, key(account)), key(id))
    }

    private fun durable(file: File, bytes: ByteArray) {
        FileOutputStream(file).use {
            it.write(bytes)
            it.fd.sync()
        }
    }

    override suspend fun save(accountId: String, document: CreativeDocument): Outcome<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = document.immutableCopy()
                val bytes = codec.encode(snapshot)
                val dir = directory(accountId, snapshot.id)
                dir.mkdirs()
                synchronized(lockFor(dir)) {
                    val existing = readLatest(dir)
                    if (existing is Outcome.Success && existing.value.revision > snapshot.revision)
                        return@synchronized failure(
                            CreativeError.Code.STALE_REVISION,
                            "Newer draft exists",
                        )
                    if (
                        existing is Outcome.Failure &&
                            existing.error.code == CreativeError.Code.UNSUPPORTED
                    )
                        return@synchronized existing
                    val rev = File(dir, "revision-${UUID.randomUUID()}")
                    rev.mkdirs()
                    for (asset in snapshot.assets) {
                        val b = assets.read(asset.id)
                        if (b !is Outcome.Success)
                            return@synchronized failure(
                                CreativeError.Code.MISSING_ASSET,
                                "Asset unavailable: ${asset.id}",
                            )
                        require(b.value.size <= 64 * 1024 * 1024)
                        durable(File(rev, key(asset.id)), b.value)
                    }
                    fault("assets-written")
                    durable(File(rev, "document.bin"), bytes)
                    fault("structure-written")
                    val pointer = File(dir, "pending-${UUID.randomUUID()}")
                    durable(pointer, rev.name.toByteArray())
                    fault("before-publish")
                    Files.move(
                        pointer.toPath(),
                        File(dir, "current").toPath(),
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING,
                    )
                    Outcome.Success(Unit)
                }
            } catch (e: Exception) {
                failure(CreativeError.Code.IO, "Draft save failed: ${e.javaClass.simpleName}")
            }
        }

    private fun readLatest(dir: File): Outcome<CreativeDocument> {
        val pointer = File(dir, "current")
        if (!pointer.isFile) return failure(CreativeError.Code.UNAVAILABLE, "No draft")
        return try {
            val name = pointer.readText()
            require(name.matches(Regex("revision-[a-f0-9-]{36}")))
            val bytes = File(File(dir, name), "document.bin")
            require(bytes.length() <= 16 * 1024 * 1024)
            Outcome.Success(codec.decode(bytes.readBytes()))
        } catch (e: DocumentCodec.FutureVersion) {
            failure(CreativeError.Code.UNSUPPORTED, e.message ?: "Future document")
        } catch (e: Exception) {
            failure(CreativeError.Code.IO, "Draft could not be opened")
        }
    }

    override suspend fun load(accountId: String, documentId: String): Outcome<CreativeDocument> =
        withContext(Dispatchers.IO) {
            try {
                val dir = directory(accountId, documentId)
                synchronized(lockFor(dir)) { readLatest(dir) }
            } catch (e: Exception) {
                failure(CreativeError.Code.IO, "Draft unavailable")
            }
        }

    suspend fun readAsset(
        accountId: String,
        documentId: String,
        assetId: String,
    ): Outcome<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                val dir = directory(accountId, documentId)
                synchronized(lockFor(dir)) {
                    val name = File(dir, "current").readText()
                    require(name.matches(Regex("revision-[a-f0-9-]{36}")))
                    val f = File(File(dir, name), key(assetId))
                    require(f.length() <= 64 * 1024 * 1024)
                    Outcome.Success(f.readBytes())
                }
            } catch (e: Exception) {
                failure(CreativeError.Code.MISSING_ASSET, "Missing local media")
            }
        }

    companion object {
        private val locks = Array(64) { Any() }

        private fun lockFor(file: File) =
            locks[(file.absolutePath.hashCode() and Int.MAX_VALUE) % locks.size]
    }
}
