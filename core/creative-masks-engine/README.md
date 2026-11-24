# core-creative-masks-engine

Erase and restore masks for layers. A `Mask` is stored on a `Layer` as a list of brush strokes in source-pixel coordinates, plus an invert flag and an optional base mask from your own segmentation. This module records strokes from a gesture, rasterizes a mask to per-pixel coverage, commits a mask as an undoable edit, and provides `AndroidMask`, the `ImageMask` adapter for the shared `DocumentRenderer`. This is an engine module: it has no Compose dependency.

## When to use it

- Users cut out part of a photo with an eraser brush and bring parts back with a restore brush.
- You have a subject mask from your own segmentation model and want users to touch it up.
- You render documents with `DocumentRenderer` and some layers have a `mask`. Without an `ImageMask` adapter those layers render as placeholders (or fail with `strict = true`).
- For a ready-made brush editor, also add [core-creative-masks-compose](../creative-masks-compose/README.md).

## Add it

```kotlin
implementation("noel.libs:core-creative-masks-engine:0.2.1")
```

It brings in [`core-creative-renderer`](../creative-renderer/README.md), `core-creative-model` and `kotlinx-coroutines-core` through `api`. It is usually paired with `core-creative-history-engine` for the `EditSession` and [core-creative-effects-engine](../creative-effects-engine/README.md) for the other renderer adapter.

## Quick start

```kotlin
import noel.libs.creative.masks.data.AndroidMask
import noel.libs.creative.masks.domain.MaskGesture
import noel.libs.creative.masks.domain.SetMask
import noel.libs.creative.model.Mask
import noel.libs.creative.model.Outcome
import noel.libs.creative.model.Point
import noel.libs.creative.renderer.data.DocumentRenderer

// Once, at your composition root.
val renderer = DocumentRenderer(assets, masks = AndroidMask())

// Record one erase stroke, 24 source pixels wide.
val gesture = MaskGesture(layer.mask ?: Mask(), width = 24.0, restore = false)
gesture.add(Point(120.0, 80.0))
gesture.add(Point(180.0, 95.0))
val next = gesture.finish() // or gesture.cancel() to get the baseline back

// Optional preview on the decoded source. You recycle the result.
val preview = AndroidMask().apply(source, next)

// Commit as one undoable edit.
val result = SetMask()(session, pageId, layer.id, next)
if (result is Outcome.Failure) showError(result.error.message)
```

With a segmentation adapter, set `Mask.baseAssetId` and construct the adapter with a function that returns the base coverage:

```kotlin
val masks =
    AndroidMask(baseMask = { baseAssetId, width, height ->
        mySegmenter.coverage(baseAssetId, width, height) // ByteArray of width * height
    })
```

## Main types

| Type | What it does |
|---|---|
| `MaskGesture(baseline, width, restore)` | Collects `Point`s for one stroke. `finish()` returns the baseline plus the new `MaskStroke`; after `cancel()` or with no points it returns the baseline unchanged. |
| `MaskRasterizer` | `rasterize(width, height, mask, base)` returns one coverage byte per pixel. `composite(source, coverage)` multiplies source alpha by coverage. |
| `SetMask` | Commits a mask to one unlocked layer with `Edit.ReplaceLayer`. Optional `expectedRevision`, defaulting to the current revision. |
| `AndroidMask` | `ImageMask` adapter for `DocumentRenderer`. Optional base-mask function `(baseAssetId, width, height) -> ByteArray`. |

Domain types are in `noel.libs.creative.masks.domain`; `AndroidMask` is in `noel.libs.creative.masks.data`. `Mask`, `MaskStroke` and `Point` are in `noel.libs.creative.model`.

## What your app provides

- An `EditSession` for `SetMask`, and an `AssetBytes` for `DocumentRenderer`.
- Touch input mapped to source-pixel coordinates of the oriented image: the full decoded image after rotation by `Asset.orientation`, before any crop.
- Segmentation, if you want it. The library contains no model. The base-mask function must return exactly `width * height` bytes, 255 to keep and 0 to remove.
- A worker thread for rendering and large previews; all calls are synchronous.
- Bitmap cleanup. `AndroidMask.apply` returns a new `Bitmap` and does not change the source.

## Limits and behaviour

- Coverage 255 (white) keeps a pixel and 0 (black) removes it. Without a base mask everything starts as kept. Erase strokes set 0, restore strokes set 255, in stroke order; `inverted` flips the result.
- Restoring never makes a pixel more opaque than the source: the output alpha is source alpha times coverage, and RGB is unchanged.
- Strokes are round brushes of the given width, filled between points.
- Images are limited to 16 million pixels. Stroke width must be finite, above 0 and at most 8192, with at most 100,000 points per stroke. A document allows at most 10,000 strokes per mask. `MaskGesture` throws `IllegalArgumentException` for a width that is not finite and above 0, a non-finite point, or more than 100,000 points.
- If `baseAssetId` is set but `AndroidMask` has no base-mask function, `apply` throws `IllegalStateException`.
- `SetMask` returns `INVALID_INPUT` ("Missing editable layer") for a missing or locked layer; other checks happen when the edit is committed.
- Checkerboard backgrounds are a UI concern; the engine never draws one.

## Tests

`MaskTest` covers: erase and restore keep the source alpha unchanged, a cancelled gesture returns the baseline mask, and an inverted empty mask removes everything.

```bash
./gradlew :core-creative-masks-engine:testDebugUnitTest
```

The sample's device test `CreativeAndroidTest.maskRestoresCoverageWithoutChangingSourceAlpha` runs `AndroidMask` on a real `Bitmap`:

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-masks-compose](../creative-masks-compose/README.md) for the `MaskEditor` brush UI.
- [core-creative-effects-engine](../creative-effects-engine/README.md) for the effects adapter used by the same renderer.
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text).
- Sample: `DocumentRenderer(assets, GpuImageEffects(), AndroidMask())` in [`DemoEnvironment.kt`](../../sample/src/main/kotlin/noel/libs/sample/DemoEnvironment.kt), the "Masks" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt), and [`CreativeAndroidTest.kt`](../../sample/src/androidTest/kotlin/noel/libs/sample/CreativeAndroidTest.kt).
