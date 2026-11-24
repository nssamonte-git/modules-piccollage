package noel.libs.creative.masks.domain

import kotlin.math.*
import noel.libs.creative.model.*

class MaskGesture(
    private val baseline: Mask,
    private val width: Double,
    private val restore: Boolean = false,
) {
    private val samples = mutableListOf<Point>()
    private var canceled = false

    init {
        require(width.isFinite() && width > 0)
    }

    fun add(point: Point) {
        require(point.x.isFinite() && point.y.isFinite() && samples.size < 100_000)
        samples += point
    }

    fun cancel() {
        canceled = true
    }

    fun finish(): Mask =
        if (canceled || samples.isEmpty()) baseline
        else
            baseline.copy(strokes = baseline.strokes + MaskStroke(samples.toList(), width, restore))
}

object MaskRasterizer {
    fun rasterize(width: Int, height: Int, mask: Mask, base: ByteArray? = null): ByteArray {
        require(width > 0 && height > 0 && width.toLong() * height <= 16_000_000)
        require(mask.baseAssetId == null || base != null)
        require(base == null || base.size == width * height)
        val output = base?.copyOf() ?: ByteArray(width * height) { 255.toByte() }
        mask.strokes.forEach { s ->
            require(
                s.width > 0 && s.width.isFinite() && s.width <= 8192 && s.points.size <= 100_000
            )
            fun paint(p: Point) {
                val radius = s.width / 2
                val xmin = floor(p.x - radius).toInt().coerceAtLeast(0)
                val xmax = ceil(p.x + radius).toInt().coerceAtMost(width - 1)
                val ymin = floor(p.y - radius).toInt().coerceAtLeast(0)
                val ymax = ceil(p.y + radius).toInt().coerceAtMost(height - 1)
                for (y in ymin..ymax) for (x in xmin..xmax) if (
                    hypot(x + 0.5 - p.x, y + 0.5 - p.y) <= radius
                )
                    output[y * width + x] = if (s.restore) 255.toByte() else 0
            }
            s.points.forEachIndexed { i, p ->
                if (i == 0) paint(p)
                else {
                    val a = s.points[i - 1]
                    val steps =
                        ceil(hypot(p.x - a.x, p.y - a.y) / max(0.5, s.width / 4))
                            .toInt()
                            .coerceIn(1, 100_000)
                    for (j in 1..steps) {
                        val t = j.toDouble() / steps
                        paint(Point(a.x + (p.x - a.x) * t, a.y + (p.y - a.y) * t))
                    }
                }
            }
        }
        if (mask.inverted)
            for (i in output.indices) output[i] = (255 - (output[i].toInt() and 255)).toByte()
        return output
    }

    fun composite(source: IntArray, coverage: ByteArray): IntArray {
        require(source.size == coverage.size)
        return IntArray(source.size) { i ->
            val alpha = ((source[i] ushr 24) * (coverage[i].toInt() and 255) + 127) / 255
            (source[i] and 0x00ffffff) or (alpha shl 24)
        }
    }
}

class SetMask {
    operator fun invoke(
        session: EditSession,
        pageId: String,
        layerId: String,
        mask: Mask,
        expectedRevision: Long = session.document.value.revision,
    ): Outcome<CreativeDocument> {
        val l =
            session.document.value.pages
                .firstOrNull { it.id == pageId }
                ?.layers
                ?.firstOrNull { it.id == layerId && !it.locked }
                ?: return failure(CreativeError.Code.INVALID_INPUT, "Missing editable layer")
        return session.commit(
            EditRequest(expectedRevision, listOf(Edit.ReplaceLayer(pageId, l.copy(mask = mask))))
        )
    }
}
