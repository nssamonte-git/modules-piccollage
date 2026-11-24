# core-creative-model

The shared data model and contracts for the creative modules: the `CreativeDocument` tree (pages, layers, assets), the `Edit` commands that change it, the `Outcome` result type, and the small interfaces the host app implements (asset bytes, storage, backup, reminders, entitlements, telemetry, operation journal). It is an engine module with no Compose; the code is plain Kotlin plus kotlinx-coroutines `StateFlow`.

## When to use it

- You never add it alone in practice: every other creative module exposes it through `api`, so it arrives with any of them.
- Depend on it directly when you write your own engine, adapter or UI against the document model, or implement a host port such as `AssetBytes` or `DocumentRepository` in a separate module.
- For undo/redo use `core-creative-history-engine` (it implements `EditSession`). For drawing pages use `core-creative-renderer`.

## Add it

```kotlin
implementation("noel.libs:core-creative-model:0.2.0")
```

Brings in `org.jetbrains.kotlinx:kotlinx-coroutines-core` through `api`. Nothing else.

## Quick start

```kotlin
import noel.libs.creative.model.Content
import noel.libs.creative.model.CreativeDocument
import noel.libs.creative.model.Edit
import noel.libs.creative.model.Layer
import noel.libs.creative.model.Page
import noel.libs.creative.model.TextFormat
import noel.libs.creative.model.Transform
import noel.libs.creative.model.applyEdits

val title =
    Layer("title", Transform(24.0, 15.0), 590.0, 60.0, Content.Text("Hello", TextFormat(size = 36f)))
val document =
    CreativeDocument(
        "project-1",
        pages = listOf(Page("page-1", 640, 480, layers = listOf(title))),
        title = "My project",
    )
document.validate() // throws IllegalArgumentException if a limit is broken

// Pure function: returns a new validated document, or throws IllegalArgumentException.
val moved = applyEdits(document, listOf(Edit.TransformLayer("page-1", "title", Transform(40.0, 15.0))))
```

`applyEdits` does not change `revision`. In an app, send edits through an `EditSession` (`HistorySession`) with `commit(EditRequest(expectedRevision, edits))`, which bumps the revision and returns an `Outcome`.

## Main types

| Type | What it does |
|---|---|
| `CreativeDocument` | Document with stable IDs, `revision`, `pages`, `assets`, `title`. `validate()` and `immutableCopy()`. |
| `Page`, `Layer` | A page (size in document units, ARGB `background`) and its layers, drawn in list order (last is on top). |
| `Content` | `Image(assetId, crop)`, `Text(text, format, styles)` or `Drawing(strokes)`. |
| `Transform`, `Point` | Position, scale and rotation (degrees). `map` and `inverse` convert between layer and page coordinates. |
| `Crop`, `TextFormat`, `TextStyle`, `Stroke`, `Effect`, `Mask`, `MaskStroke`, `Asset` | Layer details. Colors are ARGB `Int`. |
| `Edit`, `EditRequest` | Edit commands (add, replace, transform, remove, reorder layer; add, remove page; add asset) and a batch with the expected revision and an optional `mergeKey`. |
| `EditSession`, `HistoryState` | Interface for a document owner with `document`, `history`, `commit`, `undo`, `redo`. |
| `Outcome`, `CreativeError`, `failure()` | Success or typed failure (`CreativeError.Code`). |
| `DocumentRepository`, `AssetBytes` | Storage and asset-byte ports. |
| `Entitlements`, `BackupPort`, `ReminderPort`, `Telemetry`, `Usage` | Host ports used by other modules. |
| `OperationStore`, `OperationRecord`, `OperationState`, `MemoryOperationStore` | Operation journal contract and an in-memory version. |
| `fingerprint()`, `operationKey()` | SHA-256 identity helpers for operations. |
| `MessagePart` | Text, image, file, tool-result and unknown message envelopes. |

## What your app provides

Implementations of the ports you use: `AssetBytes` (return a copy of the bytes for an asset ID), and optionally `DocumentRepository`, `BackupPort`, `ReminderPort`, `Entitlements`, `Telemetry`. An `OperationStore` must serialize `atomic` blocks for every caller that shares its store. `MemoryOperationStore` keeps up to 4096 records (constructor `maxEntries`) and throws when full.

## Limits and behaviour

`validate()` throws `IllegalArgumentException` unless: `schemaVersion == 1`; 1 to 256 pages with unique IDs; up to 4096 assets with unique IDs, each at most 64,000,000 pixels; layer IDs unique across the whole document; each page at most 16,000,000 pixels and 1024 layers; transforms finite with non-zero scale; opacity in 0..1; up to 32 effects per layer; mask strokes up to 10,000, each width in (0, 8192] with up to 100,000 points; drawing layers up to 10,000 strokes, each up to 100,000 points with pressures empty or one per point in 0..1; image layers point at an existing asset; text up to 100,000 characters and 100,000 styles, with size 1..4096. The stroke and style counts match what the draft format in `core-creative-documents-engine` can read back.

`applyEdits` accepts at most 256 edits. Replace, transform, remove and reorder reject locked layers. `Crop` throws at construction unless its edges are finite, inside 0..1 and `right > left`, `bottom > top`.

## Tests

`ValidationTest` checks that a locked layer cannot be replaced that a mask with a NaN point and negative width fails validation, and that drawing stroke and text style counts above the draft format's limits are rejected.

```bash
./gradlew :core-creative-model:testDebugUnitTest
```

## See also

- [core-creative-history-engine](../creative-history-engine/README.md), [core-creative-renderer](../creative-renderer/README.md), [core-creative-documents-engine](../creative-documents-engine/README.md), [core-creative-canvas-engine](../creative-canvas-engine/README.md)
- Guide: [Editing and composition](../../docs/creative-libraries.md#editing-and-composition)
- Sample: `sample/src/main/kotlin/noel/libs/sample/DemoEnvironment.kt` (`document()` and the `AssetBytes` object)
