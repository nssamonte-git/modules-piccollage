package noel.libs.creative.export.data

import android.graphics.*
import java.io.*
import java.nio.file.*
import java.util.UUID
import kotlinx.coroutines.*
import noel.libs.creative.model.*
import noel.libs.creative.renderer.data.DocumentRenderer

class StillExporter(private val renderer: DocumentRenderer) {
    suspend fun export(
        document: CreativeDocument,
        pageId: String,
        destination: File,
        png: Boolean = true,
        background: Int = Color.WHITE,
    ): Outcome<File> =
        withContext(Dispatchers.IO) {
            val temp = File(destination.parentFile, "image-${UUID.randomUUID()}.tmp")
            var bitmap: Bitmap? = null
            var flat: Bitmap? = null
            try {
                require(!destination.exists())
                destination.parentFile?.mkdirs()
                val result = renderer.render(document.immutableCopy(), pageId, strict = true)
                if (result is Outcome.Failure) return@withContext result
                bitmap = (result as Outcome.Success).value.bitmap
                val output =
                    if (png) bitmap
                    else
                        Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
                            .also {
                                flat = it
                                val c = Canvas(it)
                                c.drawColor(background)
                                c.drawBitmap(bitmap, 0f, 0f, null)
                            }
                ensureActive()
                FileOutputStream(temp).use {
                    require(
                        output.compress(
                            if (png) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
                            90,
                            it,
                        )
                    )
                    it.fd.sync()
                }
                val decoded =
                    BitmapFactory.decodeFile(temp.path) ?: error("Encoded image is invalid")
                try {
                    require(decoded.width == output.width && decoded.height == output.height)
                } finally {
                    decoded.recycle()
                }
                ensureActive()
                Files.move(temp.toPath(), destination.toPath(), StandardCopyOption.ATOMIC_MOVE)
                Outcome.Success(destination)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                failure(CreativeError.Code.IO, "Image export failed: ${e.javaClass.simpleName}")
            } finally {
                bitmap?.recycle()
                flat?.recycle()
                temp.delete()
            }
        }
}
