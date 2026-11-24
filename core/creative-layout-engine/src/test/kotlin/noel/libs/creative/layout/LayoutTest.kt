package noel.libs.creative.layout

import noel.libs.creative.layout.domain.*
import org.junit.Assert.*
import org.junit.Test

class LayoutTest {
    @Test
    fun globalAssignmentBeatsGreedy() {
        assertArrayEquals(
            intArrayOf(1, 0),
            Assignment.solve(arrayOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(2.0, 100.0))),
        )
    }

    @Test
    fun orientation() {
        val p = orient(Roi(0.1, 0.2, 0.3, 0.4), 90, false)
        assertEquals(0.6, p.left, 1e-6)
        assertEquals(0.1, p.top, 1e-6)
    }

    @Test
    fun emptyInput() {
        assertTrue(Assignment.solve(emptyArray()).isEmpty())
    }

    @Test
    fun unequalCountsRejected() {
        try {
            Assignment.solve(arrayOf(doubleArrayOf(1.0, 2.0)))
            fail()
        } catch (_: IllegalArgumentException) {}
    }
}
