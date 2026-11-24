package noel.libs.creative.layout.domain

import kotlin.math.*
import noel.libs.creative.model.*

data class Roi(val left: Double, val top: Double, val right: Double, val bottom: Double) {
    init {
        require(
            listOf(left, top, right, bottom).all { it.isFinite() } &&
                left in 0.0..1.0 &&
                top in 0.0..1.0 &&
                right in left..1.0 &&
                bottom in top..1.0
        )
    }
}

fun orient(roi: Roi, clockwise: Int, mirror: Boolean): Roi {
    require(clockwise in listOf(0, 90, 180, 270))
    val points =
        listOf(
                Point(roi.left, roi.top),
                Point(roi.right, roi.top),
                Point(roi.left, roi.bottom),
                Point(roi.right, roi.bottom),
            )
            .map { p ->
                var q = p
                repeat(clockwise / 90) { q = Point(1 - q.y, q.x) }
                if (mirror) Point(1 - q.x, q.y) else q
            }
    return Roi(
        points.minOf { it.x },
        points.minOf { it.y },
        points.maxOf { it.x },
        points.maxOf { it.y },
    )
}

/** Hungarian minimum-cost perfect assignment; deterministic column-order tie breaks. */
object Assignment {
    fun solve(cost: Array<DoubleArray>): IntArray {
        val n = cost.size
        if (n == 0) return intArrayOf()
        require(
            n <= 128 && cost.all { it.size == n && it.all { v -> v.isFinite() && abs(v) < 1e12 } }
        )
        val u = DoubleArray(n + 1)
        val v = DoubleArray(n + 1)
        val p = IntArray(n + 1)
        val way = IntArray(n + 1)
        for (i in 1..n) {
            p[0] = i
            var j0 = 0
            val minv = DoubleArray(n + 1) { Double.POSITIVE_INFINITY }
            val used = BooleanArray(n + 1)
            do {
                used[j0] = true
                val i0 = p[j0]
                var delta = Double.POSITIVE_INFINITY
                var j1 = 0
                for (j in 1..n) if (!used[j]) {
                    val cur = cost[i0 - 1][j - 1] - u[i0] - v[j]
                    if (cur < minv[j]) {
                        minv[j] = cur
                        way[j] = j0
                    }
                    if (minv[j] < delta) {
                        delta = minv[j]
                        j1 = j
                    }
                }
                for (j in 0..n) if (used[j]) {
                    u[p[j]] += delta
                    v[j] -= delta
                } else minv[j] -= delta
                j0 = j1
            } while (p[j0] != 0)
            do {
                val j1 = way[j0]
                p[j0] = p[j1]
                j0 = j1
            } while (j0 != 0)
        }
        return IntArray(n).also { a -> for (j in 1..n) a[p[j] - 1] = j - 1 }
    }
}

data class LayoutPhoto(val asset: Asset, val roi: Roi? = null)

data class Slot(val id: String, val x: Double, val y: Double, val width: Double, val height: Double)

data class Placement(val assetId: String, val slot: Slot, val crop: Roi, val cost: Double)

class SuggestLayout {
    operator fun invoke(photos: List<LayoutPhoto>, slots: List<Slot>): Outcome<List<Placement>> {
        if (photos.isEmpty() || photos.size != slots.size)
            return failure(CreativeError.Code.INVALID_INPUT, "Provide one slot per photo")
        return try {
            require(slots.all { it.width > 0 && it.height > 0 && it.x >= 0 && it.y >= 0 })
            val crops =
                Array(photos.size) { i -> Array(slots.size) { j -> crop(photos[i], slots[j]) } }
            val costs =
                Array(photos.size) { i ->
                    DoubleArray(slots.size) { j ->
                        val roi = photos[i].roi
                        val c = crops[i][j]
                        val visible =
                            roi == null ||
                                (roi.left >= c.left - 1e-8 &&
                                    roi.right <= c.right + 1e-8 &&
                                    roi.top >= c.top - 1e-8 &&
                                    roi.bottom <= c.bottom + 1e-8)
                        if (!visible) 1e9
                        else
                            abs(
                                ln(
                                    (photos[i].asset.width.toDouble() / photos[i].asset.height) /
                                        (slots[j].width / slots[j].height)
                                )
                            )
                    }
                }
            val assignment = Assignment.solve(costs)
            if (assignment.indices.any { costs[it][assignment[it]] >= 1e9 })
                return failure(
                    CreativeError.Code.UNAVAILABLE,
                    "No layout preserves every required subject",
                )
            Outcome.Success(
                assignment.indices.map { i ->
                    val j = assignment[i]
                    Placement(photos[i].asset.id, slots[j], crops[i][j], costs[i][j])
                }
            )
        } catch (e: IllegalArgumentException) {
            failure(CreativeError.Code.INVALID_INPUT, "Invalid layout geometry")
        }
    }

    private fun crop(photo: LayoutPhoto, slot: Slot): Roi {
        val ar = photo.asset.width.toDouble() / photo.asset.height
        val target = slot.width / slot.height
        val w = if (ar > target) target / ar else 1.0
        val h = if (ar > target) 1.0 else ar / target
        val cx = photo.roi?.let { (it.left + it.right) / 2 } ?: 0.5
        val cy = photo.roi?.let { (it.top + it.bottom) / 2 } ?: 0.5
        val l = (cx - w / 2).coerceIn(0.0, 1 - w)
        val t = (cy - h / 2).coerceIn(0.0, 1 - h)
        return Roi(l, t, l + w, t + h)
    }
}

data class AnalysisKey(
    val accountId: String,
    val assetId: String,
    val revision: String,
    val orientation: Int,
    val analyzerVersion: String,
)

class AnalysisCache(private val maxEntries: Int = 128) {
    private val values = LinkedHashMap<AnalysisKey, List<Roi>>()

    @Synchronized
    fun put(key: AnalysisKey, regions: List<Roi>) {
        values[key] = regions.toList()
        while (values.size > maxEntries) values.remove(values.keys.first())
    }

    @Synchronized fun get(key: AnalysisKey): List<Roi>? = values[key]?.toList()

    @Synchronized
    fun purge(accountId: String, assetId: String) {
        values.keys.removeAll { it.accountId == accountId && it.assetId == assetId }
    }
}
