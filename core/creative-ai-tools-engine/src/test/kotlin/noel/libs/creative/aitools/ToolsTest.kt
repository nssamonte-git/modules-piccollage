package noel.libs.creative.aitools

import kotlinx.coroutines.test.runTest
import noel.libs.creative.aitools.domain.*
import noel.libs.creative.history.domain.HistorySession
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class ToolsTest {
    private fun session(): EditSession {
        val l = Layer("l", content = Content.Text("A"))
        return HistorySession(
            CreativeDocument("d", pages = listOf(Page("p", 100, 100, layers = listOf(l))))
        )
    }

    private fun call(id: String = "op", revision: Long = 0) =
        ToolCall(
            "c",
            id,
            "account",
            "d",
            revision,
            "move",
            mapOf("pageId" to "p", "layerId" to "l", "x" to "4", "y" to "5"),
        )

    @Test
    fun duplicateDoesNotMutateTwice() = runTest {
        val s = session()
        val runner = ToolRunner(MemoryOperationStore())
        assertTrue(runner.execute(call(), s) is Outcome.Success)
        assertTrue(runner.execute(call(), s) is Outcome.Success)
        assertEquals(1L, s.document.value.revision)
    }

    @Test
    fun fingerprintConflict() = runTest {
        val s = session()
        val runner = ToolRunner(MemoryOperationStore())
        runner.execute(call(), s)
        assertTrue(
            runner.execute(call().copy(arguments = call().arguments + ("x" to "8")), s)
                is Outcome.Failure
        )
    }

    @Test
    fun unknownToolAndExtraFields() = runTest {
        val runner = ToolRunner(MemoryOperationStore())
        assertTrue(runner.execute(call().copy(name = "executeCode"), session()) is Outcome.Failure)
        assertTrue(
            runner.execute(call().copy(arguments = call().arguments + ("extra" to "x")), session())
                is Outcome.Failure
        )
    }

    @Test
    fun staleRevision() = runTest {
        assertTrue(
            ToolRunner(MemoryOperationStore()).execute(call(revision = 4), session())
                is Outcome.Failure
        )
    }

    @Test
    fun fullJournalReturnsLimitFailureWithoutEditing() = runTest {
        val s = session()
        val runner = ToolRunner(MemoryOperationStore(maxEntries = 1))
        assertTrue(runner.execute(call("first"), s) is Outcome.Success)
        val second = runner.execute(call("second", revision = 1), s)
        assertEquals(CreativeError.Code.LIMIT, (second as Outcome.Failure).error.code)
        assertEquals(1L, s.document.value.revision)
        // An operation already in the journal still replays.
        assertTrue(runner.execute(call("first"), s) is Outcome.Success)
    }
}
