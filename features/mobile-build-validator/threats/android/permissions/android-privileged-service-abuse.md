---
id: android-privileged-service-abuse
title: Privileged-Service Abuse (Device Admin / VPN / Media Projection)
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-PRIVACY]
cwe: [CWE-250, CWE-269]
severity: high
last_verified: 2026-10-29
aliases: [device-administrator-abuse, vpn-service-abuse, media-projection-abuse]
---

# Privileged-Service Abuse (Device Admin / VPN / Media Projection)

## Why It Is a Threat

Malware abuses powerful Android capabilities: Device Admin (lock, wipe, persistence), VpnService (route and observe all traffic), and MediaProjection (record or share the screen, capturing your app's sensitive UI). All need user consent, which is easily socially engineered, for example during a fake support call with screen sharing. Android 14 requires consent for each MediaProjection session, and Android 15 hides notifications and detected sensitive content from remote viewers during screen sharing.

## How to Find or Test

Authorized testing only (resilience review; follow the `smx-ai-vapt` authorization gate).

- Assess which sensitive UI and data are exposed while screen capture or a third-party VPN is active; for your own app, review the justification for requesting such capabilities.

## How to Fix

- Protect sensitive screens with `FLAG_SECURE` (captures show a blank window) and mark sensitive views with `setContentSensitivity` (Android 15+); detect recording with the Android 15 screen recording callback and warn or pause high-risk flows.
- Use TLS with certificate validation (and pinning where justified) so a rogue VPN cannot read traffic; check the Play Integrity app access risk verdict for screen-capturing apps.
- Do not request privileged capabilities without strong justification. Map to MASVS-PLATFORM.

## References

- [Android Developers: Secure sensitive activities](https://developer.android.com/security/fraud-prevention/activities)
- [Android Developers: Media projection](https://developer.android.com/media/grow/media-projection)
- [Android Developers: Android 15 features (screen recording detection)](https://developer.android.com/about/versions/15/features)
- [CWE-250: Execution with Unnecessary Privileges](https://cwe.mitre.org/data/definitions/250.html)
