package noel.libs.creative.export

import noel.libs.creative.export.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class TimelineTest {
    @Test
    fun overlapDuration() {
        val t = Timeline(listOf(Clip.Photo("a", 2_000_000), Clip.Photo("b", 2_000_000)), 500_000)
        assertEquals(3_500_000, t.durationUs)
        assertEquals(listOf(0L, 1_500_000L), t.startsUs)
    }

    @Test
    fun musicOffset() {
        val m = Music("m", sourceTrimUs = 1_250_000, placementUs = 250_000)
        assertEquals(1_750_000L, m.sourceTimeAt(750_000))
        assertNull(m.sourceTimeAt(0))
    }

    @Test
    fun unsupportedOverlapRejected() {
        try {
            Timeline(listOf(Clip.Video("v", 0, 2_000_000), Clip.Photo("a", 2_000_000)), 500_000)
            fail()
        } catch (_: IllegalArgumentException) {}
    }
}
