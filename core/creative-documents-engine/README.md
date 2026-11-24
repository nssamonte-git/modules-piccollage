# core-creative-documents-engine

Local drafts and project-level policy for creative documents. `FileDocumentRepository` saves each draft, with copies of its asset bytes, into a new revision directory and then switches a pointer file atomically, so an interrupted save never replaces the last good draft. It also has a bounded binary codec, a durable operation journal, page duplication, and small use cases that call host adapters for backup, deletion, reminders and purchases. It is an engine module with no Compose.

## When to use it

- Saving and reopening drafts in app-private storage, per account.
- Duplicating a page inside an `EditSession`.
- A file-backed `OperationStore` (`FileOperationStore`) that survives a restart, for example for `core-creative-ai-images-engine`.
- For a ready-made project list, add `core-creative-documents-compose`.

## Add it

```kotlin
implementation("noel.libs:core-creative-documents-engine:0.2.1")
```

Brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`. Usually paired with `core-creative-history-engine`.

## Quick start

Based on the sample's `DemoEnvironment`:

```kotlin
import android.content.Context
import java.io.File
import noel.libs.creative.documents.data.FileDocumentRepository
import noel.libs.creative.documents.data.FileOperationStore
import noel.libs.creative.documents.domain.OpenDraft
import noel.libs.creative.documents.domain.SaveDraft
import noel.libs.creative.model.AssetBytes
import noel.libs.creative.model.CreativeDocument
import noel.libs.creative.model.Outcome

class Drafts(context: Context, assets: AssetBytes) {
    val repository = FileDocumentRepository(File(context.filesDir, "drafts"), assets)
    val journal = FileOperationStore(File(context.filesDir, "operations.bin")) // reads the file now

    suspend fun save(document: CreativeDocument) = SaveDraft(repository)("account-1", document)

    suspend fun reopen(id: String): CreativeDocument? {
        val opened = OpenDraft(repository)("account-1", id)
        if (opened !is Outcome.Success) return null
        for (asset in opened.value.assets) {
            val bytes = repository.readAsset("account-1", id, asset.id)
            // if Outcome.Success, store bytes.value where your AssetBytes reads it
        }
        return opened.value
    }
}
```

## Main types

| Type | What it does |
|---|---|
| `FileDocumentRepository(root, assets, codec, fault)` | `DocumentRepository` on files. `save`, `load`, `readAsset`. |
| `DocumentCodec` | `encode` / `decode` of the binary format (v1; reads v0). Throws `DocumentCodec.FutureVersion` for newer versions. |
| `FileOperationStore(file)` | Durable `OperationStore`; every `put` rewrites the file and moves it into place atomically. |
| `SaveDraft`, `OpenDraft` | Use cases over a `DocumentRepository`. |
| `DuplicatePage` | Commits a copy of a page (new page and layer IDs) at the end of the document. |
| `BackupDraft`, `RestorePurchases`, `RemindUnfinishedProject` | Call the host `BackupPort`, `PaidContentGateway` or `ReminderPort`; return `UNAVAILABLE` when it is `null`. |
| `DeleteProject`, `DeletionScope`, `LocalDraftDeletion` | Deletes `LOCAL`, `REMOTE` or `BOTH`. |
| `Album`, `AlbumMembership` | Adds a document to an album only for the album's account. |
| `MediaEligibility`, `UploadPreflight`, `UploadPermit` | Checks a media selection against type and size limits, and an upload permit against account, asset, revision and expiry. |

## What your app provides

- An app-private `root` directory and an `AssetBytes` that can return every asset in the document at save time.
- After `load`, reload asset bytes with `readAsset` into whatever your `AssetBytes` reads.
- A list of saved projects. The repository has no listing or delete method; implement `LocalDraftDeletion` yourself for `DeleteProject`.
- Adapters for backup, remote deletion, reminders and purchases. No scheduler, billing SDK or backend is included.
- Distinct account IDs, and one `FileOperationStore` instance per journal file. Its constructor and `put` do blocking file I/O; call them off the main thread. `save`, `load` and `readAsset` switch to `Dispatchers.IO` themselves.

## Limits and behaviour

- Account and document IDs are hashed (SHA-256) into directory names, and asset IDs into file names. Another account cannot load the draft.
- `save` returns `STALE_REVISION` if a newer revision is stored, `MISSING_ASSET` if an asset cannot be read, `UNSUPPORTED` if the stored draft is from a future version (it is not overwritten), and `IO` for other errors. `load` returns `UNAVAILABLE` when there is no draft.
- Old revision directories are never deleted.
- Size bounds: encoded document 16 MiB, each string 1,000,000 UTF-8 bytes, each asset file 64 MiB. Decoding accepts at most 10,000 strokes per drawing layer and 100,000 text styles per text layer; `CreativeDocument.validate()` enforces the same limits, so any valid document can be saved and reopened.
- Journals hold at most 4096 records and throw when full.
- `MediaEligibility` defaults: 100 items, 16,000,000 pixels each; failure code `LIMIT`.
- `DeleteProject` with `BOTH` deletes remotely first and stops if that fails.

## Tests

`DocumentTest`: codec round trip (including an emoji family), a drawing at the 10,000-stroke limit saving and reopening, future-version detection, truncated input, an interrupted save that keeps the last published draft and is not readable by another account, account-scoped album membership, and v0 migration. `JournalTest`: a journal record survives reopening the file.

```bash
./gradlew :core-creative-documents-engine:testDebugUnitTest
```

## See also

- [core-creative-documents-compose](../creative-documents-compose/README.md), [core-creative-history-engine](../creative-history-engine/README.md), [core-creative-model](../creative-model/README.md)
- Guide: [Persistence and recovery](../../docs/creative-libraries.md#persistence-and-recovery), [AI and cloud boundaries](../../docs/creative-libraries.md#ai-and-cloud-boundaries)
- Sample: `sample/src/main/kotlin/noel/libs/sample/DemoEnvironment.kt` and the "Projects" tab in `MainActivity.kt`
