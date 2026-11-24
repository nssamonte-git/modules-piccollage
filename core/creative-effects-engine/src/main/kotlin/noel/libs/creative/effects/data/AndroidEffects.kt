package noel.libs.creative.effects.data

import android.graphics.Bitmap
import noel.libs.creative.effects.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.renderer.data.ImageEffects

class CpuImageEffects : ImageEffects {
    override fun apply(source: Bitmap, effects: List<Effect>): Bitmap {
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)
        val output = PixelEffects.apply(pixels, source.width, source.height, effects)
        return Bitmap.createBitmap(output, source.width, source.height, Bitmap.Config.ARGB_8888)
    }
}
