package noel.libs.buildvalidator

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

/** Runs the bundled scanner (`threat-scan.sh --platform android`) against [scanRoot]. */
@UntrackedTask(because = "The scan reads the whole source tree and must run on every build.")
abstract class ValidateThreatsTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    /** Root of the tree to scan; also where `.build-validator-ignore` is read from. */
    @get:Internal
    abstract val scanRoot: DirectoryProperty

    /** JSON report of the last scan. */
    @get:Internal
    abstract val report: RegularFileProperty

    @TaskAction
    fun scan() {
        val engine = File(temporaryDir, "engine")
        extractEngine(engine)
        val reportFile = report.get().asFile
        reportFile.parentFile.mkdirs()
        val shell = if (System.getProperty("os.name").lowercase().contains("windows")) "bash" else "sh"
        val result = execOperations.exec { spec ->
            spec.commandLine(
                shell, File(engine, "threat-scan.sh").absolutePath,
                "--platform", "android",
                "--threshold", THRESHOLD,
                "--root", scanRoot.get().asFile.absolutePath,
                "--report", reportFile.absolutePath,
            )
            spec.isIgnoreExitValue = true
        }
        when (result.exitValue) {
            0 -> logger.info("Mobile build validator: no blocking findings. Report: $reportFile")
            1 -> throw GradleException(
                "Mobile build validator: critical or high findings (listed above) block this build. " +
                    "Fix them, or record a reasoned exception (build-validator:ignore or .build-validator-ignore). " +
                    "Report: $reportFile",
            )
            else -> throw GradleException("Mobile build validator: configuration error (exit ${result.exitValue}); see the output above.")
        }
    }

    private fun extractEngine(engine: File) {
        engine.deleteRecursively()
        val files = resourceText("$RESOURCE_ROOT/engine-files.txt").lines().filter { it.isNotBlank() }
        check(files.isNotEmpty()) { "Mobile build validator: the plugin jar has no bundled scanner." }
        for (path in files) {
            val target = File(engine, path)
            target.parentFile.mkdirs()
            openResource("$RESOURCE_ROOT/engine/$path").use { input -> target.outputStream().use { input.copyTo(it) } }
            if (target.name.endsWith(".sh")) target.setExecutable(true)
        }
    }

    // No URL caching: with includeBuild the plugin jar is rebuilt in place while the daemon lives.
    private fun openResource(path: String) =
        (javaClass.classLoader.getResource(path) ?: throw GradleException("Mobile build validator: missing resource $path"))
            .openConnection().apply { useCaches = false }.getInputStream()

    private fun resourceText(path: String) = openResource(path).use { it.readBytes().toString(Charsets.UTF_8) }

    private companion object {
        const val RESOURCE_ROOT = "noel/libs/buildvalidator"

        /** Findings at or above this severity fail the build. Changing it is a reviewed code change. */
        const val THRESHOLD = "high"
    }
}
