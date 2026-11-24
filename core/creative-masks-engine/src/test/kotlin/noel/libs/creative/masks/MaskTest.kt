package noel.libs.creative.masks

import noel.libs.creative.masks.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class MaskTest {
    @Test
    fun eraseRestorePreservesSourceAlpha() {
        val source = intArrayOf(0x804080c0.toInt())
        assertEquals(128, MaskRasterizer.composite(source, byteArrayOf(255.toByte()))[0] ushr 24)
        assertEquals(0, MaskRasterizer.composite(source, byteArrayOf(0))[0] ushr 24)
        assertEquals(128, source[0] ushr 24)
    }

    @Test
    fun canceledStrokeRestoresMask() {
        val s = MaskGesture(Mask(), 3.0)
        s.add(Point(1.0, 1.0))
        s.cancel()
        assertEquals(Mask(), s.finish())
    }

    @Test
    fun invertedMask() {
        assertEquals(0, MaskRasterizer.rasterize(1, 1, Mask(inverted = true))[0].toInt())
    }
}
