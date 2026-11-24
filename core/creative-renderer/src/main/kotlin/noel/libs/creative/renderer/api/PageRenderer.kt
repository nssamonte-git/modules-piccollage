package noel.libs.creative.renderer.api

import android.graphics.Bitmap
import noel.libs.creative.model.*

data class RenderedPage(val bitmap: Bitmap, val warnings: List<String>)

/** Platform-facing rendering API. Call on a worker thread; caller owns the returned bitmap. */
interface PageRenderer {
    fun render(
        document: CreativeDocument,
        pageId: String,
        width: Int? = null,
        height: Int? = null,
        strict: Boolean = false,
    ): Outcome<RenderedPage>
}
