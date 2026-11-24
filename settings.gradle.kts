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

// Creative capabilities: engines have no Compose dependency.
module("core/creative-model")
module("core/creative-renderer")
module("core/creative-compose-common")
module("core/creative-history-engine")
module("core/creative-history-compose")
module("core/creative-documents-engine")
module("core/creative-documents-compose")
module("core/creative-canvas-engine")
module("core/creative-canvas-compose")
module("core/creative-templates-engine")
module("core/creative-templates-compose")
module("core/creative-layout-engine")
module("core/creative-layout-compose")
module("core/creative-effects-engine")
module("core/creative-effects-compose")
module("core/creative-masks-engine")
module("core/creative-masks-compose")
module("core/creative-text-engine")
module("core/creative-text-compose")
module("core/creative-export-engine")
module("core/creative-export-compose")
module("core/creative-ai-tools-engine")
module("core/creative-ai-tools-compose")
module("core/creative-ai-images-engine")
module("core/creative-ai-images-compose")
