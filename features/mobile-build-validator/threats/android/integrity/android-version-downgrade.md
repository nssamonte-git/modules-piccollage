---
id: android-version-downgrade
title: App Version Downgrade
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-CODE, MASTG-TEST-0392, A03:2025]
cwe: [CWE-757, CWE-1104]
severity: medium
last_verified: 2026-10-29
aliases: [downgrade-to-vulnerable-version]
---

# App Version Downgrade

## Why It Is a Threat

An attacker installs, or tricks the user into installing, an older app version with known vulnerabilities that a later release fixed. If the backend still accepts that version, exploitation works again and client-side patches are bypassed.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check whether the backend accepts requests from old or vulnerable app versions, whether a minimum version is enforced server-side, and whether the app has an update-enforcement mechanism (MASTG-TEST-0392).

## How to Fix

- Enforce a minimum supported app version server-side and reject or force-upgrade older clients. Take the version from a verified source, such as `versionCode` in the Play Integrity `appIntegrity` verdict, not only a client header.
- Use Play in-app updates (immediate flow) for critical fixes, and raise `minSdkVersion` when old platform versions can no longer be secured (MASVS-CODE-1, MASVS-CODE-2).
- Fix vulnerabilities server-side where possible. Map to MASVS-CODE and A03:2025; see `unpatched-vulnerabilities`.

## References

- [MASTG-TEST-0392: References to Enforced Updating APIs](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0392/)
- [MASVS-CODE: Code Quality](https://mas.owasp.org/MASVS/10-MASVS-CODE/)
- [Android Developers: In-app updates](https://developer.android.com/guide/playcore/in-app-updates)
- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
