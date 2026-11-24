package noel.libs.creative.renderer.data

import android.graphics.*
import android.text.*
import android.text.style.*
import java.util.regex.Pattern
import noel.libs.creative.model.*
import noel.libs.creative.renderer.api.*

interface ImageEffects {
    fun apply(source: Bitmap, effects: List<Effect>): Bitmap
}

interface ImageMask {
    fun apply(source: Bitmap, mask: Mask): Bitmap
}

interface FontResolver {
    fun resolve(fontId: String): Typeface?
}

/** Bitmap ownership transfers to the caller. Preview and export use this same renderer. */
class DocumentRenderer(
    private val assets: AssetBytes,
    private val effects: ImageEffects? = null,
    private val masks: ImageMask? = null,
    private val fonts: FontResolver =
        object : FontResolver {
            override fun resolve(fontId: String): Typeface? =
                if (fontId in setOf("sans-serif", "serif", "monospace"))
                    Typeface.create(fontId, Typeface.NORMAL)
                else null
        },
    private val maxPixels: Long = 16_000_000,
) : PageRenderer {
    override fun render(
        document: CreativeDocument,
        pageId: String,
        width: Int?,
        height: Int?,
        strict: Boolean,
    ): Outcome<RenderedPage> {
        var output: Bitmap? = null
        return try {
            document.validate()
            val page = document.pages.first { it.id == pageId }
            val w = width ?: page.width
            val h = height ?: page.height
            require(w > 0 && h > 0 && w.toLong() * h <= maxPixels)
            output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            canvas.drawColor(page.background)
            canvas.scale(w.toFloat() / page.width, h.toFloat() / page.height)
            val warnings = mutableListOf<String>()
            for (layer in page.layers.filter { it.visible && it.opacity > 0 }) {
                var bitmap: Bitmap? = null
                try {
                    bitmap =
                        when (val content = layer.content) {
                            is Content.Image -> {
                                val asset = document.assets.first { it.id == content.assetId }
                                val decoded = decode(content.assetId)
                                if (asset.orientation == 0) decoded
                                else
                                    Bitmap.createBitmap(
                                            decoded,
                                            0,
                                            0,
                                            decoded.width,
                                            decoded.height,
                                            Matrix().apply {
                                                postRotate(asset.orientation.toFloat())
                                            },
                                            true,
                                        )
                                        .also { if (it !== decoded) decoded.recycle() }
                            }
                            is Content.Text -> textBitmap(content, layer, warnings)
                            is Content.Drawing -> drawingBitmap(content, layer)
                        }
                    if (layer.effects.isNotEmpty()) {
                        val adapter = effects ?: error("Effects adapter required")
                        val next = adapter.apply(bitmap, layer.effects)
                        if (next !== bitmap) bitmap.recycle()
                        bitmap = next
                    }
                    val mask = layer.mask
                    if (mask != null) {
                        val adapter = masks ?: error("Mask adapter required")
                        val next = adapter.apply(bitmap, mask)
                        if (next !== bitmap) bitmap.recycle()
                        bitmap = next
                    }
                    canvas.save()
                    canvas.translate(layer.transform.x.toFloat(), layer.transform.y.toFloat())
                    canvas.rotate(layer.transform.rotation.toFloat())
                    canvas.scale(layer.transform.scaleX.toFloat(), layer.transform.scaleY.toFloat())
                    val crop = (layer.content as? Content.Image)?.crop
                    val src = crop?.let {
                        Rect(
                            (it.left * bitmap.width).toInt(),
                            (it.top * bitmap.height).toInt(),
                            kotlin.math.ceil(it.right * bitmap.width).toInt(),
                            kotlin.math.ceil(it.bottom * bitmap.height).toInt(),
                        )
                    }
                    canvas.drawBitmap(
                        bitmap,
                        src,
                        RectF(0f, 0f, layer.width.toFloat(), layer.height.toFloat()),
                        Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
                            alpha = (layer.opacity * 255).toInt()
                        },
                    )
                    canvas.restore()
                } catch (e: Exception) {
                    if (strict) throw e
                    warnings += "Layer ${layer.id} unavailable"
                    canvas.drawRect(
                        layer.transform.x.toFloat(),
                        layer.transform.y.toFloat(),
                        (layer.transform.x + layer.width).toFloat(),
                        (layer.transform.y + layer.height).toFloat(),
                        Paint().apply { color = Color.LTGRAY },
                    )
                } finally {
                    bitmap?.recycle()
                }
            }
            Outcome.Success(RenderedPage(output, warnings))
        } catch (e: Exception) {
            output?.recycle()
            failure(CreativeError.Code.INVALID_INPUT, "Render failed: ${e.javaClass.simpleName}")
        }
    }

    fun decode(assetId: String): Bitmap {
        val bytes = (assets.read(assetId) as? Outcome.Success)?.value ?: error("Missing image")
        require(bytes.size <= 64 * 1024 * 1024)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        require(
            bounds.outWidth > 0 &&
                bounds.outHeight > 0 &&
                bounds.outWidth.toLong() * bounds.outHeight <= maxPixels
        )
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: error("Corrupt image")
    }

    private fun dimensions(l: Layer): Pair<Int, Int> {
        val w = kotlin.math.ceil(l.width).toInt()
        val h = kotlin.math.ceil(l.height).toInt()
        require(w > 0 && h > 0 && w.toLong() * h <= maxPixels)
        return w to h
    }

    private fun drawingBitmap(c: Content.Drawing, l: Layer): Bitmap {
        val (w, h) = dimensions(l)
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).also { b ->
            val cv = Canvas(b)
            c.strokes.forEach { s ->
                val path = Path()
                s.points.forEachIndexed { i, p ->
                    if (i == 0) path.moveTo(p.x.toFloat(), p.y.toFloat())
                    else path.lineTo(p.x.toFloat(), p.y.toFloat())
                }
                val paint =
                    Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = s.color
                        strokeWidth = s.width.toFloat()
                        style = Paint.Style.STROKE
                        strokeCap = Paint.Cap.ROUND
                        strokeJoin = Paint.Join.ROUND
                    }
                if (s.points.size == 1)
                    cv.drawPoint(s.points[0].x.toFloat(), s.points[0].y.toFloat(), paint)
                else cv.drawPath(path, paint)
            }
        }
    }

    private fun textBitmap(c: Content.Text, l: Layer, warnings: MutableList<String>): Bitmap {
        val (w, h) = dimensions(l)
        val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val cv = Canvas(result)
        cv.drawColor(c.format.background)
        val face =
            fonts.resolve(c.format.fontId)
                ?: Typeface.DEFAULT.also {
                    warnings += "Font ${c.format.fontId} unavailable; using system fallback"
                }
        val paint =
            TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = c.format.size
                color = c.format.color
                typeface = face
                if (c.format.shadowRadius > 0)
                    setShadowLayer(c.format.shadowRadius, 1f, 1f, Color.BLACK)
            }
        val builder = SpannableString(c.text)
        val matcher = Pattern.compile("\\X").matcher(c.text)
        val boundaries = mutableListOf(0)
        while (matcher.find()) boundaries += matcher.end()
        for (s in c.styles) {
            require(s.start >= 0 && s.end >= s.start && s.end < boundaries.size)
            val start = boundaries[s.start]
            val end = boundaries[s.end]
            builder.setSpan(
                ForegroundColorSpan(s.color),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            builder.setSpan(
                AbsoluteSizeSpan(s.size.toInt()),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            if (s.bold)
                builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
        }
        val alignment =
            when (c.format.alignment) {
                "center" -> Layout.Alignment.ALIGN_CENTER
                "right" -> Layout.Alignment.ALIGN_OPPOSITE
                else -> Layout.Alignment.ALIGN_NORMAL
            }
        val layout =
            StaticLayout.Builder.obtain(builder, 0, builder.length, paint, w)
                .setAlignment(alignment)
                .setIncludePad(false)
                .setLineSpacing(0f, c.format.lineSpacing)
                .build()
        if (c.format.outlineWidth > 0) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = c.format.outlineWidth
            paint.color = c.format.outlineColor
            layout.draw(cv)
            paint.style = Paint.Style.FILL
            paint.color = c.format.color
        }
        layout.draw(cv)
        return result
    }
}
