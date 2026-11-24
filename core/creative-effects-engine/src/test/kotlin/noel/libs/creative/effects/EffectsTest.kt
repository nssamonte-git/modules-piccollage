package noel.libs.creative.effects

import noel.libs.creative.effects.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class EffectsTest {
    @Test
    fun neutralSettingsPreservePixels() {
        val p = intArrayOf(0x804080c0.toInt())
        assertArrayEquals(
            p,
            PixelEffects.apply(
                p,
                1,
                1,
                listOf(
                    Effect(Effect.Kind.BRIGHTNESS, 0f),
                    Effect(Effect.Kind.CONTRAST, 1f),
                    Effect(Effect.Kind.SATURATION, 1f),
                ),
            ),
        )
    }

    @Test
    fun alphaPreserved() {
        val p =
            PixelEffects.apply(
                intArrayOf(0x804080c0.toInt()),
                1,
                1,
                listOf(Effect(Effect.Kind.GRAYSCALE, 1f)),
            )
        assertEquals(128, p[0] ushr 24)
    }

    @Test
    fun cyclicGraphRejected() {
        val a = EffectNode("a", listOf("b"), Effect(Effect.Kind.BRIGHTNESS))
        val b = EffectNode("b", listOf("a"), Effect(Effect.Kind.BLUR, 1f))
        assertTrue(ValidateEffectGraph()(listOf(a, b)) is Outcome.Failure)
    }
}
