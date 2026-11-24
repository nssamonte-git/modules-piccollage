import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

/**
 * A reusable Android library module: AGP 9 with its built-in Kotlin, compileSdk 37, minSdk 26,
 * Java 11, JUnit 4 tests. Publishes the release variant as `noel.libs:<project name>` (for example
 * folder core/logging -> project `:core-logging` -> `noel.libs:core-logging`) at `library.version`.
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("maven-publish")
        group = Library.GROUP
        version = providers.gradleProperty("library.version").get()
        extensions.getByType(LibraryExtension::class.java).apply {
            compileSdk { version = release(Library.COMPILE_SDK) }
            defaultConfig {
                minSdk = Library.MIN_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            publishing { singleVariant("release") { withSourcesJar() } }
        }
        addDependency("testImplementation", "junit")
        addDependency("androidTestImplementation", "androidx-test-runner")
        addDependency("androidTestImplementation", "androidx-test-ext-junit")
        afterEvaluate {
            extensions.getByType(PublishingExtension::class.java).publications.register("release", MavenPublication::class.java) { pub ->
                pub.from(components.getByName("release"))
                pub.artifactId = name
            }
        }
    }
}
