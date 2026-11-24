# core-creative-documents-compose

`DocumentBrowser`, a simple project panel: a "Save current project" button, one button per saved document, and a status line. It does no I/O; the host passes the document list and handles save and open. It is a Compose UI module.

## When to use it

- You want a basic save/open screen while wiring `core-creative-documents-engine`.
- For your own project gallery (thumbnails, sorting, delete), use the engine alone and build the UI yourself.

## Add it

```kotlin
implementation("noel.libs:core-creative-documents-compose:0.2.1")
```

Brings in `core-creative-documents-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation-layout (through the Compose BOM) via `api`. `core-creative-compose-common` is used internally (`implementation`).

## Quick start

Shaped like the sample's "Projects" tab:

```kotlin
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import noel.libs.creative.documents.domain.OpenDraft
import noel.libs.creative.documents.domain.SaveDraft
import noel.libs.creative.documents.presentation.DocumentBrowser
import noel.libs.creative.model.CreativeDocument
import noel.libs.creative.model.DocumentRepository
import noel.libs.creative.model.Outcome

@Composable
fun Projects(
    repository: DocumentRepository,
    current: CreativeDocument,
    onOpened: (CreativeDocument) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var saved by remember { mutableStateOf<List<CreativeDocument>>(emptyList()) }
    var busy by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    DocumentBrowser(
        documents = saved,
        onOpen = { id ->
            scope.launch {
                busy = true
                when (val opened = OpenDraft(repository)("account-1", id)) {
                    is Outcome.Success -> onOpened(opened.value)
                    is Outcome.Failure -> status = opened.error.message
                }
                busy = false
            }
        },
        onSave = {
            scope.launch {
                busy = true
                val result = SaveDraft(repository)("account-1", current)
                status = if (result is Outcome.Success) "Saved" else "Save failed"
                if (result is Outcome.Success) saved = listOf(current)
                busy = false
            }
        },
        busy = busy,
        status = status,
    )
}
```

In `onOpened`, create a new `HistorySession` for the opened document, as the sample does.

## Main types

| Type | What it does |
|---|---|
| `DocumentBrowser(documents, onOpen, onSave, modifier, busy, status)` | A `CreativePanel` titled "Projects". `onOpen` receives the document ID. `busy` disables all buttons. Each entry shows title, page count and revision. |

## What your app provides

- The list of saved documents. The repository has no listing method, so keep track of what you have saved (the sample keeps a list in state).
- The save and open work in `onSave` / `onOpen`, run in a coroutine, and the account ID.
- The `status` text and a `MaterialTheme`.

## Limits and behaviour

- The strings "Projects", "Save current project" and "No saved projects" are fixed English.
- All documents are laid out in a `Column` without scrolling or lazy loading; keep the list short or wrap it in a scrolling container.

## Tests

This module has no unit tests, and the sample's device tests do not open the Projects tab.

```bash
./gradlew :core-creative-documents-compose:testDebugUnitTest
```

## See also

- [core-creative-documents-engine](../creative-documents-engine/README.md), [core-creative-compose-common](../creative-compose-common/README.md), [core-creative-history-engine](../creative-history-engine/README.md)
- Guide: [Persistence and recovery](../../docs/creative-libraries.md#persistence-and-recovery)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` (the "Projects" tab)
