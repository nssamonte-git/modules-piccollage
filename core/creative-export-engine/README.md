# core-creative-export-engine

Turns a `CreativeDocument` into files: a PNG or JPEG of one page, or an MP4 built from a timeline of pages, video clips and music. It also hands a finished file to the Android share sheet or the print framework. This is an engine: no Compose. Media3 types stay in the `data` package; the timeline model in `domain` is plain Kotlin.

## When to use it

- Saving a page as an image, or building a short slideshow or video from pages, local video clips and a music track.
- Sharing or printing an exported file.
- For ready-made export buttons and a progress bar, add [core-creative-export-compose](../creative-export-compose/README.md) on top.

## Add it

```kotlin
implementation("noel.libs:core-creative-export-engine:0.2.2")
```

Brings in through `api`: `core-creative-model`, `core-creative-renderer`, `androidx.media3:media3-transformer` and `kotlinx-coroutines-core`. Media3 effect and ExoPlayer are internal (`implementation`). You usually pair it with a history engine that holds the document, and with the same `DocumentRenderer` you use for preview.

## Quick start

```kotlin
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID
import noel.libs.creative.export.data.AndroidDelivery
import noel.libs.creative.export.data.LocalMedia
import noel.libs.creative.export.data.StillExporter
import noel.libs.creative.export.data.VideoExporter
import noel.libs.creative.export.domain.Clip
import noel.libs.creative.export.domain.Music
import noel.libs.creative.export.domain.Timeline
import noel.libs.creative.export.domain.VideoSettings
import noel.libs.creative.model.Outcome
import noel.libs.creative.renderer.data.DocumentRenderer

val renderer = DocumentRenderer(assets) // assets: your AssetBytes

// Still image (call from a coroutine). The destination must not exist yet.
val png = File(activity.filesDir, "exports/${UUID.randomUUID()}.png")
val still = StillExporter(renderer).export(document, document.pages.first().id, png)

// Video: two 2 s photos, 0.5 s crossfade, music from 1.25 s in its file placed at 0.25 s.
val video = VideoExporter(activity, renderer, object : LocalMedia {
    override fun uri(assetId: String): Uri = Uri.fromFile(localFileFor(assetId)) // your lookup
})
val timeline = Timeline(
    clips = document.pages.take(2).map { Clip.Photo(it.id, 2_000_000) },
    overlapUs = 500_000,
    music = Music("music", sourceTrimUs = 1_250_000, placementUs = 250_000),
)
val mp4 = File(activity.filesDir, "exports/${UUID.randomUUID()}.mp4")
val result = video.export(document, timeline, mp4, VideoSettings(640, 480))

// Share
if (result is Outcome.Success) {
    val uri = FileProvider.getUriForFile(activity, "${activity.packageName}.exports", mp4)
    AndroidDelivery().share(activity, uri, "video/mp4")
}
```

Observe `video.state` (a `StateFlow<ExportState>`) for progress.

## Main types

| Type | What it does |
|---|---|
| `StillExporter` | `export(document, pageId, destination, png = true, background = Color.WHITE)` writes a PNG or JPEG of one page. |
| `VideoExporter` | `export(document, timeline, destination, settings)` writes an MP4; `state` reports progress; `prepare(...)` returns a `PreparedVideo`. |
| `LocalMedia` | Your mapping from asset ID to a `file:` or `content:` `Uri` for video clips and music. |
| `PreparedVideo` | Holds a Media3 `Composition` for your own preview. Close it after releasing the player. |
| `Timeline` | Clips, crossfade `overlapUs`, optional `Music` and `LayerMotion` list. Times are microseconds. |
| `Clip.Photo` / `Clip.Video` | A page shown for `durationUs` (default 2 s), or a trimmed video with optional mute and page overlay. |
| `Music` | One audio track: `sourceTrimUs`, `placementUs`, `loop`, `muted`. |
| `LayerMotion` | Moves one layer linearly from one `Transform` to another between two composition times. |
| `VideoSettings` | Output width, height, frame rate and background colour. |
| `ExportState` | `Idle`, `Running(progress)`, `Completed(path, mimeType)`, `Failed(message)`, `Canceled`. |
| `AndroidDelivery` | `share(context, uri, mimeType)` and `printImage(activity, bitmap)`. |

## What your app provides

- An `AssetBytes` for the `DocumentRenderer`, and a `LocalMedia` that returns `file:` or `content:` URIs your app can read.
- Destination files. Exporters create parent folders but refuse an existing destination.
- A FileProvider for sharing, because `share` only accepts `content:` URIs. The sample declares:

```xml
<provider android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.exports" android:exported="false"
    android:grantUriPermissions="true">
    <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/export_paths" />
</provider>
```

with `res/xml/export_paths.xml`: `<paths><files-path name="exports" path="exports/" /></paths>`.

- Threading: both `export` functions are `suspend` and move work to `Dispatchers.IO`; video also uses `Dispatchers.Main`. `prepare` renders synchronously, so call it off the main thread. Cancel the calling coroutine to stop an export.
- An optional `diagnostics` callback on `VideoExporter` for Media3 errors. Nothing is logged by default.
- For `printImage`, keep the bitmap alive until printing ends; it is drawn when the print framework asks.

## Limits and behaviour

- Stills render the page at its own size with strict rendering, so missing content fails. JPEG is flattened on `background` at quality 90; PNG keeps alpha. The file is written to a temp file, decoded to check its size, then moved atomically.
- `Timeline`: 1 to 128 clips, each longer than `overlapUs` and at most 5 minutes, total at most 5 minutes. Overlap and motions need an all-photo timeline. Bad values throw `IllegalArgumentException` from the constructor.
- `VideoSettings`: default 1920x1080, 30 fps, black. Width and height must be even, at most 16,000,000 pixels; frame rate 1 to 60.
- Output is H.264 and AAC in MP4. HDR input is tone-mapped to SDR. Video clips are scaled to fit the frame.
- An H.264 encoder for the size and frame rate is checked first. Bad configuration or unavailable assets return `UNSUPPORTED`; encoding and validation errors return `IO`.
- If decoding fails, the export is retried once with software decoders.
- Before publishing, the file is checked for size, decodable first and middle frames, and an audio track when audio is expected. The video track must match the timeline within one frame. The audio track may also carry the AAC priming its edit list skips (at most 2112 samples, plus one 1024-sample frame either way); some platforms count that priming in the file-level duration, so that value is not used. Music `placementUs` must be before the end of the timeline.
- `state` is one flow per exporter and is not reset to `Idle`. `StillExporter` does not update it.
- On cancel or failure, temporary files are removed and the destination is not created.

## Tests

`TimelineTest` covers crossfade duration and clip start times, music source-time offsets, and rejection of overlap on a mixed timeline. `EncodedDurationTest` covers the export length check: video within one frame, audio with priming accepted, and longer or shorter audio rejected.

```bash
./gradlew :core-creative-export-engine:testDebugUnitTest
```

The sample's device tests export PNG and JPEG, a photo crossfade MP4 with music (size, duration, audio placement), a trimmed video clip with an overlay, and check that a canceled export publishes nothing:

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-export-compose](../creative-export-compose/README.md)
- [core-creative-renderer](../creative-renderer/README.md)
- Guide: [Still and video output](../../docs/creative-libraries.md#still-and-video-output)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` ("Export" tab), `DemoEnvironment.kt`, `sample/src/main/AndroidManifest.xml`
