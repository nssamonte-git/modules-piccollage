# core-creative-export-compose

A small Compose panel for export: PNG, JPEG and MP4 buttons, a progress bar with Cancel while running, and a Share button when an export is ready. It only draws an `ExportState` and calls your callbacks; the export work is done by [core-creative-export-engine](../creative-export-engine/README.md). This is a Compose UI module.

## When to use it

- You want working export controls quickly and are happy with Material 3 defaults.
- Write your own UI against `ExportState` if you need other formats, labels or layout. The engine alone has no Compose dependency.

## Add it

```kotlin
implementation("noel.libs:core-creative-export-compose:0.2.2")
```

Brings in through `api`: `core-creative-export-engine` (and with it the model, renderer and Media3 Transformer), `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation layout from the Compose BOM. Material 3 and `core-creative-compose-common` are internal.

## Quick start

Based on the sample's "Export" tab. The host keeps its own `ExportState` for stills, because `StillExporter` does not publish state, and shows `VideoExporter.state` while a video runs.

```kotlin
import androidx.compose.runtime.*
import noel.libs.creative.export.domain.ExportState
import noel.libs.creative.export.presentation.ExportControls

var exportState by remember { mutableStateOf<ExportState>(ExportState.Idle) }
val videoState by videoExporter.state.collectAsState()
val shown = if (exportJob?.isActive == true && videoState is ExportState.Running) videoState else exportState

ExportControls(
    state = shown,
    onPng = { startStill(png = true) },
    onJpeg = { startStill(png = false) },
    onMp4 = { startVideo() },
    onCancel = { exportJob?.cancel() },
    onShare = { shareLastOutput() },
)
```

`startStill`, `startVideo` and `shareLastOutput` are your functions that call `StillExporter`, `VideoExporter` and `AndroidDelivery` in a coroutine scope and set `exportState` from the result.

## Main types

| Type | What it does |
|---|---|
| `ExportControls(state, onPng, onJpeg, onMp4, onCancel, onShare, modifier)` | Export panel driven by an `ExportState`. |

## What your app provides

- The `ExportState` to show, the export coroutines, and cancellation (cancel the job running the export).
- The output file, its FileProvider URI and the share call. See the engine README for the FileProvider setup.
- A `MaterialTheme` around the panel.

## Limits and behaviour

- The format buttons are disabled while the state is `Running`. `Running` shows a progress bar (`progress / 100`) and Cancel; `Completed` shows "Export ready" and Share; `Failed` shows its message; `Canceled` and `Idle` show a short line of text.
- Labels are fixed English strings.
- There is no print button. The sample adds its own that calls `AndroidDelivery().printImage(activity, bitmap)`.

## Tests

This module has no unit tests.

## See also

- [core-creative-export-engine](../creative-export-engine/README.md)
- Guide: [Still and video output](../../docs/creative-libraries.md#still-and-video-output)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` ("Export" tab)
