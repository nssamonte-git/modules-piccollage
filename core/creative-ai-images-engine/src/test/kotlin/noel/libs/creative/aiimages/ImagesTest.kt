package noel.libs.creative.aiimages

import kotlinx.coroutines.test.runTest
import noel.libs.creative.aiimages.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class ImagesTest {
    private val request = ImageJob("op", "a", "d", 0, "asset", "1", 100, 50, "expand")

    @Test
    fun duplicateSubmitsOnce() = runTest {
        var calls = 0
        val provider =
            object : ImageProvider {
                override suspend fun generate(job: ImageJob): Outcome<GeneratedImage> {
                    calls++
                    return Outcome.Success(
                        GeneratedImage("out", "image/png", 100, 50, byteArrayOf(1))
                    )
                }
            }
        val runner =
            ImageJobRunner(
                MemoryOperationStore(),
                provider,
                object : GeneratedMediaValidator {
                    override fun validate(image: GeneratedImage) = Outcome.Success(image)
                },
            )
        runner.start(request)
        runner.start(request)
        assertEquals(1, calls)
    }

    @Test
    fun wrongMaskDimensionsRejected() = runTest {
        val runner = ImageJobRunner(MemoryOperationStore(), null, null)
        assertTrue(runner.start(request.copy(maskWidth = 50, maskHeight = 100)) is Outcome.Failure)
    }

    @Test
    fun absentProviderIsUnavailable() = runTest {
        assertTrue(
            ImageJobRunner(MemoryOperationStore(), null, null).start(request) is Outcome.Failure
        )
    }

    @Test
    fun letteringFallbackDoesNotRegenerate() {
        val h = LetteringEntry("op", "a", "Hello", "outline", "missing")
        assertEquals("Hello", h.fallbackText)
    }

    @Test
    fun fullJournalReturnsLimitFailureWithoutCallingTheProvider() = runTest {
        var calls = 0
        val provider =
            object : ImageProvider {
                override suspend fun generate(job: ImageJob): Outcome<GeneratedImage> {
                    calls++
                    return Outcome.Success(GeneratedImage("out-${job.operationId}", "image/png", 100, 50, byteArrayOf(1)))
                }
            }
        val validator =
            object : GeneratedMediaValidator {
                override fun validate(image: GeneratedImage) = Outcome.Success(image)
            }
        val runner = ImageJobRunner(MemoryOperationStore(maxEntries = 1), provider, validator)
        assertTrue(runner.start(request) is Outcome.Success)
        val second = runner.start(request.copy(operationId = "op2"))
        assertEquals(CreativeError.Code.LIMIT, (second as Outcome.Failure).error.code)
        assertEquals(1, calls)
    }
}
