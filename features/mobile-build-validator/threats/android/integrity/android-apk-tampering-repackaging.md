---
id: android-apk-tampering-repackaging
title: APK Tampering / Repackaging
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0224, M7:2024]
cwe: [CWE-354, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [apk-tampering, application-repackaging, malicious-clone-applications, signature-spoofing, resource-modification, native-library-tampering]
---

# APK Tampering / Repackaging

## Why It Is a Threat

Attackers decode, modify, and re-sign the APK (injecting ads or malware, removing protections, altering resources or native libraries, or cloning the app) and distribute it through third-party stores, sideloading links, or phishing. Users then run a trojanized copy under your brand. A repackaged app carries a different signing certificate unless the original signature scheme can be bypassed, so the signature is the main anchor for detecting it.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Repackage a test build with a changed flag or resource, re-sign it with a different key, and check whether the app detects the change and whether the backend rejects it (for example through the Play Integrity `appIntegrity` verdict).
- Check which APK signature schemes are used; a v1-only (JAR) signature does not cover the whole APK and allows modification without breaking it (MASTG-TEST-0224).

## How to Fix

- Sign with APK Signature Scheme v2 or later (v3 adds key rotation), protect the signing key, and prefer Play App Signing (`signing-key-theft`).
- Verify the app on the server: decode a Play Integrity verdict and reject requests whose `appRecognitionVerdict` is not `PLAY_RECOGNIZED` or whose certificate digest does not match. Local signature and tamper checks are defense-in-depth only.
- Enforce critical logic server-side. Map to MASVS-RESILIENCE and M7:2024; see `android-integrity-bypass`.

## References

- [MASTG-TEST-0224: Usage of Insecure APK Signature Version](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0224/)
- [Android Developers: Play Integrity integrity verdicts](https://developer.android.com/google/play/integrity/verdicts)
- [OWASP Mobile Top 10 2024 M7: Insufficient Binary Protection](https://owasp.org/www-project-mobile-top-10/2023-risks/m7-insufficient-binary-protection)
- [CWE-354: Improper Validation of Integrity Check Value](https://cwe.mitre.org/data/definitions/354.html)
