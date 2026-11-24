// The build validator as a Gradle plugin (id "noel.libs.build-validator"). A standalone build so
// apps can use it straight from source: pluginManagement { includeBuild("<library>/build-validator-plugin") }.
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-validator-plugin"
