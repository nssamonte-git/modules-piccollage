# core-creative-ai-tools-engine

Runs editor tool calls proposed by an AI model, safely. Only three tools exist (`move`, `setText`, `deleteLayer`); each call is checked for exact arguments and the expected document revision, then committed to your `EditSession`. Every call has a logical operation ID recorded in an `OperationStore`, so a repeated call is replayed instead of applied twice. This is an engine: no Compose, no network code.

## When to use it

- An assistant suggests edits and you want to apply only allowlisted, revision-checked operations.
- You need duplicate delivery (retries, reconnects) to be harmless.
- For a ready-made "suggested edit" panel, add [core-creative-ai-tools-compose](../creative-ai-tools-compose/README.md).
- For AI image generation, use [core-creative-ai-images-engine](../creative-ai-images-engine/README.md).

## Add it

```kotlin
implementation("noel.libs:core-creative-ai-tools-engine:0.2.0")
```

Brings in through `api`: `core-creative-model` (which holds `OperationStore`, `MemoryOperationStore`, `EditSession` and `Outcome`) and `kotlinx-coroutines-core`. You also need an `EditSession`, usually `HistorySession` from `core-creative-history-engine`, and for a durable journal `FileOperationStore` from `core-creative-documents-engine`.

## Quick start

```kotlin
import java.io.File
import java.util.UUID
import noel.libs.creative.aitools.domain.ToolCall
import noel.libs.creative.aitools.domain.ToolRunner
import noel.libs.creative.documents.data.FileOperationStore
import noel.libs.creative.model.Outcome

val journal = FileOperationStore(File(context.filesDir, "operations.bin"))
val runner = ToolRunner(journal)

val document = session.document.value
val call = ToolCall(
    callId = UUID.randomUUID().toString(),      // correlates with the model's tool result
    operationId = UUID.randomUUID().toString(), // logical identity; reuse it on retry
    accountId = "demo",
    documentId = document.id,
    revision = document.revision,
    name = "move",
    arguments = mapOf("pageId" to pageId, "layerId" to layerId, "x" to "40", "y" to "100"),
)

// Synchronous: run on a worker thread when the journal is file-backed.
when (val result = runner.execute(call, session)) {
    is Outcome.Success -> result.value.documentRevision // replayed = true for a duplicate
    is Outcome.Failure -> result.error.code
}
```

## Main types

| Type | What it does |
|---|---|
| `ToolRunner(store, maxCalls = 16, telemetry = null)` | `execute(call, session)`, `executeRun(calls, session)`, `cancel(accountId, documentId, operationId)`. |
| `ToolCall` | Call and operation IDs, account, document, expected revision, tool name and string arguments. |
| `ToolResult` | `callId`, `operationId`, the resulting `documentRevision`, and `replayed`. |
| `OperationStore` (model) | The journal interface: `atomic`, `get`, `put`. |
| `MemoryOperationStore` (model) | In-memory journal for tests and short-lived sessions. |

## What your app provides

- An `EditSession` holding the document.
- An `OperationStore`. Implementations must serialize `atomic` blocks across every caller that shares the store. Use one store instance per journal file.
- The model, prompts, provider keys and transport. This module never calls a provider; you turn the model's output into `ToolCall` values.
- Threading: `execute`, `executeRun` and `cancel` are synchronous and run inside `store.atomic`. A file journal writes and syncs on every `put`, so call them off the main thread.
- An optional `Telemetry`; it receives the operation ID and `"completed"`. Its exceptions are ignored.

## Limits and behaviour

- Tools and their exact argument keys: `move` (`pageId`, `layerId`, `x`, `y`; sets position only), `setText` (`pageId`, `layerId`, `text`; text layers only, clears text styles) and `deleteLayer` (`pageId`, `layerId`). Locked layers are rejected by the edit. Each value is at most 100,000 characters.
- Failures: blank IDs or a different document give `INVALID_INPUT`; an unknown tool gives `UNSUPPORTED`; wrong keys or unparseable values give `INVALID_INPUT`; a revision mismatch gives `STALE_REVISION` ("Request a fresh proposal"). These are rejected before anything is journaled.
- An operation ID reused with different input gives `CONFLICT`. A repeat of a completed operation returns its stored revision with `replayed = true`. A repeat of a running, failed, canceled or uncertain operation gives `CONFLICT`.
- Each call is its own commit, so the next call in a run usually needs the next revision. `executeRun` rejects more than `maxCalls` calls with `LIMIT`, checks each call against the revision current at its turn, and stops at the first failure; earlier calls stay committed and undoable.
- `cancel` marks an unfinished operation canceled. It cannot interrupt a call that is already running.
- `MemoryOperationStore` (by default) and `FileOperationStore` hold 4,096 operations. When the journal is full, `execute` returns `Outcome.Failure` with code `LIMIT` ("Operation journal full") before editing anything; operations already in the journal still replay. A custom `OperationStore` signals full by throwing `IllegalArgumentException` from `put` for a new key.

## Tests

`ToolsTest` covers: a duplicate call does not mutate twice, reusing an operation ID with different arguments fails, unknown tools and extra arguments fail, a stale revision fails, and a full journal returns `LIMIT` without editing.

```bash
./gradlew :core-creative-ai-tools-engine:testDebugUnitTest
```

## See also

- [core-creative-ai-tools-compose](../creative-ai-tools-compose/README.md)
- [core-creative-ai-images-engine](../creative-ai-images-engine/README.md)
- [core-creative-history-engine](../creative-history-engine/README.md)
- [core-creative-documents-engine](../creative-documents-engine/README.md)
- Guide: [AI and cloud boundaries](../../docs/creative-libraries.md#ai-and-cloud-boundaries)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` ("AI Tools" tab), `DemoEnvironment.kt` (journal)
