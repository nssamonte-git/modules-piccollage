// Top-level build file. Module settings live in the convention plugins (build-logic/convention);
// declaring the plugins here once puts them on one shared classpath.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Security gate: every module's preBuild and Kotlin compile run :validateThreats first.
    // Never remove it to get a green build; fix the finding or record a reasoned exception.
    id("noel.libs.build-validator")
}

// `./gradlew publishToMavenLocal` also publishes the build validator plugin (an included build).
tasks.register("publishToMavenLocal") {
    group = "publishing"
    description = "Publishes the build validator plugin; the modules' own publishToMavenLocal tasks run too."
    dependsOn(gradle.includedBuild("build-validator-plugin").task(":publishToMavenLocal"))
}
