package noel.libs.creative.masks.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import noel.libs.creative.masks.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun MaskEditor(
    mask: Mask,
    sourceWidth: Int,
    sourceHeight: Int,
    preview: ImageBitmap?,
    onPreview: (Mask) -> Unit,
    onChange: (Mask) -> Unit,
    modifier: Modifier = Modifier,
) {
    var restore by remember { mutableStateOf(false) }
    var width by remember { mutableFloatStateOf(20f) }
    var gesture by remember { mutableStateOf<MaskGesture?>(null) }
    CreativePanel("Cutout mask", modifier) {
        Row {
            FilterChip(
                restore,
                { restore = !restore },
                label = { Text(if (restore) "Restore" else "Erase") },
            )
            TextButton(onClick = { onChange(mask.copy(inverted = !mask.inverted)) }) {
                Text("Invert")
            }
        }
        Slider(width, { width = it }, valueRange = 1f..100f)
        Canvas(
            Modifier.fillMaxWidth().aspectRatio(sourceWidth.toFloat() / sourceHeight).pointerInput(
                mask,
                width,
                restore,
            ) {
                detectDragGestures(
                    onDragStart = { point ->
                        gesture =
                            MaskGesture(mask, width.toDouble(), restore).also {
                                it.add(
                                    Point(
                                        point.x / size.width * sourceWidth.toDouble(),
                                        point.y / size.height * sourceHeight.toDouble(),
                                    )
                                )
                            }
                    },
                    onDragEnd = {
                        gesture?.let { onChange(it.finish()) }
                        gesture = null
                    },
                    onDragCancel = {
                        gesture?.cancel()
                        gesture = null
                        onPreview(mask)
                    },
                ) { change, _ ->
                    change.consume()
                    gesture?.let {
                        it.add(
                            Point(
                                change.position.x / size.width * sourceWidth.toDouble(),
                                change.position.y / size.height * sourceHeight.toDouble(),
                            )
                        )
                        onPreview(it.finish())
                    }
                }
            }
        ) {
            val cell = 16f
            for (y in 0..(size.height / cell).toInt()) for (x in
                0..(size.width / cell).toInt()) drawRect(
                if ((x + y) % 2 == 0) Color.LightGray else Color.White,
                Offset(x * cell, y * cell),
                Size(cell, cell),
            )
            preview?.let {
                drawImage(it, dstSize = IntSize(size.width.toInt(), size.height.toInt()))
            }
        }
    }
}
