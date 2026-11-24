# Android Libraries

Reusable Android modules and build tooling for your apps. It is one Gradle project: each feature is a library module that apps add as a normal dependency, and the security build gate is a Gradle plugin. The library depends on no other repository, so you can share it as it is.

## Catalog

| What | Use it in an app | Folder |
|---|---|---|
| Mobile build validator (Gradle plugin): fails the build on critical or high security findings in the source | `plugins { id("noel.libs.build-validator") }` in the root `build.gradle.kts` | [build-validator-plugin/](build-validator-plugin/) |
| Logging facade: pluggable sinks, lazy messages, Logcat sink | `implementation("noel.libs:core-logging:0.1.0")` | [core/logging/](core/logging/) |

Group `noel.libs`, version `library.version` in [gradle.properties](gradle.properties) (now `0.1.0`).

## Use it in an app

### Option 1: from source (best while you develop the library)

In the app's `settings.gradle.kts`:

```kotlin
pluginManagement {
    includeBuild("/path/to/Android Libraries/build-validator-plugin")
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}

includeBuild("/path/to/Android Libraries")
```

Then use the catalog lines above. With `includeBuild`, the plugin needs no version, and Gradle builds `noel.libs:*` dependencies from the library's source, so edits to the library show up in the app on its next build.

### Option 2: published to Maven Local

```bash
./gradlew publishToMavenLocal
```

In the app's `settings.gradle.kts`, add `mavenLocal()` first in both `pluginManagement.repositories` and `dependencyResolutionManagement.repositories`. Then use the plugin with a version, `id("noel.libs.build-validator") version "0.1.0"`, and the dependency lines above.

### Option 3: a shared Maven repository (later)

Push this repo to GitHub and add GitHub Packages (or JitPack) publishing to `build-logic`. Apps then add that repository URL instead of `mavenLocal()`. Nothing else in the apps changes.

## Build validator in an app

Apply the plugin in the app's **root** `build.gradle.kts`. Every module's `preBuild` and Kotlin compile then run `:validateThreats` first. It scans the whole project and stops the build on critical or high findings. Each finding prints the file and line, the threat card, and how to fix it. The JSON report goes to `build/reports/build-validator/report.json`.

- Run it alone: `./gradlew validateThreats`.
- Accept a finding only with a reason: inline `// build-validator:ignore <RULE-ID> reason: <why>, <ticket>`, or a line `RULE_ID<TAB>path-glob<TAB>reason` in `.build-validator-ignore` at the project root. See "Accepting a finding" in [the validator README](features/mobile-build-validator/validator/README.md).
- The plugin has no switch to skip the scan or lower the threshold.
- Flutter, React Native and iOS apps use the copy-in kit instead: [features/mobile-build-validator/FEATURE.md](features/mobile-build-validator/FEATURE.md).

## Add a module

1. Create the folder, for example `core/network/`. Register it in [settings.gradle.kts](settings.gradle.kts) with `module("core/network")`. It becomes project `:core-network`, published as `noel.libs:core-network`.
2. Copy [templates/module/](templates/module/) into the folder. Set the `namespace` (`noel.libs.<name>`) and the package.
3. Add any dependency versions to [gradle/libs.versions.toml](gradle/libs.versions.toml) (exact versions only).
4. Add a row to the catalog above. Bump `library.version` when you change a published module.
5. Check it: `./gradlew :core-network:testDebugUnitTest :sample:assembleDebug`. Use it from `sample` if it helps to see it working.

For a Compose UI module, also apply `id("noel.libs.android.compose")`.

## Build the library

Gradle runs on JDK 25 (`gradle/gradle-daemon-jvm.properties`). If Gradle can't find one, point `JAVA_HOME` at Android Studio's bundled JDK:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
```

```bash
./gradlew validateThreats testDebugUnitTest :sample:assembleRelease
```

```bash
./gradlew -p build-validator-plugin test
```

## Layout

```text
settings.gradle.kts, build.gradle.kts   the library build; the root applies the build validator to itself
gradle/libs.versions.toml               all versions
build-logic/                            convention plugins: noel.libs.android.library, noel.libs.android.compose
build-validator-plugin/                 the noel.libs.build-validator Gradle plugin (bundles the scanner below)
core/<name>/                            reusable modules
sample/                                 app that uses the modules (not published)
features/mobile-build-validator/        the validator scanner, rules and threat cards; copy-in kit for non-Gradle apps
templates/                              starting points for new modules and kits
```
