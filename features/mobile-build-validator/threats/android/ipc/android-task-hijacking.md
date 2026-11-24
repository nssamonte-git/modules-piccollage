---
id: android-task-hijacking
title: Task Hijacking (StrandHogg)
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM]
cwe: [CWE-1021]
severity: high
last_verified: 2026-10-29
aliases: [activity-launch-manipulation, strandhogg]
---

# Task Hijacking (StrandHogg)

## Why It Is a Threat

A malicious app abuses task affinity, task reparenting, or activity launches to place its activities in your app's task, so when the user opens your app they see the attacker's screen (for example a fake login). This leads to credential theft through UI confusion. The original variant was patched in 2019 and a broader variant (StrandHogg 2.0) in 2020; the second cannot be fully prevented by app configuration.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Review `minSdkVersion`, `taskAffinity`, `launchMode`, and `allowTaskReparenting`; on in-scope OS versions below Android 11, test whether another app can place activities in your task.

## How to Fix

- Where possible set `minSdkVersion` to 30 (Android 11) or higher, which includes the OS fixes for both variants.
- For older supported versions, set `taskAffinity=""` and avoid `allowTaskReparenting` as a partial mitigation. Android 15 also blocks other apps from launching arbitrary activities into your task.
- Verify sensitive actions server-side and protect sensitive screens. Map to MASVS-PLATFORM; see `android-overlay-tapjacking`.

## References

- [Android Developers: StrandHogg attack / task affinity vulnerability](https://developer.android.com/privacy-and-security/risks/strandhogg)
- [Android Developers: Behavior changes for apps targeting Android 15](https://developer.android.com/about/versions/15/behavior-changes-15)
- [MASVS-PLATFORM: Platform Interaction](https://mas.owasp.org/MASVS/09-MASVS-PLATFORM/)
- [CWE-1021: Improper Restriction of Rendered UI Layers or Frames](https://cwe.mitre.org/data/definitions/1021.html)
