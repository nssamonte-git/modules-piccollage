package noel.libs.creative.canvas.domain

import kotlin.math.*
import noel.libs.creative.model.*

class HitTest {
    operator fun invoke(page: Page, point: Point, tolerance: Double = 0.0): Layer? =
        page.layers.asReversed().firstOrNull { l ->
            if (!l.visible || l.locked || l.opacity == 0f) false
            else {
                val p = l.transform.inverse(point)
                p.x >= -tolerance &&
                    p.y >= -tolerance &&
                    p.x <= l.width + tolerance &&
                    p.y <= l.height + tolerance
            }
        }
}

/** Preview state belongs to the gesture, never to committed document history. */
class CanvasGesture(
    private val pageId: String,
    private val original: Layer,
    private val revision: Long,
) {
    var preview: Transform = original.transform
        private set

    private var canceled = false

    companion object {
        const val MIN_SCALE = 0.01
        const val MAX_SCALE = 100.0

        /** Keeps an accumulated pinch factor inside the range [transformBy] accepts (NaN -> 1). */
        fun clampScale(scale: Double): Double =
            if (scale.isNaN()) 1.0 else scale.coerceIn(MIN_SCALE, MAX_SCALE)
    }

    fun moveBy(dx: Double, dy: Double) {
        require(dx.isFinite() && dy.isFinite())
        preview =
            original.transform.copy(x = original.transform.x + dx, y = original.transform.y + dy)
    }

    /** [scale] is relative to the gesture start and must be in [MIN_SCALE]..[MAX_SCALE]; see [clampScale]. */
    fun transformBy(dx: Double, dy: Double, scale: Double, rotation: Double, pivot: Point? = null) {
        require(scale in MIN_SCALE..MAX_SCALE)
        preview =
            original.transform.copy(
                x = original.transform.x + dx,
                y = original.transform.y + dy,
                scaleX = original.transform.scaleX * scale,
                scaleY = original.transform.scaleY * scale,
                rotation = original.transform.rotation + rotation,
            )
        if (pivot != null) {
            val local = original.transform.inverse(pivot)
            val projected = preview.map(local)
            preview =
                preview.copy(
                    x = preview.x + pivot.x + dx - projected.x,
                    y = preview.y + pivot.y + dy - projected.y,
                )
        }
    }

    fun cancel() {
        canceled = true
        preview = original.transform
    }

    fun finish(): EditRequest? =
        if (canceled || preview == original.transform) null
        else EditRequest(revision, listOf(Edit.TransformLayer(pageId, original.id, preview)))
}

class MoveLayer {
    operator fun invoke(
        session: EditSession,
        pageId: String,
        id: String,
        dx: Double,
        dy: Double,
    ): Outcome<CreativeDocument> {
        val d = session.document.value
        val l =
            d.pages.firstOrNull { it.id == pageId }?.layers?.firstOrNull { it.id == id }
                ?: return failure(CreativeError.Code.INVALID_INPUT, "Missing layer")
        return session.commit(
            EditRequest(
                d.revision,
                listOf(
                    Edit.TransformLayer(
                        pageId,
                        id,
                        l.transform.copy(x = l.transform.x + dx, y = l.transform.y + dy),
                    )
                ),
            )
        )
    }
}

class ReorderLayer {
    operator fun invoke(session: EditSession, pageId: String, id: String, index: Int) =
        session.commit(
            EditRequest(
                session.document.value.revision,
                listOf(Edit.ReorderLayer(pageId, id, index)),
            )
        )
}

class AddDrawing {
    operator fun invoke(
        session: EditSession,
        pageId: String,
        id: String,
        strokes: List<Stroke>,
    ): Outcome<CreativeDocument> {
        if (
            strokes.isEmpty() ||
                strokes.any { it.points.isEmpty() || it.points.size > 100_000 || it.width <= 0 }
        )
            return failure(CreativeError.Code.INVALID_INPUT, "Invalid drawing")
        val p =
            session.document.value.pages.firstOrNull { it.id == pageId }
                ?: return failure(CreativeError.Code.INVALID_INPUT, "Missing page")
        return session.commit(
            EditRequest(
                session.document.value.revision,
                listOf(
                    Edit.AddLayer(
                        pageId,
                        Layer(
                            id,
                            width = p.width.toDouble(),
                            height = p.height.toDouble(),
                            content = Content.Drawing(strokes),
                        ),
                    )
                ),
            )
        )
    }
}
