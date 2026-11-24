---
id: android-pendingintent-hijacking
title: PendingIntent Hijacking
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0381]
cwe: [CWE-927, CWE-200]
severity: high
last_verified: 2026-10-29
aliases: [mutable-pendingintent-abuse]
---

# PendingIntent Hijacking

## Why It Is a Threat

A mutable `PendingIntent` whose base Intent leaves the component, package, or action unset lets the receiving app fill in those fields and fire it with your app's identity and permissions, reaching non-exported components or gaining URI access. A `PendingIntent` without `FLAG_ONE_SHOT` can also be replayed to repeat actions meant to happen once.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review `PendingIntent` creation (MASTG-TEST-0381) for `FLAG_MUTABLE`, implicit base Intents, and missing `FLAG_ONE_SHOT`, especially those handed to other apps through notifications, widgets, alarms, or other IPC; test whether a recipient can alter or replay them.

## How to Fix

- Use `FLAG_IMMUTABLE` by default with an explicit base Intent. Apps targeting Android 12+ must declare mutability, and apps targeting Android 14+ get an exception for a mutable `PendingIntent` with an implicit Intent.
- Use `FLAG_MUTABLE` only when required (for example inline reply) with a fully specified component and package; add `FLAG_ONE_SHOT` for single-use actions.
- Do not grant your background-activity-launch privileges to a `PendingIntent` unless needed. Map to MASVS-PLATFORM.

## References

- [Android Developers: Pending intents](https://developer.android.com/privacy-and-security/risks/pending-intent)
- [MASTG-TEST-0381: References to Insecure PendingIntent Creation](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0381/)
- [Android Developers: Behavior changes for apps targeting Android 12](https://developer.android.com/about/versions/12/behavior-changes-12)
- [CWE-927: Use of Implicit Intent for Sensitive Communication](https://cwe.mitre.org/data/definitions/927.html)
