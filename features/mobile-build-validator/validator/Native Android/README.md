# Native Android build gate

Stops Gradle builds of a Kotlin / Java app while the source has unfixed security findings. It applies the shared rules and the Android rules. See [RULES.md](../RULES.md) for the list.

## Install

```bash
sh "Native Android/install.sh" /path/to/android-app --apply-gradle --with-cards
```

This creates `build-validator/` in the project and adds this line to the end of `app/build.gradle` or `app/build.gradle.kts`:

```groovy
apply from: "$rootDir/build-validator/build-validator.gradle"
```

```kotlin
apply(from = "$rootDir/build-validator/build-validator.gradle")
```

Without `--apply-gradle`, add the line yourself after the `plugins { }` block. Commit `build-validator/` and `.build-validator-ignore`.

## How it blocks the build

`build-validator.gradle` registers `validateThreats` and makes `preBuild` depend on it. Every variant runs it, so `assembleDebug`, `bundleRelease`, `installDebug`, and Android Studio Run all stop on a blocking finding. The task is never up to date, so it runs on every build.

Gradle shows the findings above the failure. The JSON report goes to `app/build/reports/build-validator/report.json`.

Run it alone:

```bash
./gradlew :app:validateThreats
```

## What it checks

- **Manifest**: exported components without permissions, implicit exports, exported providers and FileProviders, custom permissions, backups, cleartext traffic, `debuggable`, task affinity, deep links, SMS permissions, Safe Browsing.
- **Gradle and R8**: debuggable or unminified release builds, non-debug build types (staging, QA, beta, UAT, preprod, internal) without R8 unless they `initWith(release)`, `-dontobfuscate`, `-dontshrink`, keep-all rules (`-keep class ** { *; }`; package-wide keeps only warn), v1-only signing, signing passwords and secrets in `buildConfigField` / `resValue`, low `minSdk`.
- **Kotlin and Java**: WebView bridges and file access, `onReceivedSslError` calling `proceed()`, trust-all TrustManagers and HostnameVerifiers, mutable PendingIntents, intent redirection, `MODE_WORLD_READABLE`, credentials in SharedPreferences or logcat, Keystore misuse, ECB / DES / MD5, hard-coded keys and IVs, SQL concatenation, dynamic code loading.
- **Resources**: FileProvider paths, backup rules, Network Security Config, password fields without a password `inputType`, secrets in `strings.xml`.
- **Repository**: committed keystores and provider tokens.

Files under `src/debug/` are skipped for debug-only settings such as cleartext and `debuggable`. `src/test` and `src/androidTest` are not scanned.

## Exceptions

See "Accepting a finding" in the [main README](../README.md#accepting-a-finding). Rule ids start with `AND-` or `SH-`.

## Troubleshooting

- **`build-validator/validate-build.sh was not found`**: the folder must sit next to the Gradle root. Re-run `install.sh`.
- **Windows**: the hook calls `bash`. Install Git for Windows with Git Bash on `PATH`, or build in WSL.
