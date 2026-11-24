package noel.libs.creative.masks.data

import android.graphics.Bitmap
import noel.libs.creative.masks.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.renderer.data.*

class AndroidMask(private val baseMask: ((String, Int, Int) -> ByteArray)? = null) : ImageMask {
    override fun apply(source: Bitmap, mask: Mask): Bitmap {
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)
        val base =
            mask.baseAssetId?.let {
                baseMask?.invoke(it, source.width, source.height)
                    ?: error("Base mask adapter required")
            }
        val coverage = MaskRasterizer.rasterize(source.width, source.height, mask, base)
        return Bitmap.createBitmap(
            MaskRasterizer.composite(pixels, coverage),
            source.width,
            source.height,
            Bitmap.Config.ARGB_8888,
        )
    }
}
