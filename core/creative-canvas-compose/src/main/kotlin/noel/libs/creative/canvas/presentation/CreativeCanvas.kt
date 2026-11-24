package noel.libs.creative.canvas.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noel.libs.creative.canvas.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.renderer.api.PageRenderer

@Composable
fun CreativeCanvas(
    session: EditSession,
    renderer: PageRenderer,
    pageId: String,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val document by session.document.collectAsState()
    var preview by remember { mutableStateOf<Layer?>(null) }
    val page = document.pages.firstOrNull { it.id == pageId } ?: return
    val displayed =
        preview?.let { p ->
            document.copy(
                pages =
                    document.pages.map {
                        if (it.id == pageId)
                            it.copy(layers = it.layers.map { if (it.id == p.id) p else it })
                        else it
                    }
            )
        } ?: document
    val rendered by
        produceState<android.graphics.Bitmap?>(null, displayed, renderer, pageId) {
            val result = withContext(Dispatchers.Default) { renderer.render(displayed, pageId) }
            val bitmap = (result as? Outcome.Success)?.value?.bitmap
            value = bitmap
            awaitDispose { bitmap?.recycle() }
        }
    Canvas(
        modifier.fillMaxWidth().aspectRatio(page.width.toFloat() / page.height).pointerInput(
            session,
            pageId,
            document.revision,
        ) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                val point =
                    Point(
                        down.position.x / size.width * page.width.toDouble(),
                        down.position.y / size.height * page.height.toDouble(),
                    )
                val layer = HitTest()(page, point)
                onSelect(layer?.id)
                if (layer != null) {
                    val gesture = CanvasGesture(pageId, layer, document.revision)
                    var pan = Offset.Zero
                    var scale = 1.0
                    var rotation = 0.0
                    try {
                        do {
                            val event = awaitPointerEvent()
                            pan += event.calculatePan()
                            // Clamp so an extreme pinch can't push transformBy out of range.
                            scale = CanvasGesture.clampScale(scale * event.calculateZoom())
                            rotation += event.calculateRotation()
                            gesture.transformBy(
                                pan.x / size.width * page.width.toDouble(),
                                pan.y / size.height * page.height.toDouble(),
                                scale,
                                rotation,
                                point,
                            )
                            preview = layer.copy(transform = gesture.preview)
                            event.changes.forEach { if (it.positionChanged()) it.consume() }
                        } while (event.changes.any { it.pressed })
                        gesture.finish()?.let { session.commit(it) }
                    } catch (e: CancellationException) {
                        gesture.cancel()
                        throw e
                    } finally {
                        preview = null
                    }
                }
            }
        }
    ) {
        rendered
            ?.takeUnless { it.isRecycled }
            ?.let {
                drawImage(
                    it.asImageBitmap(),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                )
            }
        page.layers
            .firstOrNull { it.id == selectedId }
            ?.let { l ->
                val t = preview?.takeIf { it.id == l.id }?.transform ?: l.transform
                val corners =
                    listOf(
                            Point(0.0, 0.0),
                            Point(l.width, 0.0),
                            Point(l.width, l.height),
                            Point(0.0, l.height),
                        )
                        .map { t.map(it) }
                for (i in corners.indices) {
                    val a = corners[i]
                    val b = corners[(i + 1) % 4]
                    drawLine(
                        Color(0xff1565c0),
                        Offset(
                            (a.x / page.width * size.width).toFloat(),
                            (a.y / page.height * size.height).toFloat(),
                        ),
                        Offset(
                            (b.x / page.width * size.width).toFloat(),
                            (b.y / page.height * size.height).toFloat(),
                        ),
                        2f,
                    )
                }
            }
    }
}
