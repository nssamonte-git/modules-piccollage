# Flutter build gate

Stops Flutter builds while the project has unfixed security findings. One scan covers the Dart code and the native `android/` and `ios/` projects. It applies the shared, Flutter, Android and iOS rules. See [RULES.md](../RULES.md) for the list.

## Install

```bash
sh "Flutter/install.sh" /path/to/flutter-app --apply-gradle --with-cards
```

Run it against the folder with `pubspec.yaml`. This creates `build-validator/` there and adds the Gradle hook to `android/app/build.gradle(.kts)`:

```kotlin
apply(from = "$rootDir/../build-validator/build-validator.gradle")
```

Then wire Xcode in `ios/Runner.xcworkspace`, on the **Runner** target:

1. Open **Build Phases**, click **+**, and choose **New Run Script Phase**.
2. Drag it above **Compile Sources** and uncheck **Based on dependency analysis**.
3. Set the body to:

   ```sh
   sh "${SRCROOT}/../build-validator/xcode-run-script.sh"
   ```

4. Set **User Script Sandboxing** to **No** for Runner.

Commit `build-validator/`, `.build-validator-ignore`, and the two project changes.

## How it blocks the build

`flutter run` and `flutter build apk|appbundle|ios|ipa` compile through Gradle and Xcode, so both hooks fire. Findings print in the Flutter output and the build stops.

Android release builds must be obfuscated. The Gradle hook fails any `assemble*Release` / `bundle*Release` task unless Flutter passed `--obfuscate --split-debug-info=<dir>`. Keep that symbols folder with each release for crash symbolication.

For CI, `flutter-build.sh` runs the gate first, checks the same obfuscation flags for `build apk|appbundle`, then runs Flutter with the same arguments:

```bash
sh build-validator/flutter-build.sh build appbundle --release --obfuscate --split-debug-info=build/symbols
```

## What it checks

- **Dart**: `badCertificateCallback` / `validateCertificate` returning true, WebView TLS errors set to PROCEED, credentials in SharedPreferences / Hive / GetStorage, WebView file access and mixed content, JavaScript channels, `runJavaScript` with string interpolation, credentials in `print` / `debugPrint` / `log`, ECB mode, hard-coded `Key` / `IV`, MD5, cleartext URLs, hard-coded secrets.
- **android/ and ios/**: every native Android and iOS rule (see the Native Android and Native IOS READMEs).

`build/`, `.dart_tool/`, `ios/.symlinks/` (plugin sources), `ios/Pods/`, and `test/` are not scanned. The minify rule is skipped for Flutter's Gradle plugin, which shrinks release builds by default.

## Exceptions

See "Accepting a finding" in the [main README](../README.md#accepting-a-finding). Dart rule ids start with `FL-`.
