pluginManagement { repositories { google(); mavenCentral(); gradlePluginPortal() } }
dependencyResolutionManagement {
 repositories { mavenLocal(); google(); mavenCentral() }
 versionCatalogs { create("libs") { from(files("../../gradle/libs.versions.toml")) } }
}
providers.gradleProperty("creativeLibrarySource").orNull?.let { includeBuild(it) }
rootProject.name="creative-consumer-check"
include(":engine-only",":compose-ui")
