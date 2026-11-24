package noel.libs.creative.documents

import java.nio.file.Files
import noel.libs.creative.documents.data.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class JournalTest {
    @Test
    fun operationIdentitySurvivesRestart() {
        val file = Files.createTempDirectory("journal").resolve("operations.bin").toFile()
        val record = OperationRecord("key", "fingerprint", OperationState.UNCERTAIN)
        FileOperationStore(file).put(record)
        assertEquals(record, FileOperationStore(file).get("key"))
    }
}
