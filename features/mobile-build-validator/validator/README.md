# Mobile Build Validator

A build-time security gate for Android and iOS apps. Before the app compiles, a scanner checks the project source against the threat library. If it finds a known vulnerability at or above the blocking threshold, the build fails. The developer must fix the code or record a reasoned exception.

The rules come from the threat cards in the AI Knowledge and Skills Repo (`threats/android`, `threats/ios`, and the `threats/shared` cards they reference). Every finding names its card, the CWE and OWASP references, and the fix.

| Folder | Use it for | Hooks into |
|---|---|---|
| [Native Android](Native%20Android/) | Kotlin / Java apps | Gradle `preBuild` |
| [Native IOS](Native%20IOS/) | Swift / Objective-C apps | Xcode Run Script phase |
| [Flutter](Flutter/) | Dart plus its `android/` and `ios/` projects | Gradle, Xcode, optional `flutter` wrapper |
| [React Native](React%20Native/) | JS / TS plus `android/` and `ios/` (bare or Expo prebuild) | Gradle, Xcode, npm pre-scripts, EAS |

## Quick start

Install the gate into your project. The installer copies the scanner into `<project>/build-validator/`, prints the remaining wiring steps, and runs a report-only baseline scan.

```bash
sh "Native Android/install.sh" /path/to/android-app --apply-gradle --with-cards
```

```bash
sh "Native IOS/install.sh" /path/to/ios-app --with-cards
```

```bash
sh "Flutter/install.sh" /path/to/flutter-app --apply-gradle --with-cards
```

```bash
sh "React Native/install.sh" /path/to/rn-app --apply-gradle --with-cards
```

- `--apply-gradle` appends the `apply from:` line to the app module's `build.gradle(.kts)`.
- `--with-cards` copies only the threat cards the rules cite. Findings then print each card's "How to Fix" section on any machine, including CI.
- The Xcode Run Script phase is added by hand. Each platform README lists the four clicks.

Commit `build-validator/` and `.build-validator-ignore` so every developer and every CI build runs the same gate.

## What happens on a build

1. Gradle runs `:app:validateThreats` before `preBuild`. Xcode runs the Run Script phase before compiling.
2. The scanner walks the project and applies the rule sets for the platform. It skips build output, `node_modules`, `Pods`, `.dart_tool`, Flutter plugin symlinks, and test sources.
3. Findings print grouped by severity, each with file and line, threat card, fix, and card path. Xcode shows them in the Issue navigator.
4. Any **critical** or **high** finding fails the build (exit code 1). **Medium** and **low** findings print as warnings.

The JSON report goes to `app/build/reports/build-validator/report.json` for Gradle builds, and to `DERIVED_FILE_DIR` for Xcode builds.

## Accepting a finding

The gate has no skip switch. A finding goes away only when the code is fixed or when an exception with a reason is committed. Exceptions stay visible in every report under "Suppressed".

Inline, on the finding's line or the line above, in that language's comment syntax:

```kotlin
// build-validator:ignore AND-WV-001 reason: bridge only serves bundled offline help pages (SEC-123)
webView.addJavascriptInterface(HelpBridge(), "Help")
```

Or in `.build-validator-ignore` at the project root, with tab-separated fields:

```text
AND-ST-003	app/src/main/java/com/example/legacy/*	legacy export path, removal tracked in SEC-142
```

How an inline marker is read:

- It must start a comment (`//`, `/*`, `{/*`, `<!--`, `#`, or a ` * ` line inside a block comment). A marker inside a string literal does not count.
- On the line above the finding it must be the whole line. A comment at the end of a code line covers only that line.
- List the rule ids right after the marker (`AND-WV-001, AND-WV-007`). Ids or `all` that appear later, inside the reason, do not count. `all` is accepted inline (it covers that one line) but never in `.build-validator-ignore`, where a single entry would switch the gate off for the whole project.
- The reason ends at the first comment closer (`*/` or `-->`); code after the closer is not a reason.
- A line that holds a lone carriage return or a Unicode line separator cannot carry a marker: the compiler sees two lines there.

In `.build-validator-ignore`, the path glob knows only `*` (any characters, `/` included) and `?`; every other character is literal. The file is read only when it is a regular file, not a symlink.

Both forms need a reason with at least one letter or digit, in any common script; spaces, punctuation, symbols, emoji and invisible characters do not count, and a ticket id such as `SEC-123` always does. A suppression without one (or without `reason:`) is not accepted: the finding stays active and every report lists it under "Ignored suppressions". Changing `THRESHOLD` in `build-validator/validate-build.sh` is also possible, but it is a code change that shows up in review.

## Running the scanner by hand

```bash
sh build-validator/validate-build.sh
```

Useful options:

| Option | Effect |
|---|---|
| `--format json` / `markdown` / `xcode` / `ids` | Output for CI, pull-request comments, or scripting |
| `--report <file>` | Also write the JSON report |
| `--threshold none` | Report only, never block (used by the installer's baseline) |
| `--include-tests` | Also scan test source sets |
| `--list-rules` | Print the active rules |

Exit codes: `0` pass, `1` blocking findings, `2` configuration error.

## Rules

[RULES.md](RULES.md) lists all 160 rules: 15 shared, 77 Android, 47 iOS, 8 Flutter and 13 React Native. Each row links its threat card.

- Rules are pattern and structure checks. They cover manifest components and attributes, Gradle build types, plist and entitlement keys, Release build settings, and API calls in source.
- Rules marked *heuristic* can misfire. They are set one level below the card severity, so they warn instead of blocking.
- Runtime-only threats are not checked as code, such as a rooted or jailbroken device, hooking, or sideloaded malware. The scanner only reports when no integrity defense (Play Integrity, App Attest, RASP) exists anywhere.
- The scanner reads source, not the built APK or IPA. It does not scan dependencies for CVEs and does not see manifest entries merged in from libraries.
- It matches bytes (C locale), so source files in any encoding are scanned. A scanned file whose name contains a tab, carriage return or newline stops the scan with exit code `2`: such names would split its file list.

## Repository layout

```text
core/threat-scan.sh            scanner engine (POSIX sh, grep, awk)
core/lib/                      report formats and structural checks (manifest, plist, brace and call scope)
core/rules/*.rules             rule catalog, tab-separated, one rule per line
core/rules/threat-index.tsv    threat id -> title, severity, CWE, OWASP, card path
core/tools/                    installer, threat-index builder, rule generator
<platform folder>/             wrapper, build hooks, install.sh, README
tests/                         fixture projects and the test harness
```

## Updating the rules

When the threat library changes, or to add a rule:

1. Rebuild the threat index: `sh core/tools/build-threat-index.sh "<path to>/threats"`.
2. Edit the rule definitions in `core/tools/generate-rules.py`, then run `python3 core/tools/generate-rules.py`. This rewrites `core/rules/*.rules` and `RULES.md`.
3. Add a violation to the matching `tests/fixtures/<platform>/bad/` project and its id to `EXPECTED.txt`.
4. Run `sh tests/run-tests.sh`. It checks that every rule fires on a bad fixture, every clean fixture passes, every rule cites an existing card, the threat index matches the cards in `../threats`, suppressions and file names cannot hide findings, and the JSON report parses.
5. Re-run the platform `install.sh` in each project to update its copy.

## Requirements

- macOS or Linux with `sh`, `grep`, `awk`, `sed` and `find`. These are present on every Xcode and Android build machine.
- Windows needs Git Bash or WSL. The Gradle hook calls `bash` on Windows.
- Python 3 is needed only to regenerate rules, not to scan.
