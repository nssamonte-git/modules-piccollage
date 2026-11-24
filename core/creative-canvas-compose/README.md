# core-creative-canvas-compose

`CreativeCanvas`, a Compose canvas that shows one page of the session's document and lets the user select a layer by tapping and move, scale or rotate it with one or two fingers. The page is drawn by a `PageRenderer`, and each gesture becomes one commit (one undo step) when the fingers lift. It is a Compose UI module.

## When to use it

- You want a working editor canvas on top of `HistorySession` and `DocumentRenderer` with no gesture code of your own.
- For a custom canvas, use `core-creative-canvas-engine` (`HitTest`, `CanvasGesture`) and `core-creative-renderer` directly.

## Add it

```kotlin
implementation("noel.libs:core-creative-canvas-compose:0.2.0")
```

Brings in `core-creative-canvas-engine`, `core-creative-renderer`, `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation-layout (through the Compose BOM) via `api`. Usually paired with `core-creative-history-engine` and `core-creative-history-compose`.

## Quick start

As wired in the sample:

```kotlin
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noel.libs.creative.canvas.presentation.CreativeCanvas
import noel.libs.creative.model.EditSession
import noel.libs.creative.renderer.api.PageRenderer

@Composable
fun Editor(session: EditSession, renderer: PageRenderer) {
    val document by session.document.collectAsState()
    val page = document.pages.first()
    var selected by remember { mutableStateOf<String?>(null) }
    CreativeCanvas(
        session = session,
        renderer = renderer,
        pageId = page.id,
        selectedId = selected,
        onSelect = { selected = it },
        modifier = Modifier.padding(16.dp),
    )
}
```

The sample passes `DocumentRenderer(assets, GpuImageEffects(), AndroidMask())` as the renderer.

## Main types

| Type | What it does |
|---|---|
| `CreativeCanvas(session, renderer, pageId, selectedId, onSelect, modifier)` | Renders the page, handles tap-to-select and drag/pinch/rotate, and draws a blue outline around the selected layer. |

## What your app provides

- The `EditSession` and `PageRenderer`. The renderer runs on `Dispatchers.Default`; the canvas recycles each bitmap when it is replaced.
- Selection state: `selectedId` in, `onSelect` out. A tap on empty space calls `onSelect(null)`.
- Status and error display. Commit results from gestures are not reported back.

## Limits and behaviour

- The canvas fills the available width and keeps the page's aspect ratio. If `pageId` is not in the document, nothing is drawn.
- The page is rendered at its full document size, and again for every preview change during a gesture. Large pages cost more on slower devices.
- If rendering fails, the canvas shows no page image; the renderer's warnings are not shown.
- Only unlocked, visible layers can be picked (see `HitTest`). The layer under the first touch is the one transformed, around that touch point.
- The preview is local to the gesture. A canceled gesture commits nothing.
- The accumulated pinch scale is clamped with `CanvasGesture.clampScale` to 0.01..100 times the size at gesture start, so an extreme pinch stops at the limit instead of failing.
- The outline color (`0xff1565c0`) and width are fixed.

## Tests

This module has no unit tests. The sample's device test `sampleActivityLaunches` starts `MainActivity`, which shows `CreativeCanvas`; it only checks that the activity launches.

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-canvas-engine](../creative-canvas-engine/README.md), [core-creative-renderer](../creative-renderer/README.md), [core-creative-history-compose](../creative-history-compose/README.md)
- Guide: [Editing and composition](../../docs/creative-libraries.md#editing-and-composition)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt`
