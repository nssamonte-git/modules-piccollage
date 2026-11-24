# core-creative-history-engine

`HistorySession` owns the current `CreativeDocument` and its undo/redo history. Each `commit` applies a batch of edits as one transaction: the whole batch is validated before anything is published, the revision goes up by one, and the change becomes one undo step. Repeated transform edits of the same layer can be merged into one step. It is an engine module with no Compose.

## When to use it

- Any editor that changes a document. Other modules (canvas actions, `DuplicatePage`, `HistoryControls`, `CreativeCanvas`) take the `EditSession` interface, which `HistorySession` implements.
- For Undo and Redo buttons, add `core-creative-history-compose`.

## Add it

```kotlin
implementation("noel.libs:core-creative-history-engine:0.2.0")
```

Brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`. Usually paired with `core-creative-canvas-engine` and `core-creative-history-compose`.

## Quick start

```kotlin
import noel.libs.creative.history.domain.HistorySession
import noel.libs.creative.model.CreativeDocument
import noel.libs.creative.model.Edit
import noel.libs.creative.model.EditRequest
import noel.libs.creative.model.Outcome
import noel.libs.creative.model.Transform

fun moveTitle(document: CreativeDocument) {
    val session = HistorySession(document)
    val current = session.document.value
    val edit = Edit.TransformLayer("page-1", "title", Transform(x = 40.0, y = 15.0))
    when (val result = session.commit(EditRequest(current.revision, listOf(edit)))) {
        is Outcome.Success -> println("now at revision ${result.value.revision}")
        is Outcome.Failure -> println(result.error.code)
    }
    if (session.history.value.canUndo) session.undo()
}
```

In Compose, the sample keeps the session in state and observes it: `var session by remember { mutableStateOf<EditSession>(HistorySession(env.document())) }` and `session.document.collectAsState()`.

## Main types

| Type | What it does |
|---|---|
| `HistorySession(initial, maxEntries, maxBytes)` | Implements `EditSession`. Exposes `document` and `history` as `StateFlow`. |
| `EditSession` (model) | `commit(EditRequest)`, `undo()`, `redo()`, all returning `Outcome<CreativeDocument>`. |
| `EditRequest` (model) | `expectedRevision`, `edits`, optional `mergeKey`. |
| `HistoryState` (model) | `canUndo`, `canRedo`. |

## What your app provides

- One session per open document, created at your composition root. To open another document, create a new `HistorySession`.
- Calls are `@Synchronized` and run on the calling thread; they do no I/O.
- Code that starts asynchronous work must keep the revision it started from and pass it as `expectedRevision`.
- Saving: read `session.document.value` and pass it to your repository (for example `SaveDraft` from `core-creative-documents-engine`).

## Limits and behaviour

- If `expectedRevision` is not the current revision, `commit` returns `STALE_REVISION` and changes nothing.
- An invalid batch (validation failure, locked layer, missing target) returns `INVALID_INPUT` and publishes nothing, even if earlier edits in the batch were valid.
- A batch that leaves the document unchanged returns success with the same revision and no history entry.
- Undo and redo also increase the revision; it never goes back. With nothing to undo or redo they return `UNAVAILABLE`.
- A new commit clears the redo stack.
- Merging: two consecutive commits merge into one undo step when both have the same non-null `mergeKey`, each contains a single `Edit.TransformLayer` for the same page and layer, and there is nothing to redo.
- History keeps at most `maxEntries` (default 200) steps and an estimated `maxBytes` (default 16 MiB); the oldest steps are dropped first. A single change larger than the budget is committed but cannot be undone.
- `initial` is validated in the constructor, which throws `IllegalArgumentException` if it is invalid.

## Tests

`HistoryTest` covers undo, redo and the cleared redo stack after a new commit; a failed batch and a stale revision leaving the document unchanged; and two merged drag commits undone in one step.

```bash
./gradlew :core-creative-history-engine:testDebugUnitTest
```

## See also

- [core-creative-history-compose](../creative-history-compose/README.md), [core-creative-model](../creative-model/README.md), [core-creative-canvas-engine](../creative-canvas-engine/README.md)
- Guide: [Editing and composition](../../docs/creative-libraries.md#editing-and-composition)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` (creates and replaces the `HistorySession`)
