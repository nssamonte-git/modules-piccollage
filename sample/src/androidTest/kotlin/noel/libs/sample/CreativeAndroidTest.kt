package noel.libs.sample

import android.graphics.*
import android.media.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.UUID
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import noel.libs.creative.aiimages.data.*
import noel.libs.creative.aiimages.domain.*
import noel.libs.creative.effects.data.*
import noel.libs.creative.export.data.*
import noel.libs.creative.export.domain.*
import noel.libs.creative.masks.data.*
import noel.libs.creative.model.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreativeAndroidTest {
    private val context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun gpuMatchesCpuAndPreservesOrientation() {
        val pixels =
            intArrayOf(
                0xffff0000.toInt(),
                0xff00ff00.toInt(),
                0xff0000ff.toInt(),
                0x804080c0.toInt(),
            )
        val source = Bitmap.createBitmap(pixels, 2, 2, Bitmap.Config.ARGB_8888)
        var backend = ""
        val filters =
            listOf(Effect(Effect.Kind.SATURATION, 0.7f), Effect(Effect.Kind.BRIGHTNESS, 0.1f))
        val gpu = GpuImageEffects { backend = it }.apply(source, filters)
        val cpu = CpuImageEffects().apply(source, filters)
        try {
            assertEquals("OpenGL ES 2", backend)
            for (y in 0..1) for (x in 0..1) {
                val a = gpu.getPixel(x, y)
                val b = cpu.getPixel(x, y)
                for (shift in listOf(0, 8, 16, 24)) assertTrue(
                    "channel at $x,$y: $a vs $b",
                    kotlin.math.abs(((a ushr shift) and 255) - ((b ushr shift) and 255)) <= 3,
                )
            }
        } finally {
            source.recycle()
            gpu.recycle()
            cpu.recycle()
        }
    }

    @Test
    fun maskRestoresCoverageWithoutChangingSourceAlpha() {
        val source =
            Bitmap.createBitmap(intArrayOf(0x804080c0.toInt()), 1, 1, Bitmap.Config.ARGB_8888)
        val result = AndroidMask().apply(source, Mask())
        try {
            assertEquals(128, result.getPixel(0, 0) ushr 24)
            assertEquals(128, source.getPixel(0, 0) ushr 24)
        } finally {
            source.recycle()
            result.recycle()
        }
    }

    @Test
    fun pngAndJpegAreDecodable() = runBlocking {
        val env =
            DemoEnvironment(context) { android.util.Log.e("CreativeTest", "export diagnostic", it) }
        val d = env.document()
        val exporter = StillExporter(env.renderer)
        for (png in listOf(true, false)) {
            val destination =
                File(context.cacheDir, "test-${UUID.randomUUID()}.${if(png)"png" else "jpg"}")
            try {
                val result = exporter.export(d, d.pages.first().id, destination, png)
                assertTrue(result.toString(), result is Outcome.Success)
                val b = BitmapFactory.decodeFile(destination.path)
                assertNotNull(b)
                assertEquals(640, b.width)
                assertEquals(480, b.height)
                b.recycle()
            } finally {
                destination.delete()
            }
        }
    }

    @Test
    fun corruptGeneratedMediaRejected() {
        val result =
            AndroidGeneratedMediaValidator()
                .validate(GeneratedImage("x", "image/png", 10, 10, byteArrayOf(1, 2, 3)))
        assertTrue(result is Outcome.Failure)
    }

    @Test(timeout = 180000)
    fun photoCrossfadeMusicProducesPlayableMp4() = runBlocking {
        val env =
            DemoEnvironment(context) { android.util.Log.e("CreativeTest", "export diagnostic", it) }
        val d = env.document()
        val file = File(context.cacheDir, "test-${UUID.randomUUID()}.mp4")
        try {
            val timeline =
                Timeline(
                    d.pages.map { Clip.Photo(it.id, 2_000_000) },
                    500_000,
                    Music("music", 1_250_000, 250_000),
                )
            val result = env.video.export(d, timeline, file, VideoSettings(320, 240))
            assertTrue(result.toString(), result is Outcome.Success)
            assertTrue(
                "Decoded audio placement",
                kotlin.math.abs(firstAudibleUs(file) - 250_000) < 50_000,
            )
            val r = MediaMetadataRetriever()
            try {
                r.setDataSource(file.path)
                assertEquals(
                    "320",
                    r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH),
                )
                // The video track sets the length; the file-level duration can include AAC priming.
                val videoUs = trackDurationUs(file, "video/")
                assertTrue("video duration $videoUs", kotlin.math.abs(videoUs - 3_500_000) <= 34_000)
                assertNotNull(
                    r.getFrameAtTime(1_750_000, MediaMetadataRetriever.OPTION_CLOSEST)?.also {
                        it.recycle()
                    }
                )
            } finally {
                r.release()
            }
        } finally {
            file.delete()
        }
    }

    @Test(timeout = 180000)
    fun videoClipTrimAndOverlayExport() = runBlocking {
        val env =
            DemoEnvironment(context) { android.util.Log.e("CreativeTest", "export diagnostic", it) }
        val d = env.document()
        val source = File(context.cacheDir, "source-${UUID.randomUUID()}.mp4")
        val output = File(context.cacheDir, "trim-${UUID.randomUUID()}.mp4")
        try {
            assertTrue(
                env.video.export(
                    d,
                    Timeline(listOf(Clip.Photo(d.pages.first().id, 2_000_000))),
                    source,
                    VideoSettings(320, 240),
                ) is Outcome.Success
            )
            val exporter =
                VideoExporter(
                    context,
                    env.renderer,
                    object : LocalMedia {
                        override fun uri(assetId: String) = android.net.Uri.fromFile(source)
                    },
                    { android.util.Log.e("CreativeTest", "clip diagnostic", it) },
                )
            val result =
                exporter.export(
                    d,
                    Timeline(listOf(Clip.Video("v", 500_000, 1_500_000, true, d.pages.last().id))),
                    output,
                    VideoSettings(320, 240),
                )
            assertTrue(result.toString(), result is Outcome.Success)
        } finally {
            source.delete()
            output.delete()
        }
    }

    @Test
    fun sampleActivityLaunches() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val intent =
            android.content
                .Intent(context, MainActivity::class.java)
                .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = instrumentation.startActivitySync(intent)
        instrumentation.waitForIdleSync()
        assertFalse(activity.isFinishing)
        instrumentation.runOnMainSync { activity.finish() }
    }

    @Test(timeout = 60000)
    fun canceledVideoDoesNotPublish() = runBlocking {
        val env =
            DemoEnvironment(context) { android.util.Log.e("CreativeTest", "export diagnostic", it) }
        val d = env.document()
        val destination = File(context.cacheDir, "cancel-${UUID.randomUUID()}.mp4")
        val task = launch {
            env.video.export(
                d,
                Timeline(listOf(Clip.Photo(d.pages.first().id, 60_000_000))),
                destination,
                VideoSettings(320, 240),
            )
        }
        withTimeout(15000) { env.video.state.first { it is ExportState.Running } }
        task.cancelAndJoin()
        assertFalse(destination.exists())
        assertEquals(ExportState.Canceled, env.video.state.value)
    }

    private fun trackDurationUs(file: File, mimePrefix: String): Long {
        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(file.path)
            return (0 until extractor.trackCount)
                .map { extractor.getTrackFormat(it) }
                .first { it.getString(MediaFormat.KEY_MIME)!!.startsWith(mimePrefix) }
                .getLong(MediaFormat.KEY_DURATION)
        } finally {
            extractor.release()
        }
    }

    private fun firstAudibleUs(file: File): Long {
        val extractor = MediaExtractor()
        var decoder: MediaCodec? = null
        try {
            extractor.setDataSource(file.path)
            val track =
                (0 until extractor.trackCount).first {
                    extractor
                        .getTrackFormat(it)
                        .getString(MediaFormat.KEY_MIME)!!
                        .startsWith("audio/")
                }
            extractor.selectTrack(track)
            val format = extractor.getTrackFormat(track)
            val codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME)!!)
            decoder = codec
            codec.configure(format, null, null, 0)
            codec.start()
            var inputEnded = false
            val info = MediaCodec.BufferInfo()
            repeat(10000) {
                if (!inputEnded) {
                    val index = codec.dequeueInputBuffer(5000)
                    if (index >= 0) {
                        val input = codec.getInputBuffer(index)!!
                        val count = extractor.readSampleData(input, 0)
                        if (count < 0) {
                            codec.queueInputBuffer(
                                index,
                                0,
                                0,
                                0,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM,
                            )
                            inputEnded = true
                        } else {
                            codec.queueInputBuffer(index, 0, count, extractor.sampleTime, 0)
                            extractor.advance()
                        }
                    }
                }
                val index = codec.dequeueOutputBuffer(info, 5000)
                if (index >= 0) {
                    val output =
                        codec.getOutputBuffer(index)!!.order(java.nio.ByteOrder.LITTLE_ENDIAN)
                    val out = codec.outputFormat
                    val rate = out.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                    val channels = out.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
                    for (offset in 0 until info.size / 2) {
                        if (
                            kotlin.math.abs(output.getShort(info.offset + offset * 2).toInt()) > 256
                        ) {
                            val time =
                                info.presentationTimeUs + (offset / channels) * 1_000_000L / rate
                            codec.releaseOutputBuffer(index, false)
                            return time
                        }
                    }
                    codec.releaseOutputBuffer(index, false)
                    if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) return -1
                }
            }
            return -1
        } finally {
            decoder?.let {
                runCatching { it.stop() }
                it.release()
            }
            extractor.release()
        }
    }
}
