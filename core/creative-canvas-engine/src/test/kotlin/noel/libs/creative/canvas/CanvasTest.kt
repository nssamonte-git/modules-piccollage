package noel.libs.creative.canvas

import noel.libs.creative.canvas.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class CanvasTest {
    @Test
    fun rotatedTransformRoundTrip() {
        val t = Transform(100.0, 50.0, 2.0, 2.0, 90.0)
        val p = t.map(Point(10.0, 15.0))
        assertEquals(70.0, p.x, 1e-6)
        assertEquals(70.0, p.y, 1e-6)
        assertEquals(10.0, t.inverse(p).x, 1e-6)
    }

    @Test
    fun frontmostUnlockedLayer() {
        val a = Layer("a", content = Content.Text("A"))
        val p = Page("p", 100, 100, layers = listOf(a, a.copy(id = "b", locked = true)))
        assertEquals("a", HitTest()(p, Point(10.0, 10.0))?.id)
    }

    @Test
    fun canceledGestureHasNoCommand() {
        val layer = Layer("l", content = Content.Text("A"))
        val g = CanvasGesture("p", layer, 0)
        g.moveBy(20.0, 10.0)
        g.cancel()
        assertNull(g.finish())
    }

    @Test
    fun extremePinchIsClampedIntoTheAcceptedRange() {
        assertEquals(CanvasGesture.MAX_SCALE, CanvasGesture.clampScale(1e9), 0.0)
        assertEquals(CanvasGesture.MIN_SCALE, CanvasGesture.clampScale(1e-9), 0.0)
        assertEquals(1.0, CanvasGesture.clampScale(Double.NaN), 0.0)
        val g = CanvasGesture("p", Layer("l", content = Content.Text("A")), 0)
        // Accumulating many zoom events stays usable instead of throwing.
        var scale = 1.0
        repeat(200) {
            scale = CanvasGesture.clampScale(scale * 1.5)
            g.transformBy(0.0, 0.0, scale, 0.0)
        }
        assertEquals(CanvasGesture.MAX_SCALE, g.preview.scaleX, 1e-9)
    }
}
