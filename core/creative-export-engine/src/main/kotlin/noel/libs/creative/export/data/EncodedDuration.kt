package noel.libs.creative.export.data

import kotlin.math.abs

/** AAC codes 1024 samples per frame. */
internal const val AAC_FRAME_SAMPLES = 1024L

/** Largest AAC encoder delay in common use; the MP4 audio edit list skips these priming samples. */
internal const val AAC_MAX_PRIMING_SAMPLES = 2112L

/**
 * Checks an encoded export's track lengths against the composition.
 *
 * The video track sets the length: it has no priming, so it must match within one frame. The audio
 * track's media duration may include the AAC priming samples its edit list skips. Some platforms
 * report that raw length (an API 35 emulator reads a correct 3.5 s file as 3.536 s), so the file-level
 * duration from `MediaMetadataRetriever` is not used.
 */
internal fun checkEncodedDurations(
    expectedUs: Long,
    frameRate: Int,
    videoUs: Long,
    audioUs: Long?,
    audioSampleRate: Int?,
) {
    require(abs(videoUs - expectedUs) <= 1_000_000L / frameRate + 1000) {
        "video duration $videoUs expected $expectedUs"
    }
    if (audioUs == null) return
    require(audioSampleRate != null && audioSampleRate > 0) { "audio track has no sample rate" }
    val frameUs = AAC_FRAME_SAMPLES * 1_000_000L / audioSampleRate
    val primingUs = AAC_MAX_PRIMING_SAMPLES * 1_000_000L / audioSampleRate
    require(audioUs in (expectedUs - frameUs)..(expectedUs + primingUs + frameUs)) {
        "audio duration $audioUs expected $expectedUs"
    }
}
