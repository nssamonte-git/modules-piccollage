# Mobile build validator

> **Gradle apps (native Android): use the Gradle plugin instead.** Add `id("noel.libs.build-validator")` to the root `build.gradle.kts` (see the library [README](../../README.md)). This kit is for Flutter, React Native and iOS apps, or for any app where you want the scanner copied in. The plugin bundles the files in this folder.

A security gate that runs before the app compiles. It scans the project source against a library of mobile threat cards. Any **critical** or **high** finding fails the build; medium and low findings print as warnings. Each finding names its rule, the file and line, the threat card, and how to fix it.

- `validator/`: the scanner, its rules, the platform installers and hooks, and the tests. Full docs: [validator/README.md](validator/README.md). Rule list: [validator/RULES.md](validator/RULES.md).
- `threats/`: the threat cards that the rules cite. Installers copy the cards an app needs from here.

## Install

Run the installer for the app's platform from this library. It copies the scanner into `<app>/build-validator/`, creates `<app>/.build-validator-ignore`, and prints a report-only baseline scan.

| App | Command | Hooks into |
|---|---|---|
| Kotlin / Java | `sh "features/mobile-build-validator/validator/Native Android/install.sh" <app> --with-cards` | Gradle `preBuild` → `:app:validateThreats` |
| Flutter | `sh "features/mobile-build-validator/validator/Flutter/install.sh" <app> --with-cards` | Gradle in `android/app`, Xcode Run Script, optional `flutter-build.sh` |
| React Native / Expo | `sh "features/mobile-build-validator/validator/React Native/install.sh" <app> --with-cards` | Gradle, Xcode Run Script, npm pre-scripts, EAS |
| Swift / Objective-C | `sh "features/mobile-build-validator/validator/Native IOS/install.sh" <app> --with-cards` | Xcode Run Script |

`--with-cards` copies the cited threat cards into the app, so findings print the card's "How to Fix" section on any machine, including CI. `--apply-gradle` also appends the Gradle hook to the app module's build file.

## Rollout in an app (one task, in order)

1. **Install without hooks** (command above). Read the baseline.
2. **Triage.** Fix each critical or high finding using the card's "How to Fix". Record a reasoned exception only for a confirmed false positive or a risk the app owner accepts (see "Exceptions").
3. **Wire the hooks.**
   - Gradle: re-run the installer with `--apply-gradle`, or add this line yourself after the `plugins { }` block of the app module:
     - Groovy: `apply from: "$rootDir/build-validator/build-validator.gradle"`
     - Kotlin DSL: `apply(from = "$rootDir/build-validator/build-validator.gradle")`
     - Flutter and React Native use `$rootDir/../build-validator/build-validator.gradle`.
   - Xcode (iOS, Flutter, React Native): see the platform README in `validator/`.
   - CI: run `sh build-validator/validate-build.sh` as the first step, so the gate still runs if an IDE hook is removed.
4. **Verify.** `sh build-validator/validate-build.sh` exits 0, then the normal build passes (for example `./gradlew :app:assembleDebug`).
5. **Commit** `build-validator/`, `.build-validator-ignore`, and the build file change together.

Exit codes: `0` pass, `1` blocking findings, `2` configuration error.

## Exceptions

There is no skip switch. A finding goes away only when the code is fixed or a suppression with a reason is committed:

- Inline, on the line or the line above: `// build-validator:ignore AND-WV-001 reason: <why>, approved by <name>, <ticket>, expires <date>`
- In `.build-validator-ignore`: `RULE_ID<TAB>path-glob<TAB>reason` (`all` is rejected here).

Suppressions stay visible under "Suppressed" in every report. Never lower `THRESHOLD` in `build-validator/validate-build.sh`, remove a hook, or pass `--threshold none` to get a green build. Full syntax: "Accepting a finding" in [validator/README.md](validator/README.md).

## Update an app's copy

Re-run the platform installer with `--with-cards` against the app (it updates a `build-validator/` that it created). Review the new baseline and commit the refreshed folder.

## Provenance

- source: AI Knowledge and Skills Repo, folders `mobile-build-validator/` → `validator/` and `threats/` → `threats/`
- version: 2026-11-19, canonical commit `68e2d97`
- local changes: none (verbatim copies, `.DS_Store` removed)
- update: `sh features/mobile-build-validator/sync-from-canonical.sh <path to the canonical repo>` (maintainers only; the library never needs the canonical repo to work)

## Verification

```bash
sh features/mobile-build-validator/validator/tests/run-tests.sh
```

All tests pass, including "threat-index.tsv matches the cards in ../threats", which checks the copy against its own `threats/` folder.
