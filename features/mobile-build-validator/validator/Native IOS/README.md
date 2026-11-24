# Native iOS build gate

Stops Xcode builds of a Swift / Objective-C app while the source has unfixed security findings. It applies the shared rules and the iOS rules. See [RULES.md](../RULES.md) for the list.

## Install

```bash
sh "Native IOS/install.sh" /path/to/ios-app --with-cards
```

Run it against the folder that contains the `.xcodeproj`. This creates `build-validator/` there. Then wire Xcode:

1. Select the app target, open **Build Phases**, click **+**, and choose **New Run Script Phase**.
2. Drag the new phase above **Compile Sources**.
3. Uncheck **Based on dependency analysis** so it runs on every build.
4. Set the script body to:

   ```sh
   sh "${SRCROOT}/build-validator/xcode-run-script.sh"
   ```

5. In **Build Settings**, set **User Script Sandboxing** (`ENABLE_USER_SCRIPT_SANDBOXING`) to **No** for this target. The scan reads the whole source tree, which the sandbox forbids. The script stops with an explanation if sandboxing is still on.

Commit `build-validator/`, `.build-validator-ignore`, and the project file change.

## How it blocks the build

The phase runs the scanner with Xcode output. Blocking findings appear as errors in the Issue navigator at the exact file and line, and the build fails. Warnings appear as Xcode warnings. The JSON report goes to `$DERIVED_FILE_DIR/build-validator-report.json`.

For CI and fastlane, the phase already runs inside `xcodebuild` and `gym`. To fail before any compile work, add this to the Fastfile:

```ruby
before_all do
  sh("sh ../build-validator/validate-build.sh")
end
```

## What it checks

- **Info.plist**: ATS disabled or weakened, `NSAllowsArbitraryLoadsInWebContent`, custom URL schemes, Always-location requests, empty permission strings, extension activation rules.
- **Entitlements**: `get-task-allow`, wildcard keychain groups and associated domains, development APNs, missing default Data Protection.
- **project.pbxproj**: `DEBUG` defined in Release, unstripped or unoptimized Release builds.
- **Swift and Objective-C**: weak Keychain accessibility classes, iCloud-synced Keychain items, `NSFileProtectionNone`, credentials in UserDefaults / `@AppStorage` / App Group defaults / logs, trust accepted without `SecTrustEvaluateWithError`, WKWebView file-access flags and JavaScript string interpolation, `UIWebView`, LocalAuthentication used as a boolean gate, keys created outside the Secure Enclave, pasteboard leaks, ECB / DES / MD5, hard-coded keys.
- **Repository**: committed `.p12` / `.p8` files, provisioning profiles, fastlane passwords, provider tokens, secrets in `.xcconfig`.

`Pods/`, `DerivedData/`, `build/`, and `*Tests` / `*UITests` folders are not scanned.

## Exceptions

See "Accepting a finding" in the [main README](../README.md#accepting-a-finding). Rule ids start with `IOS-` or `SH-`. In a plist, put the suppression in an XML comment on the line above the `<key>`.
