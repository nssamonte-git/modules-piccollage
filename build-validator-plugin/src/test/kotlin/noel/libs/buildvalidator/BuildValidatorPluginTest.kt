package noel.libs.buildvalidator

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Runs the plugin in real Gradle builds over the validator's Android fixtures (tests/fixtures/android):
 * `clean` must pass, `bad` must fail. The fixtures are not Gradle projects themselves, so each test
 * adds its own settings and a stand-in `preBuild` task (no Android Gradle Plugin needed).
 */
class BuildValidatorPluginTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private val fixtures = File(System.getProperty("fixturesDir"))

    private fun project(fixture: String, rootBuild: String, settings: String = ""): File {
        val dir = tmp.newFolder("project")
        File(fixtures, fixture).copyRecursively(dir)
        File(dir, "settings.gradle.kts").writeText("rootProject.name = \"fixture\"\n$settings")
        File(dir, "build.gradle.kts").writeText(rootBuild)
        return dir
    }

    private fun runner(dir: File, vararg args: String) =
        GradleRunner.create().withProjectDir(dir).withPluginClasspath().withArguments(*args, "--stacktrace")

    private val appliedWithPreBuild = """
        plugins { id("noel.libs.build-validator") }
        tasks.register("preBuild")
    """.trimIndent()

    @Test
    fun `clean project passes and writes a report`() {
        val dir = project("clean", appliedWithPreBuild)
        val result = runner(dir, "preBuild").build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":validateThreats")?.outcome)
        assertTrue(File(dir, "build/reports/build-validator/report.json").isFile)
    }

    @Test
    fun `bad project fails the build with the findings and fix text`() {
        val dir = project("bad", appliedWithPreBuild)
        val result = runner(dir, "preBuild").buildAndFail()
        assertEquals(TaskOutcome.FAILED, result.task(":validateThreats")?.outcome)
        assertTrue(result.output, result.output.contains("SH-TLS-001"))
        assertTrue(result.output, result.output.contains("how to fix (from the threat card)"))
        assertTrue(result.output, result.output.contains("critical or high findings"))
    }

    @Test
    fun `applied to the root it gates every subproject`() {
        val dir = project("bad", "plugins { id(\"noel.libs.build-validator\") }", "include(\":lib\")\n")
        File(dir, "lib").mkdirs()
        File(dir, "lib/build.gradle.kts").writeText("tasks.register(\"preBuild\")\ntasks.register(\"compileDebugKotlin\")\n")
        val result = runner(dir, ":lib:compileDebugKotlin").buildAndFail()
        assertEquals(TaskOutcome.FAILED, result.task(":validateThreats")?.outcome)
    }

    @Test
    fun `a reasoned exception file is honoured`() {
        val dir = project("clean", appliedWithPreBuild)
        File(dir, "app/src/main/java/com/example/good/Debug.kt")
            .writeText("package com.example.good\nclass D { val ok = javax.net.ssl.HostnameVerifier { _, _ -> true } }\n")
        runner(dir, "preBuild").buildAndFail()
        File(dir, ".build-validator-ignore")
            .writeText("SH-TLS-002\tapp/src/main/java/com/example/good/Debug.kt\ttest-only verifier, ticket SEC-1\n")
        val result = runner(dir, "preBuild").build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":validateThreats")?.outcome)
        assertTrue(result.output, result.output.contains("SH-TLS-002"))
    }
}
