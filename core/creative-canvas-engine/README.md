# core-creative-canvas-engine

Canvas logic without UI: find the layer under a point (`HitTest`), track a drag or pinch as a preview that is committed once at the end (`CanvasGesture`), and one-call actions to move, reorder or add a drawing layer through an `EditSession`. It is an engine module with no Compose.

## When to use it

- You build your own canvas or gesture handling and need hit testing and preview-then-commit gestures that keep history clean.
- You want buttons or accessibility actions that move or reorder a layer, or add a drawing.
- For a ready-made Compose canvas, add `core-creative-canvas-compose`, which uses these types.

## Add it

```kotlin
implementation("noel.libs:core-creative-canvas-engine:0.2.2")
```

Brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`. Usually paired with `core-creative-history-engine`, which provides the `EditSession`.

## Quick start

```kotlin
import noel.libs.creative.canvas.domain.AddDrawing
import noel.libs.creative.canvas.domain.CanvasGesture
import noel.libs.creative.canvas.domain.HitTest
import noel.libs.creative.canvas.domain.MoveLayer
import noel.libs.creative.model.EditSession
import noel.libs.creative.model.Point
import noel.libs.creative.model.Stroke

fun demo(session: EditSession, pageId: String) {
    val document = session.document.value
    val page = document.pages.first { it.id == pageId }

    // Drag: preview while moving, commit once at the end.
    val layer = HitTest()(page, Point(100.0, 120.0)) ?: return
    val gesture = CanvasGesture(pageId, layer, document.revision)
    gesture.moveBy(30.0, 0.0) // draw gesture.preview while the finger moves
    gesture.finish()?.let { session.commit(it) }

    // Button actions, as in the sample's Editor tab.
    MoveLayer()(session, pageId, layer.id, 10.0, 0.0)
    AddDrawing()(
        session,
        pageId,
        "drawing-1",
        listOf(Stroke(listOf(Point(100.0, 180.0), Point(200.0, 220.0)), 8.0, 0xffca603f.toInt())),
    )
}
```

## Main types

| Type | What it does |
|---|---|
| `HitTest` | `invoke(page, point, tolerance)` returns the topmost visible, unlocked layer with opacity above 0 that contains the point, or `null`. |
| `CanvasGesture(pageId, original, revision)` | Holds `preview`. `moveBy`, `transformBy(dx, dy, scale, rotation, pivot)`, `cancel`, `finish`. |
| `MoveLayer` | Commits a translation by `dx`, `dy`. |
| `ReorderLayer` | Commits `Edit.ReorderLayer` to a new index in the page's layer list. |
| `AddDrawing` | Commits a new `Content.Drawing` layer the size of the page. |

## What your app provides

- An `EditSession` (normally `HistorySession`). All actions read `session.document.value` and commit synchronously on the calling thread.
- Conversion from screen pixels to page coordinates. Points are in document units with the origin at the top left of the page; viewport scaling is up to you.
- Drawing of the preview while a gesture runs (for example by rendering a copy of the document with `layer.copy(transform = gesture.preview)`).

## Limits and behaviour

- `finish()` returns `null` after `cancel()` or when the transform did not change; otherwise an `EditRequest` with the revision the gesture started from. If the document changed meanwhile, the commit fails with `STALE_REVISION`.
- `finish()` does not set a `mergeKey`, so each gesture is one undo step.
- `transformBy` requires `scale` in `CanvasGesture.MIN_SCALE..MAX_SCALE` (0.01..100) and `moveBy` requires finite values; both throw `IllegalArgumentException` otherwise. When scale comes from accumulated pinch events, pass it through `CanvasGesture.clampScale` first (as `CreativeCanvas` does); it also maps NaN to 1. Scale and rotation apply to the gesture's starting transform; with a `pivot`, the layer point that was under `pivot` ends up at `pivot` plus (`dx`, `dy`).
- `AddDrawing` returns `INVALID_INPUT` for an empty stroke list, an empty stroke, more than 100,000 points or a width of 0 or less, and for an unknown page. `MoveLayer` returns `INVALID_INPUT` for an unknown layer.
- Locked layers are skipped by `HitTest` and rejected by the session for move and reorder.

## Tests

`CanvasTest` covers a rotated and scaled `Transform` mapping and inverting a point, `HitTest` choosing the front unlocked layer, a canceled gesture producing no edit, and an extreme pinch being clamped to the accepted scale range.

```bash
./gradlew :core-creative-canvas-engine:testDebugUnitTest
```

## See also

- [core-creative-canvas-compose](../creative-canvas-compose/README.md), [core-creative-history-engine](../creative-history-engine/README.md), [core-creative-model](../creative-model/README.md)
- Guide: [Editing and composition](../../docs/creative-libraries.md#editing-and-composition)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` (the "Editor" tab buttons)
