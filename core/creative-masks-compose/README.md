# core-creative-masks-compose

A Compose brush editor for [core-creative-masks-engine](../creative-masks-engine/README.md). `MaskEditor` draws your preview image over a checkerboard, turns drag gestures into erase or restore strokes in source-pixel coordinates, and has an Erase/Restore toggle, an Invert button and a brush-width slider. It asks your app for previews during a drag and reports the finished mask at the end. This is a Compose UI module.

## When to use it

- You want a working cutout editor for one image layer without writing gesture code.
- Use the engine module alone if the mask brush must work directly on your canvas, or you need zoom, undo inside a stroke, or other tools.

## Add it

```kotlin
implementation("noel.libs:core-creative-masks-compose:0.2.2")
```

It brings in `core-creative-masks-engine` (and through it `core-creative-renderer`), `core-creative-model`, `kotlinx-coroutines-core`, and the Compose BOM, `compose-ui` and `compose-foundation-layout` through `api`. Material 3 and `core-creative-compose-common` are internal (`implementation`).

## Quick start

Based on the sample app, inside a composable. `renderer` is your `DocumentRenderer`. Note that `decode` does not apply `Asset.orientation`; if the asset has one, rotate the bitmap so it matches what the renderer masks.

```kotlin
import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import noel.libs.creative.masks.data.AndroidMask
import noel.libs.creative.masks.domain.SetMask
import noel.libs.creative.masks.presentation.MaskEditor
import noel.libs.creative.model.Content
import noel.libs.creative.model.Mask

val source = remember(layer.id) { renderer.decode((layer.content as Content.Image).assetId) }
var preview by remember { mutableStateOf<Bitmap?>(null) }
DisposableEffect(source) { onDispose { source.recycle(); preview?.recycle() } }

fun showPreview(mask: Mask) {
    val next = AndroidMask().apply(source, mask)
    preview?.recycle()
    preview = next
}
LaunchedEffect(layer.mask) { showPreview(layer.mask ?: Mask()) }

MaskEditor(
    mask = layer.mask ?: Mask(),
    sourceWidth = source.width,
    sourceHeight = source.height,
    preview = preview?.takeUnless { it.isRecycled }?.asImageBitmap(),
    onPreview = { showPreview(it) },
    onChange = { SetMask()(session, page.id, layer.id, it) },
)
```

## Main types

| Type | What it does |
|---|---|
| `MaskEditor(mask, sourceWidth, sourceHeight, preview, onPreview, onChange, modifier)` | Panel titled "Cutout mask" with the brush controls and a drawing area that has the source's aspect ratio. |

Package: `noel.libs.creative.masks.presentation`.

## What your app provides

- The current `mask` of the layer, read from the document so the editor follows undo and redo.
- `sourceWidth` and `sourceHeight` of the oriented source image, used to map touches to source pixels. Both must be above 0.
- The `preview` image. The editor never renders the mask itself; it draws whatever `ImageBitmap` you pass, scaled to its size.
- `onPreview`: render a preview for an in-progress mask. It is called on every drag move, so keep it fast or use a smaller source for previews.
- `onChange`: commit the mask, for example with `SetMask`.
- Bitmap cleanup for the source and every preview.
- A `MaterialTheme` around it; it uses Material 3 controls.

## Limits and behaviour

- The brush starts in Erase mode with a width of 20 source pixels; the slider ranges from 1 to 100. These settings live in `remember` state inside the editor.
- During a drag, `onPreview` gets the baseline mask plus the stroke so far. At drag end, `onChange` gets the finished mask. If the drag is cancelled, the stroke is dropped and `onPreview` gets the original mask.
- "Invert" calls `onChange` right away with `inverted` flipped.
- The checkerboard is drawn only in this editor, behind the preview. It is never part of the mask or the rendered output.
- There is no zoom or pan.

## Tests

This module has no unit tests. Gesture, rasterizing and compositing logic is tested in `core-creative-masks-engine`.

## See also

- [core-creative-masks-engine](../creative-masks-engine/README.md) for `MaskGesture`, `AndroidMask`, `SetMask` and the limits.
- [core-creative-effects-compose](../creative-effects-compose/README.md) for the effect sliders.
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text).
- Sample: the "Masks" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
