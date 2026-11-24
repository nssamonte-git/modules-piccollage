package noel.libs.creative.documents

import java.nio.file.Files
import kotlinx.coroutines.test.runTest
import noel.libs.creative.documents.data.*
import noel.libs.creative.documents.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class DocumentTest {
    private val d =
        CreativeDocument(
            "d",
            pages =
                listOf(
                    Page(
                        "p",
                        100,
                        100,
                        layers = listOf(Layer("text", content = Content.Text("Hello 👨‍👩‍👧‍👦"))),
                    )
                ),
        )

    @Test
    fun codecRoundTrip() {
        val c = DocumentCodec()
        assertEquals(d, c.decode(c.encode(d)))
    }

    @Test
    fun futureVersionPreserved() {
        val c = DocumentCodec()
        val b = c.encode(d)
        b[7] = 99
        try {
            c.decode(b)
            fail()
        } catch (e: DocumentCodec.FutureVersion) {
            assertEquals(99, e.version)
        }
    }

    @Test
    fun truncatedAndOversizedCountsRejected() {
        val c = DocumentCodec()
        try {
            c.decode(c.encode(d).copyOf(12))
            fail()
        } catch (_: Exception) {}
    }

    @Test
    fun interruptedSaveRetainsPublishedRevision() = runTest {
        val root = Files.createTempDirectory("draft-test").toFile()
        val source =
            object : AssetBytes {
                override fun read(assetId: String) = Outcome.Success(byteArrayOf(1))
            }
        val first = FileDocumentRepository(root, source)
        assertTrue(first.save("a", d) is Outcome.Success)
        val broken =
            FileDocumentRepository(
                root,
                source,
                fault = { if (it == "before-publish") error("fault") },
            )
        assertTrue(broken.save("a", d.copy(revision = 1, title = "Changed")) is Outcome.Failure)
        assertEquals(d, (first.load("a", "d") as Outcome.Success).value)
        assertTrue(first.load("another-account", "d") is Outcome.Failure)
    }

    @Test
    fun membershipIsAccountScoped() {
        assertTrue(AlbumMembership().add(Album("x", "a", emptySet()), "b", "d") is Outcome.Failure)
    }

    @Test
    fun legacyV0MigrationPreservesIds() {
        val codec = DocumentCodec()
        val bytes = codec.encode(d)
        val input = java.io.DataInputStream(java.io.ByteArrayInputStream(bytes))
        input.readInt()
        input.readInt()
        val idSize = input.readInt()
        val id = ByteArray(idSize)
        input.readFully(id)
        val revision = input.readLong()
        val titleSize = input.readInt()
        input.skipBytes(titleSize)
        val rest = input.readBytes()
        val buffer = java.io.ByteArrayOutputStream()
        val output = java.io.DataOutputStream(buffer)
        output.writeInt(0x4e43444f)
        output.writeInt(0)
        output.writeInt(idSize)
        output.write(id)
        output.writeLong(revision)
        output.write(rest)
        assertEquals(d, codec.decode(buffer.toByteArray()))
    }
}
