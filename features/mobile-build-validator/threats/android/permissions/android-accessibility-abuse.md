---
id: android-accessibility-abuse
title: Accessibility / Notification-Listener Abuse
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-PRIVACY]
cwe: [CWE-250, CWE-200]
severity: high
last_verified: 2026-10-29
aliases: [accessibility-service-abuse, notification-listener-abuse, screen-reader-data-capture, malicious-accessibility-malware]
---

# Accessibility / Notification-Listener Abuse

## Why It Is a Threat

Malware abuses Accessibility services and Notification Listener access to read on-screen content (including your app's), capture input, inject taps and gestures, and steal OTPs from notifications. It is a leading Android banking-malware technique and runs outside your app's control. Android 15 redacts detected OTPs for untrusted notification listeners, but accessibility-based capture still works unless the app protects its views.

## How to Find or Test

Authorized testing only (resilience review; follow the `smx-ai-vapt` authorization gate).

- Assess which sensitive content and actions are exposed when an accessibility service or notification listener is active; check whether sensitive views use `accessibilityDataSensitive`, whether secrets appear in notifications, and whether the backend uses Play Integrity `appAccessRiskVerdict` for high-risk actions.

## How to Fix

- On Android 14+, mark sensitive views (login, transaction confirmation) with `accessibilityDataSensitive` so only services declared as accessibility tools can read or act on them; on Android 16 this also blocks their injected gestures. Use `FLAG_SECURE` against screen capture.
- Keep secrets and OTPs out of notification text (private visibility with a redacted public version), and use the app access risk verdict (capturing or controlling apps) to step up or block high-risk actions.
- Use phishing-resistant authentication so captured OTPs are less useful. Map to MASVS-PLATFORM; see `android-data-leakage-surfaces` and `android-overlay-tapjacking`.

## References

- [Android Developers: Tapjacking (accessibilityDataSensitive)](https://developer.android.com/privacy-and-security/risks/tapjacking)
- [Android Developers: Play Integrity integrity verdicts (app access risk)](https://developer.android.com/google/play/integrity/verdicts)
- [Android Developers: Android 15 behavior changes for all apps (OTP redaction)](https://developer.android.com/about/versions/15/behavior-changes-all)
- [MASVS-PLATFORM: Platform Interaction](https://mas.owasp.org/MASVS/09-MASVS-PLATFORM/)
