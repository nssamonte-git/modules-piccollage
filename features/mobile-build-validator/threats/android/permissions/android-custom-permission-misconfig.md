---
id: android-custom-permission-misconfig
title: Custom Permission Misconfiguration
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-CODE]
cwe: [CWE-732, CWE-280]
severity: medium
last_verified: 2026-10-29
aliases: [signature-permission-mistakes, custom-permission-race]
---

# Custom Permission Misconfiguration

## Why It Is a Threat

Custom permissions with the wrong protection level (`normal` or `dangerous` instead of `signature`), misspelled or invalid permission names (for example `android:permission="true"`), or orphaned permissions (enforced but defined by no installed app) let a malicious app define or obtain the permission first and reach your protected components. This defeats the intended app-to-app boundary.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review custom permission declarations and protection levels and the components they guard; compare every enforced permission name (manifest and runtime checks) with the declared names to find typos, invalid values, and orphaned permissions; run Android Lint; assess whether another app could define them first.

## How to Fix

- Use `signature` protection for permissions guarding sensitive inter-app access, and define every permission you enforce in the app that enforces it (or one that is always installed with it).
- Use a consistent naming scheme and Lint checks to catch typos; do not rely on custom permissions alone for security-critical boundaries.
- Verify the caller's signature or UID for high-risk operations. Map to MASVS-PLATFORM and MASVS-CODE.

## References

- [Android Developers: Custom permissions (security risks)](https://developer.android.com/privacy-and-security/risks/custom-permissions)
- [Android Developers: Define a custom app permission](https://developer.android.com/guide/topics/permissions/defining)
- [MASVS-PLATFORM: Platform Interaction](https://mas.owasp.org/MASVS/09-MASVS-PLATFORM/)
- [CWE-732: Incorrect Permission Assignment for Critical Resource](https://cwe.mitre.org/data/definitions/732.html)
