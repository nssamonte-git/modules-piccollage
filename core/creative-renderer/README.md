# core-creative-renderer

Draws one page of a `CreativeDocument` into an Android `Bitmap`. It defines the `PageRenderer` interface and `DocumentRenderer`, the Android implementation that decodes images, lays out text and draws strokes. Preview (the canvas) and export use the same renderer. It is an engine module with no Compose.

## When to use it

- You need a page as a bitmap: for an on-screen preview, a thumbnail, or as input to export.
- `core-creative-canvas-compose`, `core-creative-effects-engine`, `core-creative-masks-engine` and `core-creative-export-engine` already bring it in through `api`.
- For effects and masks, pass the adapters from `core-creative-effects-engine` (`GpuImageEffects`) and `core-creative-masks-engine` (`AndroidMask`).

## Add it

```kotlin
implementation("noel.libs:core-creative-renderer:0.2.0")
```

Brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`.

## Quick start

```kotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noel.libs.creative.model.AssetBytes
import noel.libs.creative.model.CreativeDocument
import noel.libs.creative.model.Outcome
import noel.libs.creative.renderer.data.DocumentRenderer

suspend fun preview(assets: AssetBytes, document: CreativeDocument, pageId: String) {
    val renderer = DocumentRenderer(assets)
    val result = withContext(Dispatchers.Default) { renderer.render(document, pageId, 320, 240) }
    when (result) {
        is Outcome.Success -> {
            val page = result.value
            // show page.bitmap, report page.warnings, then:
            page.bitmap.recycle()
        }
        is Outcome.Failure -> println(result.error.message)
    }
}
```

The sample builds it as `DocumentRenderer(assets, GpuImageEffects(), AndroidMask())`.

## Main types

| Type | What it does |
|---|---|
| `PageRenderer` (`renderer.api`) | `render(document, pageId, width, height, strict)` returning `Outcome<RenderedPage>`. |
| `RenderedPage` | The output `bitmap` and a list of `warnings`. |
| `DocumentRenderer` (`renderer.data`) | `PageRenderer` implementation. Constructor: `assets`, `effects`, `masks`, `fonts`, `maxPixels`. |
| `ImageEffects` | Adapter that applies a layer's `Effect` list to a bitmap. |
| `ImageMask` | Adapter that applies a layer's `Mask` to a bitmap. |
| `FontResolver` | Maps a `TextFormat.fontId` to a `Typeface`, or `null`. |

## What your app provides

- An `AssetBytes` that returns the encoded image bytes for each asset ID.
- An `ImageEffects` if any layer has effects, and an `ImageMask` if any layer has a mask. Without them those layers fail (see below).
- Optionally a `FontResolver`. The default resolves only `sans-serif`, `serif` and `monospace`. A custom resolver must return fonts that are already available on the device.
- Threading: `render` is synchronous. Call it on a worker thread.
- Ownership: the caller owns the returned bitmap and must recycle it.
- `StillExporter` and `VideoExporter` take a `DocumentRenderer`, not a `PageRenderer`, so keep the concrete instance if you export.

## Limits and behaviour

- `width` and `height` default to the page size; the page is scaled to fit. Output, decoded images and layer bitmaps are each limited to `maxPixels` (default 16,000,000). Image bytes over 64 MiB are rejected.
- The document is validated first. Invalid input, a missing page or a too-large size returns `Outcome.Failure` with code `INVALID_INPUT`.
- Hidden layers and layers with opacity 0 are skipped. Image layers are rotated by `Asset.orientation` and cropped by `Crop`.
- When a layer cannot be drawn (missing or corrupt asset, missing adapter), the default mode draws a light gray box at the layer's position, adds `Layer <id> unavailable` to `warnings` and continues. With `strict = true` the whole render fails instead.
- A font the resolver cannot find falls back to the system default and adds a warning; the saved `fontId` is unchanged.
- `TextStyle.start` and `end` are grapheme (user-perceived character) indices, not UTF-16 indices. Alignment is `left`, `center` or `right`.

## Tests

This module has no unit tests. The sample's device test `pngAndJpegAreDecodable` renders the sample page through `DocumentRenderer` (via `StillExporter`) and checks the output size:

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-model](../creative-model/README.md), [core-creative-canvas-compose](../creative-canvas-compose/README.md)
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text)
- Sample: `sample/src/main/kotlin/noel/libs/sample/DemoEnvironment.kt` (the `renderer` property)
