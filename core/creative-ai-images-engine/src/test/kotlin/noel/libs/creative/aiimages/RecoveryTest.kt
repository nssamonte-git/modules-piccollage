package noel.libs.creative.aiimages

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import noel.libs.creative.aiimages.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class RecoveryTest {
    private val request = ImageJob("op", "a", "d", 0, "asset", "1", 100, 50, "expand")
    private val validator =
        object : GeneratedMediaValidator {
            override fun validate(image: GeneratedImage) = Outcome.Success(image)
        }

    @Test
    fun timeoutReconcilesWithoutResubmitting() = runTest {
        var submits = 0
        var lookups = 0
        val p =
            object : ImageProvider {
                override suspend fun generate(job: ImageJob): Outcome<GeneratedImage> {
                    submits++
                    delay(100)
                    return Outcome.Success(
                        GeneratedImage("out", "image/png", 100, 50, byteArrayOf(1))
                    )
                }

                override suspend fun reconcile(job: ImageJob): Outcome<GeneratedImage> {
                    lookups++
                    return Outcome.Success(
                        GeneratedImage("out", "image/png", 100, 50, byteArrayOf(1))
                    )
                }
            }
        val runner = ImageJobRunner(MemoryOperationStore(), p, validator, 10)
        assertTrue(runner.start(request) is Outcome.Failure)
        assertTrue(runner.start(request) is Outcome.Success)
        assertEquals(1, submits)
        assertEquals(1, lookups)
    }

    @Test
    fun canceledSessionRejectsLateResult() = runTest {
        val p =
            object : ImageProvider {
                override suspend fun generate(job: ImageJob): Outcome<GeneratedImage> {
                    delay(100)
                    return Outcome.Success(
                        GeneratedImage("out", "image/png", 100, 50, byteArrayOf(1))
                    )
                }
            }
        val runner = ImageJobRunner(MemoryOperationStore(), p, validator)
        val result = async { runner.start(request) }
        yield()
        runner.cancel(request)
        assertTrue(result.await() is Outcome.Failure)
    }

    @Test
    fun letteringHistoryIsAccountScoped() {
        val h = LetteringHistory(1)
        h.add(LetteringEntry("1", "a", "A", "x", null))
        h.add(LetteringEntry("2", "a", "B", "x", null))
        h.add(LetteringEntry("3", "b", "C", "x", null))
        assertEquals("B", h.list("a").single().text)
        assertEquals("C", h.list("b").single().text)
    }
}
