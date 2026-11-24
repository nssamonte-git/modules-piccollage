package noel.libs.creative.aiimages.data

import android.graphics.BitmapFactory
import noel.libs.creative.aiimages.domain.*
import noel.libs.creative.model.*

class AndroidGeneratedMediaValidator(private val maxPixels: Long = 16_000_000) :
    GeneratedMediaValidator {
    override fun validate(image: GeneratedImage): Outcome<GeneratedImage> =
        try {
            require(
                image.assetId.isNotBlank() &&
                    image.bytes.size <= 64 * 1024 * 1024 &&
                    image.mediaType in setOf("image/png", "image/jpeg")
            )
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(image.bytes, 0, image.bytes.size, options)
            require(
                options.outWidth == image.width &&
                    options.outHeight == image.height &&
                    image.width > 0 &&
                    image.height > 0 &&
                    image.width.toLong() * image.height <= maxPixels &&
                    options.outMimeType == image.mediaType
            )
            val bitmap =
                BitmapFactory.decodeByteArray(image.bytes, 0, image.bytes.size)
                    ?: error("Corrupt returned image")
            bitmap.recycle()
            Outcome.Success(image)
        } catch (_: Exception) {
            failure(CreativeError.Code.INVALID_INPUT, "Returned media failed validation")
        }
}
