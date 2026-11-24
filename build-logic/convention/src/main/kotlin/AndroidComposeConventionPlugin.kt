import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/** Jetpack Compose for a library module (apply after noel.libs.android.library). */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        extensions.getByType(LibraryExtension::class.java).buildFeatures.compose = true
        // Modifier and @Composable appear in the published API. Export their BOM constraints too.
        dependencies.add("api", dependencies.platform(library("androidx-compose-bom")))
        addDependency("api", "androidx-compose-ui")
        addDependency("api", "androidx-compose-foundation-layout")
        addDependency("implementation", "androidx-compose-ui-graphics")
        addDependency("implementation", "androidx-compose-ui-tooling-preview")
        addDependency("implementation", "androidx-compose-material3")
        addDependency("debugImplementation", "androidx-compose-ui-tooling")
    }
}
