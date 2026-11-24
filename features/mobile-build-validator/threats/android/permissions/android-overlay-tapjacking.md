---
id: android-overlay-tapjacking
title: Overlay / Tapjacking Attacks
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0340]
cwe: [CWE-1021]
severity: high
last_verified: 2026-10-29
aliases: [overlay-attacks, tapjacking, screen-overlay-credential-theft]
---

# Overlay / Tapjacking Attacks

## Why It Is a Threat

A malicious app draws overlays (`SYSTEM_ALERT_WINDOW`) or sandwiches your activity under its own UI to capture taps and credentials or trick the user into unintended actions: the mobile equivalent of `clickjacking`, used for credential theft and consent fraud. Android 12+ blocks touches that pass through untrusted overlays from other apps, but sufficiently transparent overlays and partial occlusion (the touched area stays visible) are not covered by default.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Test sensitive screens (login, payment, permission or consent grants) with full and partial overlays present; check for touch filtering, obscured-flag handling, and hidden overlay windows (MASTG-TEST-0340).

## How to Fix

- Set `filterTouchesWhenObscured` on sensitive views and ignore touch events flagged `FLAG_WINDOW_IS_PARTIALLY_OBSCURED`.
- On Android 12+, hide third-party overlays on sensitive screens with `setHideOverlayWindows(true)` (`HIDE_OVERLAY_WINDOWS` permission); on Android 14+ also mark sensitive views `accessibilityDataSensitive`. Do not export activities that need not be exported.
- Check the Play Integrity app access risk verdict for overlay-capable apps before high-risk actions. Map to MASVS-PLATFORM; see `android-task-hijacking`.

## References

- [Android Developers: Tapjacking](https://developer.android.com/privacy-and-security/risks/tapjacking)
- [Android Developers: Secure sensitive activities](https://developer.android.com/security/fraud-prevention/activities)
- [MASTG-TEST-0340: References to Overlay Attack Protections](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0340/)
- [CWE-1021: Improper Restriction of Rendered UI Layers or Frames](https://cwe.mitre.org/data/definitions/1021.html)
