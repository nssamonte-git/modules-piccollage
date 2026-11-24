import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
}

group = "noel.libs"
// One version for the whole library: library.version in ../gradle.properties.
version = providers.fileContents(layout.projectDirectory.file("../gradle.properties")).asText.map { text ->
    text.lineSequence().map { it.trim() }.first { it.startsWith("library.version=") }.substringAfter('=').trim()
}.get()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        register("buildValidator") {
            id = "noel.libs.build-validator"
            implementationClass = "noel.libs.buildvalidator.BuildValidatorPlugin"
            displayName = "Mobile build validator"
            description = "Fails Android builds on critical or high security findings in the project source."
        }
    }
}

// The scanner is bundled into the plugin jar from the kit in this library, so the plugin needs
// nothing else at build time: the engine, the shared and Android rules, and only the threat cards
// those rules cite (for the "How to fix" text).
val kit = layout.projectDirectory.dir("../features/mobile-build-validator")
val ruleSets = listOf("shared", "android")
val citedCards: List<String> = ruleSets
    .flatMap { kit.file("validator/core/rules/$it.rules").asFile.readLines() }
    .filterNot { it.isBlank() || it.trimStart().startsWith("#") }
    .mapNotNull { it.split('\t').getOrNull(7)?.trim() }
    .filter { it.endsWith(".md") }
    .distinct()
    .sorted()

val engineRoot = layout.buildDirectory.dir("validator-resources")
val bundleValidator = tasks.register<Sync>("bundleValidator") {
    into(engineRoot.map { it.dir("noel/libs/buildvalidator/engine") })
    from(kit.dir("validator/core")) {
        include("threat-scan.sh", "lib/*.sh", "rules/threat-index.tsv")
        include(ruleSets.map { "rules/$it.rules" })
    }
    from(kit.dir("threats")) {
        include(citedCards)
        into("threats")
    }
}

// files.txt lists every bundled file, so the plugin can extract them at build time.
val manifestRoot = layout.buildDirectory.dir("validator-manifest")
val engineManifest = tasks.register("engineManifest") {
    inputs.files(bundleValidator)
    val engineDir = engineRoot.map { it.dir("noel/libs/buildvalidator/engine").asFile }
    val out = manifestRoot.map { it.file("noel/libs/buildvalidator/engine-files.txt") }
    outputs.file(out)
    doLast {
        val dir = engineDir.get()
        val files = dir.walkTopDown().filter { it.isFile }.map { it.relativeTo(dir).invariantSeparatorsPath }.sorted().toList()
        check(files.contains("threat-scan.sh")) { "build validator engine missing from $dir" }
        out.get().asFile.writeText(files.joinToString("\n", postfix = "\n"))
    }
}

sourceSets.main {
    resources.srcDir(files(engineRoot).builtBy(bundleValidator))
    resources.srcDir(files(manifestRoot).builtBy(engineManifest))
}

tasks.test {
    systemProperty("fixturesDir", kit.dir("validator/tests/fixtures/android").asFile.absolutePath)
}
