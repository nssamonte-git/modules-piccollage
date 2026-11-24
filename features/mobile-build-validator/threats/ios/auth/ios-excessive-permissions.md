---
id: ios-excessive-permissions
title: Excessive iOS Permissions / Overcollection
category: ios-auth
platforms: [ios]
owasp_refs: [MASVS-PRIVACY, MASTG-TEST-0360, M6:2024]
cwe: [CWE-359, CWE-250]
severity: medium
last_verified: 2026-10-29
aliases: [camera-microphone-location-misuse, photo-library-leakage, contact-calendar-overcollection]
---

# Excessive iOS Permissions / Overcollection

## Why It Is a Threat

An app that requests more privacy access than it needs (camera, microphone, precise or Always location, the full photo library, all contacts, full calendar access) increases its exposure and goes against Apple's privacy rules. If the app or an embedded SDK is compromised, it can harvest this data. Full photo or contact access also collects far more than the feature needs.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map each Info.plist purpose string (`NS*UsageDescription`) and each permission request to a real feature. Check that the purpose strings are accurate (MASTG-TEST-0360).
- Flag full-library, precise or Always location, full contacts, and full calendar access where a narrower option would work. Compare the app's and its SDKs' privacy manifests (`PrivacyInfo.xcprivacy`) with what the app actually collects.

## How to Fix

- Prefer access paths that need no permission or a narrower one: `PHPickerViewController` for photos, `CNContactPickerViewController` or limited contacts access, write-only calendar access (`requestWriteOnlyAccessToEvents`, iOS 17+), reduced-accuracy location, and When-In-Use instead of Always.
- Ask for access at the point of use with a clear, accurate purpose string.
- Keep accurate privacy manifests and required-reason API declarations for the app and every third-party SDK, and audit SDKs for hidden collection. Map to MASVS-PRIVACY-1 and MASVS-PRIVACY-3. See `excessive-data-collection` and `tracking-fingerprinting`.

## References

- [MASTG-TEST-0360: Purpose String Accuracy for Reachable Protected Resource Access](https://mas.owasp.org/MASTG/tests/ios/MASVS-PRIVACY/MASTG-TEST-0360/)
- [Apple: Privacy manifest files](https://developer.apple.com/documentation/bundleresources/privacy-manifest-files)
- [OWASP Mobile Top 10 2024 M6: Inadequate Privacy Controls](https://owasp.org/www-project-mobile-top-10/2023-risks/m6-inadequate-privacy-controls)
- [CWE-359: Exposure of Private Personal Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/359.html)
