package noel.libs.sample

import android.content.Context
import android.graphics.*
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noel.libs.creative.documents.data.*
import noel.libs.creative.effects.data.*
import noel.libs.creative.export.data.*
import noel.libs.creative.masks.data.*
import noel.libs.creative.model.*
import noel.libs.creative.renderer.data.*

/** Original generated fixtures. No media is imported from PicCollage. */
class DemoEnvironment(val context: Context, private val diagnostics: (Throwable) -> Unit = {}) {
    val bytes = ConcurrentHashMap<String, ByteArray>()
    val assets =
        object : AssetBytes {
            override fun read(assetId: String): Outcome<ByteArray> =
                bytes[assetId]?.let { Outcome.Success(it.copyOf()) }
                    ?: failure(CreativeError.Code.MISSING_ASSET, "Sample asset unavailable")
        }
    val repository = FileDocumentRepository(File(context.filesDir, "drafts"), assets)
    val journal = FileOperationStore(File(context.filesDir, "operations.bin"))
    val renderer = DocumentRenderer(assets, GpuImageEffects(), AndroidMask())
    val music = File(context.cacheDir, "sample-music-${java.util.UUID.randomUUID()}.wav")
    val video =
        VideoExporter(
            context,
            renderer,
            object : LocalMedia {
                override fun uri(assetId: String): Uri {
                    require(assetId == "music")
                    return Uri.fromFile(music)
                }
            },
            diagnostics,
        )

    init {
        val image = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawColor(Color.rgb(240, 226, 204))
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.rgb(41, 91, 85)
        canvas.drawCircle(450f, 160f, 145f, paint)
        paint.color = Color.rgb(208, 112, 72)
        canvas.drawRoundRect(50f, 230f, 470f, 430f, 60f, 60f, paint)
        bytes["photo"] =
            ByteArrayOutputStream().use {
                image.compress(Bitmap.CompressFormat.PNG, 100, it)
                it.toByteArray()
            }
        image.recycle()
        if (!music.isFile) writeMusic(music)
    }

    suspend fun reopen(account: String, id: String): Outcome<CreativeDocument> =
        withContext(Dispatchers.IO) {
            val result = repository.load(account, id)
            if (result is Outcome.Success)
                for (asset in result.value.assets) {
                    val data = repository.readAsset(account, id, asset.id)
                    if (data is Outcome.Success) bytes[asset.id] = data.value
                }
            result
        }

    fun document(): CreativeDocument {
        val photo =
            Layer("photo-layer", Transform(20.0, 80.0), 600.0, 380.0, Content.Image("photo"))
        val title =
            Layer(
                "title",
                Transform(24.0, 15.0),
                590.0,
                60.0,
                Content.Text("Make something yours", TextFormat(size = 36f)),
            )
        val first = Page("page-1", 640, 480, Color.WHITE, listOf(photo, title))
        val second =
            first.copy(
                id = "page-2",
                background = Color.rgb(232, 242, 239),
                layers =
                    listOf(
                        photo.copy(id = "photo-2"),
                        title.copy(
                            id = "title-2",
                            content = Content.Text("A second perspective", TextFormat(size = 36f)),
                        ),
                    ),
            )
        return CreativeDocument(
            "sample-project",
            pages = listOf(first, second),
            assets = listOf(Asset("photo", "image/png", 640, 480)),
            title = "Creative playground",
        )
    }

    private fun writeMusic(file: File) {
        val rate = 48000
        val count = rate * 5
        val buffer = ByteBuffer.allocate(44 + count * 2).order(ByteOrder.LITTLE_ENDIAN)
        buffer.put("RIFF".toByteArray())
        buffer.putInt(36 + count * 2)
        buffer.put("WAVEfmt ".toByteArray())
        buffer.putInt(16)
        buffer.putShort(1)
        buffer.putShort(1)
        buffer.putInt(rate)
        buffer.putInt(rate * 2)
        buffer.putShort(2)
        buffer.putShort(16)
        buffer.put("data".toByteArray())
        buffer.putInt(count * 2)
        repeat(count) { i ->
            buffer.putShort(
                (kotlin.math.sin(i * 2 * Math.PI * 220 / rate) * 2000).toInt().toShort()
            )
        }
        file.writeBytes(buffer.array())
    }
}
