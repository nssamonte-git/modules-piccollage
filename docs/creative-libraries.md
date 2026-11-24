# Creative libraries

## Adopt individual capabilities

Use `includeBuild` or Maven as described in the root README. Add only the capabilities needed by an app. An engine has no transitive Compose dependency; its optional `-compose` artifact adds the components. All new modules use the existing Android convention (minSdk 26, compileSdk 37, Java 11); domains are framework-free Kotlin inside AARs.

```kotlin
implementation("noel.libs:core-creative-history-engine:0.2.1")
implementation("noel.libs:core-creative-canvas-compose:0.2.1")
implementation("noel.libs:core-creative-renderer:0.2.1")
```

## Editing and composition

`CreativeDocument` contains stable page/layer/asset IDs, a schema version and a monotonic edit revision. `Content` represents images with normalized source crops, semantic text, or stored drawing strokes. Asset identity is separate from bytes and filesystem paths.

Construct `HistorySession(document)` at the host composition root. Call domain use cases or `session.commit(EditRequest(expectedRevision, edits))`; inspect `Outcome.Success` / `Outcome.Failure`. A complete batch is validated before publication. Undo and redo also increment revisions; asynchronous work must retain the revision it started from. Default history retention is 200 entries and a 16 MiB estimated payload budget. A command larger than the retention budget can commit without a retained undo entry.

`CanvasGesture` keeps previews independent of history. Commit only `finish()` at gesture end; cancel restores the baseline. Canvas coordinates are top-left document units. Layer mapping scales, rotates, then translates; viewport scaling is separate. `CreativeCanvas` accepts `EditSession`, the shared `PageRenderer` API, selection and callbacks. `MoveLayer`, `ReorderLayer` and `AddDrawing` also support accessible controls.

The sample app demonstrates all eleven capabilities with original generated media, manual wiring, and local AI provider modes. Its provider is a deterministic fixture; it performs no remote generation.

## Persistence and recovery

`FileDocumentRepository(privateRoot, AssetBytes)` saves immutable asset copies and a versioned binary document into a new revision directory, flushes files, then atomically publishes the revision pointer. Incomplete directories never replace the last published draft. Asset IDs/account/project identities are hashed for path construction; the root must be app-private. Assets remain available when another page references them. Revision directories are retained; host-controlled cleanup is intentionally conservative.

Schema v1 preserves IDs, pages, crops, effects, masks, paths and text. The decoder migrates the defined v0 format (no title or crop fields) and rejects future versions. A future-version draft cannot be overwritten by this writer. A missing asset during rendering produces an actionable placeholder; strict export rejects missing content.

`SaveDraft`, `OpenDraft`, `DuplicatePage`, `AlbumMembership`, `MediaEligibility`, and `UploadPreflight` provide local policy. Cloud backup/deletion, reminders, purchase restoration and entitlement checks are explicit host contracts. Library backup or deletion actions do not imply a scheduler, billing SDK or deployed backend. Use distinct account IDs and one operation-store instance per journal file.

## Rendering, masks and text

`DocumentRenderer(assets, effects, masks, fonts)` implements the shared `PageRenderer` API. Rendering is synchronous: run it on a worker thread. The caller owns and recycles each returned bitmap. Pass `GpuImageEffects { backend -> ... }` and `AndroidMask()` for accelerated effects and masking. GPU failure reports `CPU fallback`; saved effect parameters remain identical. The CPU path is also usable directly for reference checks. Effects do no colour-space conversion: pixels are processed as decoded (Android bitmaps are normally sRGB), with straight-alpha domain pixels and premultiplied GPU boundaries.

Only built-in brightness, contrast, saturation, grayscale and bounded box blur are accepted. Effect graphs are validated and flattened to an ordered pipeline. A graph must be one linear chain: one root, at most one input per node, and no node feeding two others. Branching or merging graphs are rejected. Decoded images/pages are bounded to 16 million pixels; effect chains to 32 operations. Apps should select smaller preview sizes for constrained devices.

Mask strokes use oriented source-pixel coordinates; white means retain and black means remove. Restoring mask coverage preserves original source alpha. `AndroidMask` accepts a base-mask adapter for host segmentation outputs; the library contains no segmentation model. Checkerboards belong only to the mask UI.

`ValidateText` and `Graphemes` preserve extended graphemes while adapters translate to UTF-16 indices. Text layouts use system fonts by default. A custom `FontResolver` must return already available, licensed typefaces; resolve remote fonts before taking a render/export snapshot. Missing fonts visibly fall back while their requested identity remains saved.

## Templates and layout

Templates use versioned `Recipe` and sealed `RecipeStep` values, with image slots resolved through document assets. Unknown versions, unresolved slots, incompatible media and excessive steps fail before editing. Premium application checks host entitlements again at apply time. No script, shader, class name or arbitrary resource path is executed from template data.

`SuggestLayout` uses deterministic Hungarian assignment (up to 128 photos/slots), required ROI visibility, and centered crops when analysis is absent. `orient` maps detector ROI coordinates; host analysis must use oriented asset dimensions. `AnalysisCache` keys account, content revision, orientation and analyzer version. Persist chosen crops in `Content.Image.crop` when applying placements.

## Still and video output

Both exporters take the concrete `DocumentRenderer` (not just the `PageRenderer` interface), so an app that exports keeps that instance. `StillExporter` writes a new PNG or JPEG, decodes it for validation, then atomically publishes it. PNG retains alpha; JPEG flattens against the caller's background (default white) at quality 90. Sources are never replaced.

`VideoExporter` uses Media3 1.11.1 and accepts a frozen document, `Timeline`, local media URI adapter, and destination file. Default output is 1920×1080 at 30 fps, SDR H.264/AAC in MP4. Encoder settings are probed; unsupported configuration returns a typed failure. Media3 implementation types remain in the data adapter; `prepare` also exposes an owned composition for a host Media3 preview. Close `PreparedVideo` after releasing its player.

Photo timelines support overlap crossfades and clock-driven `LayerMotion`; mixed photo/video timelines use cuts and optional still overlays on video clips. Overlap or motion on mixed timelines is rejected explicitly. Times are microseconds. For two 2-second photos and a 0.5-second overlap, duration is 3.5 seconds. Music source trim and placement are separate; default policy is trim without looping, and mute removes the track. Timelines are limited to five minutes and 128 clips.

`VideoExporter.state` distinguishes running progress, completion, cancellation and failure. If an advertised decoder fails at runtime, the adapter retries once with a platform software decoder before publishing anything. The muxer ends every track on the composition clock and drops encoder padding beyond that boundary. Export finalization verifies dimensions, duration within one frame (plus 1 ms), representative decoded frames and required streams. Temporary files are removed on exit. `AndroidDelivery` shares a host-owned content URI with read grants; the host configures its FileProvider. Still-image printing uses Android's print framework. Canceling share or print retains the verified output.

## AI and cloud boundaries

`ToolRunner` supports only move, setText and deleteLayer, validates exact argument keys and expected revision, correlates tool/result IDs, and limits runs to 16 calls. Runs stop at the first failure; already committed independent calls remain undoable. `MessagePart` keeps text, image, file and tool envelopes distinct. Unknown parts are data, never commands.

`ImageJobRunner` takes an `OperationStore`, a host `ImageProvider` and a media validator. Provider and validator may be null; a job then returns `UNAVAILABLE`. Use `FileOperationStore` for restart recovery; `MemoryOperationStore` is intended for tests or ephemeral sessions. A journal keeps up to 4,096 operations and fails closed when full: `ToolRunner.execute` and `ImageJobRunner.start` return a `LIMIT` failure for a new operation, while operations already recorded still replay; archive it according to the app's account/reconciliation retention policy. Provider keys and transport clients stay in the host backend/data adapter.

Logical identity is scoped to account/document/operation and bound to a request fingerprint. Duplicate completion replays its result; conflicts are rejected. An uncertain timeout reconciles the existing remote job instead of submitting another paid request. Providers must honor the logical ID as a server idempotency key. Local cancellation rejects late results without asserting remote cancellation or billing status. After restart, the host reconciles/rehydrates output media.

`AndroidGeneratedMediaValidator` checks MIME, decoded dimensions and media integrity. Returned images remain proposals until explicit revision-checked acceptance. Store their immutable bytes under the returned asset ID before accepting; keep original bytes for undo. `LetteringHistory` preserves account-scoped text/style references and accessible fallback text without generating again when reopening unavailable media. Telemetry contains operation IDs, state and usage counters; applications control retention and must avoid private prompts/media in traces.

## Provenance and limits

The capability inventory originated in an offline static review of `PicCollage-+Photo+Video+Editor_7.33.11_APKPure.xapk`, SHA-256 `a81bdb451fe066d48f899f8e64deec147cca28b7c183ed42439dea4a20d8a8aa`. This library uses the previously authored guidance and high-level findings only, as selected for this implementation. No fresh decompilation, proprietary code/media/fonts/prompts, app endpoints or credentials are included. The library does not claim PicCollage document compatibility, identical algorithms, or equivalent provider behavior.

Storage-backed operation journals are synchronous by contract; call orchestration from a host worker context when using a file journal. The optional video diagnostic callback is host-controlled and has no default logging.
