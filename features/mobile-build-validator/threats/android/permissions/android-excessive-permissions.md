---
id: android-excessive-permissions
title: Excessive / Misused Android Permissions
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PRIVACY, MASVS-PLATFORM, MASTG-TEST-0254, M6:2024]
cwe: [CWE-250, CWE-272]
severity: medium
last_verified: 2026-10-29
aliases: [runtime-permission-misuse]
---

# Excessive / Misused Android Permissions

## Why It Is a Threat

Requesting more permissions than needed, or dangerous ones up front, widens the attack surface and privacy exposure: a compromised app or a bundled SDK can reach contacts, location, SMS, camera, and storage beyond the app's purpose. Over-broad permissions are also a Google Play policy and user-trust risk.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map the merged manifest's permissions (including those pulled in by SDKs) to actual feature need, and flag dangerous or rarely justified permissions (MASTG-TEST-0254) and requests made before the feature is used.

## How to Fix

- Request the minimum permissions at the moment of use, handle denial gracefully, and remove unused and SDK-merged permissions (for example with `tools:node="remove"`).
- Prefer least-privilege alternatives: the photo picker and other system pickers, approximate instead of precise location, and `neverForLocation` for Bluetooth and Wi-Fi scanning.
- Map to MASVS-PRIVACY and M6:2024; see `excessive-data-collection`.

## References

- [Android Developers: Minimize your permission requests](https://developer.android.com/privacy-and-security/minimize-permission-requests)
- [MASTG-TEST-0254: Dangerous App Permissions](https://mas.owasp.org/MASTG/tests/android/MASVS-PRIVACY/MASTG-TEST-0254/)
- [OWASP Mobile Top 10 2024 M6: Inadequate Privacy Controls](https://owasp.org/www-project-mobile-top-10/2023-risks/m6-inadequate-privacy-controls)
- [CWE-272: Least Privilege Violation](https://cwe.mitre.org/data/definitions/272.html)
