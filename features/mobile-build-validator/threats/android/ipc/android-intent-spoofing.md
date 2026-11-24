---
id: android-intent-spoofing
title: Intent Spoofing / Interception
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0374, MASTG-TEST-0372]
cwe: [CWE-925, CWE-927]
severity: high
last_verified: 2026-10-29
aliases: [intent-interception, intent-hijacking, unsafe-implicit-intents]
---

# Intent Spoofing / Interception

## Why It Is a Threat

Components that trust incoming intents let a malicious app spoof intents to trigger actions, and implicit intents can be intercepted by any app with a matching intent filter, exposing the data or mutable objects they carry (tokens, PII, mutable `PendingIntent`s). The result is authorization bypass and data leakage. Builds on `android-exported-components`.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Review implicit intents that carry sensitive extras (MASTG-TEST-0374) or are used for internal communication (MASTG-TEST-0372), and components that act on intents without validation; send crafted intents from an unprivileged app and observe behavior.

## How to Fix

- Use explicit intents (`setPackage`/`setComponent`) for internal or sensitive communication; when targeting Android 14+, implicit intents only reach exported components.
- Register runtime receivers with `RECEIVER_NOT_EXPORTED`, require permissions on exported components, and validate incoming data and data returned from implicit intents. On Android 16, consider opting in to strict intent matching (`android:intentMatchingFlags="enforceIntentFilter"`).
- Keep sensitive data and mutable objects out of implicit intents. Map to MASVS-PLATFORM.

## References

- [Android Developers: Implicit intent hijacking](https://developer.android.com/privacy-and-security/risks/implicit-intent-hijacking)
- [Android Developers: Insecure broadcast receivers](https://developer.android.com/privacy-and-security/risks/insecure-broadcast-receiver)
- [MASTG-TEST-0374: References to Implicit Intents Carrying Sensitive Extras](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0374/)
- [Android Developers: Behavior changes for apps targeting Android 14](https://developer.android.com/about/versions/14/behavior-changes-14)
- [CWE-927: Use of Implicit Intent for Sensitive Communication](https://cwe.mitre.org/data/definitions/927.html)
