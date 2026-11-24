pluginManagement {
    // Convention plugins for the modules, and the build validator plugin apps use.
    includeBuild("build-logic")
    includeBuild("build-validator-plugin")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "android-libraries"

// Reusable modules. A module in folder core/logging is project :core-logging and is published as
// noel.libs:core-logging, so apps that includeBuild this library get it without extra setup.
fun module(dir: String) {
    val name = dir.replace('/', '-')
    include(":$name")
    project(":$name").projectDir = file(dir)
}
module("core/logging")

// A small app that uses the modules; not published.
include(":sample")
