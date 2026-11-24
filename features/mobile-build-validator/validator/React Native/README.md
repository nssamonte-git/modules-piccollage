# React Native build gate

Stops React Native builds while the project has unfixed security findings. One scan covers the JS / TS code, Expo config, and the native `android/` and `ios/` projects. It applies the shared, React Native, Android and iOS rules. See [RULES.md](../RULES.md) for the list.

## Install

```bash
sh "React Native/install.sh" /path/to/rn-app --apply-gradle --with-cards
```

Run it against the folder with `package.json`. This creates `build-validator/` there and adds the Gradle hook to `android/app/build.gradle`:

```groovy
apply from: "$rootDir/../build-validator/build-validator.gradle"
```

Then:

1. **Xcode**: in `ios/<App>.xcworkspace`, on the app target, add a Run Script phase above **Compile Sources**. Uncheck **Based on dependency analysis** and set **User Script Sandboxing** to **No**. Use this body:

   ```sh
   sh "${SRCROOT}/../build-validator/xcode-run-script.sh"
   ```

2. **npm scripts**: merge `build-validator/package-scripts.json` into the `scripts` block of `package.json`. `npm run android` and `npm run ios` then run the gate first.

Commit `build-validator/`, `.build-validator-ignore`, and the project changes.

## How it blocks the build

The Gradle and Xcode hooks run inside every native build. That covers `npx react-native run-android|run-ios`, `npx expo run:*`, Android Studio, Xcode, and CI. The npm pre-scripts fail even earlier. React Native's debug manifest in `src/debug/` (cleartext for Metro) and the template `debug.keystore` are not reported.

**Expo (managed / CNG)**: `expo prebuild` regenerates `android/` and `ios/`, which removes hand-made hooks. Rely on the `eas-build-post-install` script from `package-scripts.json` on EAS, and run `npm run validate:security` in CI before building.

## What it checks

- **JS / TS**: credentials in AsyncStorage / MMKV, WebView `allowFileAccess*`, `mixedContentMode="always"`, `originWhitelist={['*']}`, interpolated `injectJavaScript`, `onMessage` without an origin check, disabled TLS validation, credentials in `console.*`, deep-link handlers without URL validation, CryptoJS ECB / DES / MD5 / literal passphrases, cleartext URLs, hard-coded secrets.
- **Expo config**: `EXPO_PUBLIC_*` variables that carry secrets, `usesCleartextTraffic` / `NSAllowsArbitraryLoads`, `allowBackup`.
- **Obfuscation**: Hermes turned off (`hermesEnabled=false`, `enableHermes: false`, Expo `"jsEngine": "jsc"`) and `enableProguardInReleaseBuilds = false` block the build.
- **android/ and ios/**: every native Android and iOS rule (see the Native Android and Native IOS READMEs).

`node_modules/`, `__tests__/`, build outputs and `Pods/` are not scanned. Local `.env` files that git ignores are not reported for generic secrets. `EXPO_PUBLIC_*` secrets are always reported, because they ship inside the app bundle.

## Exceptions

See "Accepting a finding" in the [main README](../README.md#accepting-a-finding). React Native rule ids start with `RN-`.
