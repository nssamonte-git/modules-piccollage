import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

/** The shared version catalog (`gradle/libs.versions.toml`). */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal fun Project.library(alias: String) = libs.findLibrary(alias).orElseThrow {
    IllegalStateException("No library '$alias' in gradle/libs.versions.toml")
}

internal fun Project.addDependency(configuration: String, alias: String) {
    dependencies.add(configuration, library(alias))
}

/** Settings every module shares. */
internal object Library {
    const val GROUP = "noel.libs"
    const val COMPILE_SDK = 37
    const val MIN_SDK = 26
}
