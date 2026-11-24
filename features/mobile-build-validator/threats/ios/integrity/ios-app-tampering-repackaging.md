---
id: ios-app-tampering-repackaging
title: App Tampering / Re-Signing / Repackaging
category: ios-integrity
platforms: [ios]
owasp_refs: [MASVS-RESILIENCE, M7:2024]
cwe: [CWE-353, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [application-tampering, re-signing, application-repackaging, malicious-sideloading-ios]
---

# App Tampering / Re-Signing / Repackaging

## Why It Is a Threat

Attackers decrypt and modify the app, re-sign it with their own certificate, and distribute it through enterprise or developer profiles, sideloading tools, or third-party channels. This lets them add malware, strip protections, or ship a cloned app, so users run a trojanized version that still talks to your backend.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- On an authorized test build, re-sign the app with a small change. Check whether the app or the backend notices: a different team ID or entitlements, a modified bundle or resources, or failed App Attest.

## How to Fix

- Use App Attest so the server can reject requests from re-signed or modified copies. A re-signed app has a different App ID, so attestation fails.
- As defense-in-depth on the device, check the team identifier and expected entitlements, and run integrity checks on important resources (MASTG-TEST-0387 covers storage integrity checks).
- Ship only through the App Store or, in the EU, through notarized alternative distribution. Keep critical logic on the server.
- Map to MASVS-RESILIENCE-2. See `signing-key-theft` and `ios-enterprise-cert-abuse`.

## References

- [Apple Platform Security: App code signing process in iOS, iPadOS, tvOS, visionOS, and watchOS](https://support.apple.com/guide/security/app-code-signing-process-sec7c917bf14/web)
- [Apple: Establishing your app's integrity](https://developer.apple.com/documentation/devicecheck/establishing-your-app-s-integrity)
- [OWASP Mobile Top 10 2024 M7: Insufficient Binary Protection](https://owasp.org/www-project-mobile-top-10/2023-risks/m7-insufficient-binary-protection)
- [CWE-353: Missing Support for Integrity Check](https://cwe.mitre.org/data/definitions/353.html)
