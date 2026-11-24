@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package noel.libs.creative.export.data

import android.content.Context
import android.graphics.*
import android.media.*
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Clock
import androidx.media3.datasource.DataSourceBitmapLoader
import androidx.media3.effect.BitmapOverlay
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.Presentation
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.transformer.*
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.math.abs
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import noel.libs.creative.export.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.renderer.data.DocumentRenderer

interface LocalMedia {
    fun uri(assetId: String): Uri
}

/** Media3 adapters stay in data. The host owns access grants for content/file URIs. */
class VideoExporter(
    private val context: Context,
    private val renderer: DocumentRenderer,
    private val media: LocalMedia,
    private val diagnostics: (Throwable) -> Unit = {},
) {
    private val mutableState = MutableStateFlow<ExportState>(ExportState.Idle)
    val state = mutableState.asStateFlow()

    private fun local(id: String): Uri =
        media.uri(id).also { require(it.scheme in setOf("file", "content")) }

    private fun preflight(settings: VideoSettings) {
        val f =
            MediaFormat.createVideoFormat("video/avc", settings.width, settings.height).apply {
                setInteger(MediaFormat.KEY_FRAME_RATE, settings.frameRate)
                setInteger(MediaFormat.KEY_BIT_RATE, 8_000_000)
                setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
                setInteger(
                    MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface,
                )
            }
        require(MediaCodecList(MediaCodecList.REGULAR_CODECS).findEncoderForFormat(f) != null) {
            "Encoder configuration unavailable"
        }
    }

    suspend fun export(
        document: CreativeDocument,
        timeline: Timeline,
        destination: File,
        settings: VideoSettings = VideoSettings(),
    ): Outcome<File> {
        var decodingFailed = false
        val first =
            exportAttempt(document, timeline, destination, settings, false) { code ->
                decodingFailed =
                    code == ExportException.ERROR_CODE_DECODING_FAILED ||
                        code == ExportException.ERROR_CODE_DECODER_INIT_FAILED
            }
        val result =
            if (first is Outcome.Failure && decodingFailed)
                exportAttempt(document, timeline, destination, settings, true) {}
            else first
        mutableState.value =
            when (result) {
                is Outcome.Success -> ExportState.Completed(result.value.path, "video/mp4")
                is Outcome.Failure -> ExportState.Failed(result.error.message)
            }
        return result
    }

    private suspend fun exportAttempt(
        document: CreativeDocument,
        timeline: Timeline,
        destination: File,
        settings: VideoSettings,
        software: Boolean,
        codecFailure: (Int) -> Unit,
    ): Outcome<File> = coroutineScope {
        val owner = this
        val snapshot = document.immutableCopy()
        var prepared: PreparedVideo? = null
        var activeTransformer: Transformer? = null
        val temp = File(destination.parentFile, "video-${UUID.randomUUID()}.mp4")
        try {
            withContext(Dispatchers.IO) {
                require(!destination.exists())
                destination.parentFile?.mkdirs()
                preflight(settings)
                prepared =
                    prepare(
                        snapshot,
                        timeline,
                        settings,
                        File(context.cacheDir, "creative-${UUID.randomUUID()}"),
                    )
            }
            mutableState.value = ExportState.Running(0)
            val result =
                withContext(Dispatchers.Main.immediate) {
                    suspendCancellableCoroutine<Outcome<File>> { continuation ->
                        val transformer =
                            Transformer.Builder(context.applicationContext)
                                .apply {
                                    if (software) {
                                        val decoder =
                                            DefaultDecoderFactory.Builder(context)
                                                .setMediaCodecSelector(
                                                    MediaCodecSelector.PREFER_SOFTWARE
                                                )
                                                .setEnableDecoderFallback(true)
                                                .build()
                                        setAssetLoaderFactory(
                                            DefaultAssetLoaderFactory(
                                                context,
                                                decoder,
                                                Clock.DEFAULT,
                                                DefaultMediaSourceFactory(context),
                                                DataSourceBitmapLoader(context),
                                            )
                                        )
                                    }
                                }
                                .setMuxerFactory(TimelineMuxerFactory(timeline.durationUs))
                                .setVideoMimeType(MimeTypes.VIDEO_H264)
                                .setAudioMimeType(MimeTypes.AUDIO_AAC)
                                .addListener(
                                    object : Transformer.Listener {
                                        override fun onCompleted(
                                            composition: Composition,
                                            exportResult: ExportResult,
                                        ) {
                                            owner.launch(Dispatchers.IO) {
                                                val result =
                                                    try {
                                                        verify(
                                                            temp,
                                                            settings,
                                                            timeline.durationUs,
                                                            timeline.music?.let { !it.muted } ==
                                                                true ||
                                                                timeline.clips
                                                                    .filterIsInstance<Clip.Video>()
                                                                    .any {
                                                                        !it.muted &&
                                                                            hasAudio(it.assetId)
                                                                    },
                                                        )
                                                        ensureActive()
                                                        Files.move(
                                                            temp.toPath(),
                                                            destination.toPath(),
                                                            StandardCopyOption.ATOMIC_MOVE,
                                                        )
                                                        Outcome.Success(destination)
                                                    } catch (e: CancellationException) {
                                                        throw e
                                                    } catch (e: Exception) {
                                                        failure(
                                                            CreativeError.Code.IO,
                                                            "Encoded video validation failed: ${e.message ?: e.javaClass.simpleName}",
                                                        )
                                                    }
                                                if (continuation.isActive)
                                                    continuation.resume(result)
                                            }
                                        }

                                        override fun onError(
                                            composition: Composition,
                                            exportResult: ExportResult,
                                            exportException: ExportException,
                                        ) {
                                            codecFailure(exportException.errorCode)
                                            runCatching { diagnostics(exportException) }
                                            if (continuation.isActive)
                                                continuation.resume(
                                                    failure(
                                                        CreativeError.Code.IO,
                                                        "Video encoding failed: ${exportException.errorCode}",
                                                    )
                                                )
                                        }
                                    }
                                )
                                .build()
                        activeTransformer = transformer
                        val handler = Handler(Looper.getMainLooper())
                        val progress =
                            object : Runnable {
                                override fun run() {
                                    if (continuation.isActive) {
                                        val holder = ProgressHolder()
                                        if (
                                            transformer.getProgress(holder) ==
                                                Transformer.PROGRESS_STATE_AVAILABLE
                                        )
                                            mutableState.value =
                                                ExportState.Running(holder.progress)
                                        handler.postDelayed(this, 200)
                                    }
                                }
                            }
                        continuation.invokeOnCancellation {
                            handler.post {
                                handler.removeCallbacks(progress)
                                transformer.cancel()
                            }
                        }
                        try {
                            transformer.start(prepared!!.composition, temp.path)
                            handler.post(progress)
                        } catch (e: Exception) {
                            transformer.cancel()
                            if (continuation.isActive)
                                continuation.resume(
                                    failure(CreativeError.Code.IO, "Video start failed")
                                )
                        }
                    }
                }
            result
        } catch (e: CancellationException) {
            mutableState.value = ExportState.Canceled
            throw e
        } catch (e: Exception) {
            mutableState.value = ExportState.Failed(e.javaClass.simpleName)
            failure(CreativeError.Code.UNSUPPORTED, "Video configuration or assets unavailable")
        } finally {
            withContext(NonCancellable + Dispatchers.Main.immediate) { activeTransformer?.cancel() }
            withContext(NonCancellable + Dispatchers.IO) {
                prepared?.close()
                temp.delete()
            }
        }
    }

    fun prepare(
        document: CreativeDocument,
        timeline: Timeline,
        settings: VideoSettings,
        work: File,
    ): PreparedVideo {
        require(!work.exists())
        work.mkdirs()
        val owned = mutableListOf<BitmapOverlay>()
        try {
            val video =
                EditedMediaItemSequence.Builder(setOf(C.TRACK_TYPE_VIDEO, C.TRACK_TYPE_AUDIO))
            if (timeline.clips.all { it is Clip.Photo }) {
                val blank =
                    Bitmap.createBitmap(settings.width, settings.height, Bitmap.Config.ARGB_8888)
                blank.eraseColor(settings.background)
                val file = File(work, "base.png")
                file.outputStream().use { blank.compress(Bitmap.CompressFormat.PNG, 100, it) }
                blank.recycle()
                val overlay = PhotoOverlay(renderer, document, timeline, settings)
                owned += overlay
                val item =
                    EditedMediaItem.Builder(
                            MediaItem.Builder()
                                .setUri(Uri.fromFile(file))
                                .setImageDurationMs((timeline.durationUs + 999) / 1000)
                                .build()
                        )
                        .setFrameRate(settings.frameRate)
                        .setEffects(Effects(emptyList(), listOf(OverlayEffect(listOf(overlay)))))
                        .build()
                video.addItem(item)
            } else
                for ((index, clip) in timeline.clips.withIndex()) {
                    val item =
                        when (clip) {
                            is Clip.Photo -> {
                                val bitmap =
                                    (renderer.render(
                                            document,
                                            clip.pageId,
                                            settings.width,
                                            settings.height,
                                            true,
                                        ) as? Outcome.Success)
                                        ?.value
                                        ?.bitmap ?: error("Photo unavailable")
                                val file = File(work, "page-$index.png")
                                file.outputStream().use {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                                }
                                bitmap.recycle()
                                EditedMediaItem.Builder(
                                        MediaItem.Builder()
                                            .setUri(Uri.fromFile(file))
                                            .setImageDurationMs((clip.durationUs + 999) / 1000)
                                            .build()
                                    )
                                    .setFrameRate(settings.frameRate)
                                    .build()
                            }
                            is Clip.Video -> {
                                val input =
                                    MediaItem.Builder()
                                        .setUri(local(clip.assetId))
                                        .setClippingConfiguration(
                                            MediaItem.ClippingConfiguration.Builder()
                                                .setStartPositionUs(clip.trimStartUs)
                                                .setEndPositionUs(clip.trimEndUs)
                                                .build()
                                        )
                                        .build()
                                val fx =
                                    mutableListOf<androidx.media3.common.Effect>(
                                        Presentation.createForWidthAndHeight(
                                            settings.width,
                                            settings.height,
                                            Presentation.LAYOUT_SCALE_TO_FIT,
                                        )
                                    )
                                clip.overlayPageId?.let { id ->
                                    val bitmap =
                                        (renderer.render(
                                                document,
                                                id,
                                                settings.width,
                                                settings.height,
                                                true,
                                            ) as? Outcome.Success)
                                            ?.value
                                            ?.bitmap ?: error("Overlay unavailable")
                                    val overlay = OwnedBitmapOverlay(bitmap)
                                    owned += overlay
                                    fx += OverlayEffect(listOf(overlay))
                                }
                                EditedMediaItem.Builder(input)
                                    .setRemoveAudio(clip.muted)
                                    .setEffects(Effects(emptyList(), fx))
                                    .build()
                            }
                        }
                    video.addItem(item)
                }
            val sequences = mutableListOf(video.build())
            timeline.music
                ?.takeUnless { it.muted }
                ?.let { music ->
                    require(music.placementUs < timeline.durationUs)
                    val audio = EditedMediaItemSequence.Builder(setOf(C.TRACK_TYPE_AUDIO))
                    if (music.placementUs > 0) audio.addGap(music.placementUs)
                    val input =
                        MediaItem.Builder()
                            .setUri(local(music.assetId))
                            .setClippingConfiguration(
                                MediaItem.ClippingConfiguration.Builder()
                                    .setStartPositionUs(music.sourceTrimUs)
                                    .setEndPositionUs(
                                        music.sourceTrimUs + timeline.durationUs - music.placementUs
                                    )
                                    .build()
                            )
                            .build()
                    audio.addItem(EditedMediaItem.Builder(input).setRemoveVideo(true).build())
                    audio.setIsLooping(music.loop)
                    sequences += audio.build()
                }
            val composition =
                Composition.Builder(sequences)
                    .setHdrMode(Composition.HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_OPEN_GL)
                    .build()
            return PreparedVideo(composition, work, owned)
        } catch (e: Exception) {
            owned.forEach { runCatching { it.release() } }
            work.deleteRecursively()
            throw e
        }
    }

    private fun hasAudio(assetId: String): Boolean {
        val extractor = MediaExtractor()
        return try {
            extractor.setDataSource(context, local(assetId), null)
            (0 until extractor.trackCount).any {
                extractor
                    .getTrackFormat(it)
                    .getString(MediaFormat.KEY_MIME)
                    ?.startsWith("audio/") == true
            }
        } finally {
            extractor.release()
        }
    }

    private fun verify(
        file: File,
        settings: VideoSettings,
        durationUs: Long,
        expectAudio: Boolean,
    ) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(file.path)
            require(
                retriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    ?.toInt() == settings.width
            )
            require(
                retriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                    ?.toInt() == settings.height
            )
            val actual =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong() *
                    1000
            require(abs(actual - durationUs) <= 1_000_000 / settings.frameRate + 1000) {
                "duration $actual expected $durationUs"
            }
            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)?.recycle()
                ?: error("No decoded first frame")
            retriever
                .getFrameAtTime(durationUs / 2, MediaMetadataRetriever.OPTION_CLOSEST)
                ?.recycle() ?: error("No decoded middle frame")
            val extractor = MediaExtractor()
            try {
                extractor.setDataSource(file.path)
                val types =
                    (0 until extractor.trackCount).map {
                        extractor.getTrackFormat(it).getString(MediaFormat.KEY_MIME) ?: ""
                    }
                require(types.any { it.startsWith("video/") })
                if (expectAudio) require(types.any { it.startsWith("audio/") })
            } finally {
                extractor.release()
            }
        } finally {
            retriever.release()
        }
    }
}

class PreparedVideo
internal constructor(
    val composition: Composition,
    private val work: File,
    private val overlays: List<BitmapOverlay>,
) : AutoCloseable {
    override fun close() {
        overlays.forEach { runCatching { it.release() } }
        work.deleteRecursively()
    }
}

private class PhotoOverlay(
    private val renderer: DocumentRenderer,
    private val document: CreativeDocument,
    private val timeline: Timeline,
    private val settings: VideoSettings,
) : BitmapOverlay() {
    private val frame =
        Bitmap.createBitmap(settings.width, settings.height, Bitmap.Config.ARGB_8888)

    override fun getBitmap(presentationTimeUs: Long): Bitmap {
        val canvas = Canvas(frame)
        canvas.drawColor(settings.background, PorterDuff.Mode.SRC)
        val starts = timeline.startsUs
        timeline.clips.forEachIndexed { index, c ->
            val clip = c as Clip.Photo
            val start = starts[index]
            if (presentationTimeUs >= start && presentationTimeUs < start + clip.durationUs) {
                val page = document.pages.first { it.id == clip.pageId }
                val animated =
                    page.copy(
                        layers =
                            page.layers.map { l ->
                                val motion =
                                    timeline.motions.firstOrNull {
                                        it.pageId == page.id && it.layerId == l.id
                                    }
                                if (motion == null) l
                                else l.copy(transform = motion.at(presentationTimeUs))
                            }
                    )
                val d =
                    document.copy(
                        pages = document.pages.map { if (it.id == page.id) animated else it }
                    )
                val bitmap =
                    (renderer.render(d, page.id, settings.width, settings.height, true)
                            as? Outcome.Success)
                        ?.value
                        ?.bitmap ?: error("Timeline render failed")
                val alpha =
                    if (
                        index > 0 &&
                            timeline.overlapUs > 0 &&
                            presentationTimeUs < start + timeline.overlapUs
                    )
                        (presentationTimeUs - start).toFloat() / timeline.overlapUs
                    else 1f
                canvas.drawBitmap(
                    bitmap,
                    0f,
                    0f,
                    Paint().apply { this.alpha = (alpha * 255).toInt() },
                )
                bitmap.recycle()
            }
        }
        return frame
    }

    override fun release() {
        try {
            super.release()
        } finally {
            if (!frame.isRecycled) frame.recycle()
        }
    }
}

private class OwnedBitmapOverlay(private val bitmap: Bitmap) : BitmapOverlay() {
    override fun getBitmap(presentationTimeUs: Long): Bitmap = bitmap

    override fun release() {
        try {
            super.release()
        } finally {
            if (!bitmap.isRecycled) bitmap.recycle()
        }
    }
}
