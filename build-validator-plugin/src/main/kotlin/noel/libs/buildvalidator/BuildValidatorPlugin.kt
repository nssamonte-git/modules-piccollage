package noel.libs.buildvalidator

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Mobile build validator (Native Android). Registers `validateThreats`, which scans the source of
 * the whole Gradle build (from the settings directory) and fails on critical or high findings.
 *
 * - Applied to the root project: every project's `preBuild` and Kotlin compile tasks depend on it,
 *   so app and library builds both stop on a blocking finding.
 * - Applied to one module (for example `:app`): only that module's tasks depend on it.
 *
 * There is no option to lower the threshold or skip the scan. Accept a finding only with a
 * reasoned exception (inline `build-validator:ignore` or `.build-validator-ignore` at the root).
 */
class BuildValidatorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val validate = project.tasks.register("validateThreats", ValidateThreatsTask::class.java) { task ->
            task.group = "verification"
            task.description = "Scans the project source for known mobile threats and fails on blocking findings."
            task.scanRoot.set(project.layout.settingsDirectory)
            task.report.set(project.layout.buildDirectory.file("reports/build-validator/report.json"))
        }
        val hooked = if (project == project.rootProject) project.allprojects else setOf(project)
        hooked.forEach { p ->
            p.tasks.configureEach { t ->
                if (t.name == "preBuild" || (t.name.startsWith("compile") && t.name.contains("Kotlin"))) {
                    t.dependsOn(validate)
                }
            }
        }
    }
}
