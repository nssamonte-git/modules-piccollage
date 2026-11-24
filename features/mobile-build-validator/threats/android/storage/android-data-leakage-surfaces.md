---
id: android-data-leakage-surfaces
title: Android Data-Leakage Surfaces
category: android-storage
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-STORAGE, MASTG-TEST-0291, MASTG-TEST-0315]
cwe: [CWE-200, CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [clipboard-leakage-android, screenshot-leakage-android, recent-app-preview-leakage, notification-content-leakage]
---

# Android Data-Leakage Surfaces

## Why It Is a Threat

Sensitive data leaks through Android UI and system surfaces: the clipboard (readable by background apps before Android 10, and by keyboards and the foreground app after), screenshots and screen recording, the recent-apps preview, and notification content on the lock screen. Secrets are exposed without any storage bug. Platform-specific instance of `sensitive-data-in-client-storage`.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- After sensitive flows, check clipboard contents, the recent-apps thumbnail (MASTG-TEST-0289), screenshot prevention on sensitive screens (MASTG-TEST-0291), and notification and lock-screen content (MASTG-TEST-0315).
- Source review by stack (`source-code-vapt`): Jetpack Compose sensitive screens without `FLAG_SECURE`; Flutter screens without `FLAG_SECURE` on Android.

## How to Fix

- Set `FLAG_SECURE` on sensitive screens (blocks screenshots, casting, and recents previews); on Android 13+ `setRecentsScreenshotEnabled(false)` also hides the recents preview.
- Avoid copying secrets to the clipboard; when needed, flag the clip as sensitive (Android 13+ hides its preview) and clear it after use.
- Keep sensitive data out of notifications, or use private lock-screen visibility with a redacted public version. Map to MASVS-PLATFORM and MASVS-STORAGE.

## References

- [Android Developers: Secure clipboard handling](https://developer.android.com/privacy-and-security/risks/secure-clipboard-handling)
- [Android Developers: Secure sensitive activities](https://developer.android.com/security/fraud-prevention/activities)
- [MASTG-TEST-0291: References to Screen Capturing Prevention APIs](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0291/)
- [MASTG-TEST-0315: Sensitive Data Exposed via Notifications](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0315/)
- [CWE-359: Exposure of Private Personal Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/359.html)
