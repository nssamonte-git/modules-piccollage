package noel.libs.logging

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class LoggerTest {
    private val received = mutableListOf<String>()
    private val sink = LogSink { level, tag, message, _ -> received += "$level/$tag: $message" }

    @After
    fun reset() {
        Logger.uninstallAll()
        Logger.minLevel = LogLevel.DEBUG
    }

    @Test
    fun `nothing is built or sent without a sink`() {
        var built = false
        Logger.d("T") { built = true; "x" }
        assertFalse(built)
    }

    @Test
    fun `messages reach every installed sink`() {
        val other = mutableListOf<String>()
        Logger.install(sink, LogSink { _, _, message, _ -> other += message })
        Logger.i("Sync") { "done" }
        assertEquals(listOf("INFO/Sync: done"), received)
        assertEquals(listOf("done"), other)
    }

    @Test
    fun `messages below minLevel are dropped without being built`() {
        Logger.install(sink)
        Logger.minLevel = LogLevel.WARN
        var built = false
        Logger.d("T") { built = true; "debug" }
        Logger.e("T") { "error" }
        assertFalse(built)
        assertEquals(listOf("ERROR/T: error"), received)
    }
}
