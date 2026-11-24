# core-creative-layout-engine

Assigns photos to layout slots and picks a crop for each one. `SuggestLayout` matches every photo to one slot so that aspect ratios fit as well as possible overall, and keeps an optional region of interest (for example a face) fully inside the crop. The module also has a helper to rotate detector regions into the photo's orientation and a small cache for analysis results. This is an engine module: it has no Compose dependency.

## When to use it

- You fill a collage or template with several photos and want each photo in the slot that suits its shape.
- You have subject regions from your own detector and want crops that never cut them off.
- For a ready-made "suggest and apply" panel, also add [core-creative-layout-compose](../creative-layout-compose/README.md).
- For fixed, predefined layouts with text, use [core-creative-templates-engine](../creative-templates-engine/README.md).

## Add it

```kotlin
implementation("noel.libs:core-creative-layout-engine:0.2.2")
```

It brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`. It is usually paired with an `EditSession` (`core-creative-history-engine`) to commit the chosen placements.

## Quick start

```kotlin
import noel.libs.creative.layout.domain.LayoutPhoto
import noel.libs.creative.layout.domain.Roi
import noel.libs.creative.layout.domain.Slot
import noel.libs.creative.layout.domain.SuggestLayout
import noel.libs.creative.layout.domain.orient
import noel.libs.creative.model.Outcome

// Normalized 0..1 region from your detector, rotated into the photo's orientation.
val face = orient(Roi(0.40, 0.10, 0.60, 0.35), clockwise = portrait.orientation, mirror = false)

val result =
    SuggestLayout()(
        photos = listOf(LayoutPhoto(portrait, roi = face), LayoutPhoto(landscape)),
        slots =
            listOf(
                Slot("left", 0.0, 0.0, 300.0, 600.0),
                Slot("right", 300.0, 0.0, 600.0, 600.0),
            ),
    )

when (result) {
    is Outcome.Success -> result.value.forEach { p -> println("${p.assetId} -> ${p.slot.id}") }
    is Outcome.Failure -> showError(result.error.message)
}
```

To apply a `Placement`, replace the image layer with the slot's position and size, and store the crop as `Content.Image(p.assetId, Crop(p.crop.left, p.crop.top, p.crop.right, p.crop.bottom))`. The sample does this with one `Edit.ReplaceLayer` commit.

## Main types

| Type | What it does |
|---|---|
| `SuggestLayout` | Returns `Outcome<List<Placement>>`, one placement per photo. |
| `LayoutPhoto` | An `Asset` plus an optional required `Roi`. |
| `Slot` | Slot `id`, `x`, `y`, `width`, `height` in document units. |
| `Placement` | `assetId`, the chosen `slot`, the `crop` as a normalized `Roi`, and its `cost`. |
| `Roi` | Normalized rectangle (`left`, `top`, `right`, `bottom` in 0..1). Throws on values outside that range. |
| `orient(roi, clockwise, mirror)` | Rotates a region by 0, 90, 180 or 270 degrees clockwise, then mirrors it horizontally if asked. |
| `Assignment` | `solve(cost)`: minimum-cost assignment for a square cost matrix (Hungarian method). |
| `AnalysisCache` | Thread-safe in-memory map from `AnalysisKey` to a list of `Roi`. |
| `AnalysisKey` | `accountId`, `assetId`, `revision`, `orientation`, `analyzerVersion`. |

All types are in `noel.libs.creative.layout.domain`.

## What your app provides

- The photo `Asset`s and the slot geometry.
- Subject detection, if you want it. The library contains no detector. Convert detector output to a `Roi` in the oriented image with `orient`.
- Cache keys and cleanup. Put a new `analyzerVersion` or content `revision` in the key when either changes, and call `purge(accountId, assetId)` when an asset is removed.
- The commit. `SuggestLayout` does not change the document.

All calls are synchronous; run large suggestion sets off the main thread.

## Limits and behaviour

- The number of photos must equal the number of slots, and must be at least one; otherwise `INVALID_INPUT` ("Provide one slot per photo").
- `Assignment.solve` accepts up to 128 rows. The matrix must be square with finite values below 1e12 in absolute size, or it throws `IllegalArgumentException`. Ties are broken by column order, so results are deterministic.
- Slots need positive width and height and non-negative `x` and `y`; otherwise `INVALID_INPUT` ("Invalid layout geometry"). More than 128 photos gives the same failure from `SuggestLayout`.
- The cost of a pair is the absolute log of photo aspect ratio divided by slot aspect ratio. Crops keep the slot's aspect ratio, use as much of the photo as possible, and are centred on the region of interest, or on the photo centre when there is none.
- If no assignment keeps every required region inside its crop, the result is `UNAVAILABLE` ("No layout preserves every required subject").
- Aspect ratios come from `Asset.width` and `Asset.height` as stored, while the renderer applies `Content.Image.crop` after rotating by `Asset.orientation`.
- `AnalysisCache` keeps 128 entries by default (`maxEntries`), drops the oldest inserted entry when full, and is memory only. `SuggestLayout` does not read it; pass regions in `LayoutPhoto`.

## Tests

`LayoutTest` covers: the global assignment beats a greedy choice, `orient` for a 90 degree rotation, an empty cost matrix, and rejection of a non-square matrix.

```bash
./gradlew :core-creative-layout-engine:testDebugUnitTest
```

## See also

- [core-creative-layout-compose](../creative-layout-compose/README.md) for the `LayoutSuggestions` panel.
- [core-creative-templates-engine](../creative-templates-engine/README.md) for recipe-based layouts.
- Guide: [Templates and layout](../../docs/creative-libraries.md#templates-and-layout).
- Sample: the "Layout" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
