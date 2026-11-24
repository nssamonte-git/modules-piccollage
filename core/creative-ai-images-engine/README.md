# core-creative-ai-images-engine

Runs AI image jobs (expand, replace, remove background, lettering) through a provider your app supplies, and keeps them safe to retry. Each job has a logical operation ID recorded in an `OperationStore`: a duplicate returns the earlier result, a timeout reconciles the existing remote job instead of paying for a new one, and a canceled job rejects late output. A finished image is only a proposal until the user accepts it with a revision-checked edit. This is an engine: no Compose, no network code.

## When to use it

- Your backend generates or edits images and you need idempotent submission, timeout recovery and an explicit accept step.
- For a ready-made prompt and accept panel, add [core-creative-ai-images-compose](../creative-ai-images-compose/README.md).
- For allowlisted layer edits proposed by a model, use [core-creative-ai-tools-engine](../creative-ai-tools-engine/README.md).

## Add it

```kotlin
implementation("noel.libs:core-creative-ai-images-engine:0.2.2")
```

Brings in through `api`: `core-creative-model` (which holds `OperationStore`, `MemoryOperationStore`, `EditSession` and `Outcome`) and `kotlinx-coroutines-core`. You also need an `EditSession`, usually `HistorySession` from `core-creative-history-engine`, and for restart recovery `FileOperationStore` from `core-creative-documents-engine`.

## Quick start

Based on the sample's "AI Images" tab:

```kotlin
import java.util.UUID
import noel.libs.creative.aiimages.data.AndroidGeneratedMediaValidator
import noel.libs.creative.aiimages.domain.*
import noel.libs.creative.model.*

val provider = object : ImageProvider {
    override suspend fun generate(job: ImageJob): Outcome<GeneratedImage> =
        backend.submit(job) // your transport; send job.operationId as the idempotency key
    override suspend fun reconcile(job: ImageJob): Outcome<GeneratedImage> =
        backend.lookup(job) // fetch the existing remote result, never resubmit
}
val runner = ImageJobRunner(journal, provider, AndroidGeneratedMediaValidator())

val asset = document.assets.first { it.id == (layer.content as Content.Image).assetId }
val job = ImageJob(
    operationId = UUID.randomUUID().toString(),
    accountId = "demo",
    documentId = document.id,
    revision = document.revision,
    assetId = asset.id,
    sourceRevision = asset.contentRevision,
    width = asset.width,
    height = asset.height,
    operation = "replace",
    prompt = prompt,
)
val result = runner.start(job) // suspend

// Later, when the user taps "Use result". Store the image bytes under its asset ID first.
runner.accept(job, session, pageId, layer.id)
```

## Main types

| Type | What it does |
|---|---|
| `ImageJobRunner(store, provider, validator, timeoutMs = 60_000, telemetry = null)` | `start(job)`, `cancel(job)`, `accept(job, session, pageId, layerId)`. |
| `ImageJob` | Operation identity, document revision, source asset and its `sourceRevision`, size, `operation`, `prompt`, `style`, optional mask size. |
| `GeneratedImage` | Returned `assetId`, `mediaType`, `width`, `height` and `bytes`. |
| `ImageProvider` | Your `generate(job)` and `reconcile(job)`. The default `reconcile` returns `UNAVAILABLE`. |
| `GeneratedMediaValidator` | Checks a returned image before it is kept. |
| `AndroidGeneratedMediaValidator(maxPixels = 16_000_000)` | Android implementation using `BitmapFactory`. |
| `LetteringHistory(capacity = 50)` / `LetteringEntry` | In-memory, account-scoped list of lettering text and style, with `fallbackText`. |

## What your app provides

- An `ImageProvider`. Provider keys, transport clients and prompts stay in your backend or data layer. The provider should treat `operationId` as a server idempotency key, and `reconcile` should look up the existing remote job.
- A `GeneratedMediaValidator`, usually `AndroidGeneratedMediaValidator`.
- An `OperationStore`. Use `FileOperationStore` to survive restarts; `MemoryOperationStore` is for tests and short-lived sessions. Implementations must serialize `atomic` blocks. Use one store instance per journal file.
- Storage for the returned bytes. `accept` only adds the asset record and points the layer at it, so your `AssetBytes` must already serve those bytes. Keep the original bytes for undo.
- Threading: `start` is `suspend`, but its journal calls run on the caller's thread. `cancel` and `accept` are synchronous. With a file journal, call them from a worker context.
- An optional `Telemetry`; it receives the operation ID and `"completed"`. Keep private prompts and media out of your traces.

## Limits and behaviour

- `start` rejects with `INVALID_INPUT`: blank operation or account ID, a non-positive size, more than 16,000,000 pixels, a prompt over 100,000 characters, an `operation` other than `expand`, `replace`, `remove-background` or `lettering`, or a mask size that differs from the job size. A `null` provider or validator gives `UNAVAILABLE`.
- The same operation ID with different input gives `CONFLICT`. A completed job returns a copy of its result without calling the provider. A job that is running, uncertain, or completed but not in memory (for example after a restart) calls `reconcile` instead of `generate`.
- A timeout or provider exception marks the job uncertain and returns `REMOTE` ("reconcile before retry"). If the calling coroutine is canceled, the job is marked uncertain and the cancellation is rethrown. A failure from `generate`, or a validation failure of its output, marks the job failed; failed and canceled jobs cannot be resubmitted with the same ID. The same failures after `reconcile` leave the job uncertain.
- `cancel` marks the job canceled and drops its result. A result that arrives later is rejected. It does not cancel anything remotely.
- `accept` needs a completed result, the same document and revision, an unlocked image layer still showing the source asset at `sourceRevision`, and an unused result asset ID. Otherwise it returns `STALE_REVISION`, `INVALID_INPUT`, `CONFLICT`, `CANCELED` or `UNAVAILABLE`. It commits the new asset and the layer change as one edit.
- `AndroidGeneratedMediaValidator` accepts PNG or JPEG up to 64 MiB whose decoded size and MIME type match the declared values. It does not compare the size with the job.
- Results are held in memory. Journals hold 4,096 operations. When the journal is full, `start` on a new operation returns `Outcome.Failure` with code `LIMIT` without calling the provider. A custom `OperationStore` signals full by throwing `IllegalArgumentException` from `put` for a new key.
- `LetteringHistory` keeps the newest `capacity` entries per account, text up to 1,000 characters. It is not persisted.

## Tests

`ImagesTest` covers: a duplicate start calls the provider once, wrong mask sizes are rejected, a missing provider fails, lettering fallback text, and a full journal returning `LIMIT` without calling the provider. `RecoveryTest` covers: a timeout followed by a retry reconciles without resubmitting, a canceled job rejects a late result, and lettering history is scoped per account.

```bash
./gradlew :core-creative-ai-images-engine:testDebugUnitTest
```

The sample's device tests check that `AndroidGeneratedMediaValidator` rejects corrupt bytes:

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-ai-images-compose](../creative-ai-images-compose/README.md)
- [core-creative-ai-tools-engine](../creative-ai-tools-engine/README.md)
- [core-creative-documents-engine](../creative-documents-engine/README.md)
- Guide: [AI and cloud boundaries](../../docs/creative-libraries.md#ai-and-cloud-boundaries)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` ("AI Images" tab), `DemoEnvironment.kt` (journal)
