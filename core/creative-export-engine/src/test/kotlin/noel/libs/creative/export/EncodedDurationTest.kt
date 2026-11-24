package noel.libs.creative.export

import noel.libs.creative.export.data.checkEncodedDurations
import org.junit.Assert.*
import org.junit.Test

class EncodedDurationTest {
    private fun check(videoUs: Long, audioUs: Long?, rate: Int? = 44_100) = runCatching {
        checkEncodedDurations(3_500_000, 30, videoUs, audioUs, rate)
    }

    @Test
    fun acceptsAudioIncludingPriming() {
        // Measured on an API 35 emulator: 1600 priming samples at 44.1 kHz in the audio media duration.
        assertTrue(check(3_500_000, 3_536_281).isSuccess)
        assertTrue(check(3_500_000, 3_500_000).isSuccess)
        assertTrue(check(3_500_000, null).isSuccess)
    }

    @Test
    fun videoMustMatchWithinOneFrame() {
        assertTrue(check(3_533_000, null).isSuccess)
        assertTrue(check(3_536_000, null).isFailure)
        assertTrue(check(3_464_000, null).isFailure)
    }

    @Test
    fun audioBeyondPrimingAndOneFrameFails() {
        // 2112 priming + 1024 samples at 44.1 kHz is about 71.1 ms.
        assertTrue(check(3_500_000, 3_571_000).isSuccess)
        assertTrue(check(3_500_000, 3_572_000).isFailure)
        assertTrue(check(3_500_000, 3_476_000).isFailure)
    }

    @Test
    fun audioWithoutSampleRateFails() {
        assertTrue(check(3_500_000, 3_500_000, null).isFailure)
    }
}
