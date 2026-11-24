---
id: android-intent-redirection
title: Intent Redirection
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM]
cwe: [CWE-940, CWE-441]
severity: high
last_verified: 2026-10-29
---

# Intent Redirection

## Why It Is a Threat

An exported component takes a nested Intent (from extras, or parsed from a string) supplied by another app and launches it (`startActivity`, `sendBroadcast`, `setResult`) without validation. A malicious app then uses your app's identity to reach internal, non-exported components, trigger privileged actions, or gain URI access to private data through grant flags (`confused-deputy`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Find components that read an Intent from a received Intent and launch it; check whether the target and grant flags are validated, and test whether an external app can redirect to internal components or private providers.
- Enable StrictMode `detectUnsafeIntentLaunch()` in a test build to surface unsafe launches.
- Source review by stack (`source-code-vapt`): Android `getParcelableExtra` returning an `Intent` passed to `startActivity`; Flutter `MethodChannel` handlers that build intents from Dart arguments.

## How to Fix

- Do not forward nested intents. If unavoidable, resolve the target and check it against an allowlist, sanitize with `IntentSanitizer`, and strip URI grant flags. Prefer handing out an immutable `PendingIntent` instead.
- Keep internal components non-exported. Android 16 adds default protection against intent redirection; do not opt out (`removeLaunchSecurityProtection()`) without review, and do not rely on it for older versions.
- Map to MASVS-PLATFORM.

## References

- [Android Developers: Intent redirection](https://developer.android.com/privacy-and-security/risks/intent-redirection)
- [Android Developers: Android 16 behavior changes for all apps](https://developer.android.com/about/versions/16/behavior-changes-all)
- [MASVS-PLATFORM: Platform Interaction](https://mas.owasp.org/MASVS/09-MASVS-PLATFORM/)
- [CWE-940: Improper Verification of Source of a Communication Channel](https://cwe.mitre.org/data/definitions/940.html)
