---
id: ios-weak-data-protection
title: Weak Data Protection Classes
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-STORAGE, MASTG-TEST-0299, M9:2024]
cwe: [CWE-311, CWE-312]
severity: medium
last_verified: 2026-10-29
---

# Weak Data Protection Classes

## Why It Is a Threat

iOS Data Protection ties file encryption keys to the device's lock state. Third-party app files get `CompleteUntilFirstUserAuthentication` by default, which keeps them readable while the device is locked once it has been unlocked after boot. `NoProtection` is weaker still. Leaving sensitive files in these classes leaves them readable on a lost or stolen device that is locked but has been unlocked since boot.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the `FileProtectionType`/`NSFileProtection*` attributes and `Data.write(options:)` protection options for sensitive files. Check the app's default class (the `com.apple.developer.default-data-protection` entitlement).
- On an authorized device, list the protection class of each file (MASTG-TEST-0299) and check whether sensitive data can be read while the device is locked.

## How to Fix

- Apply `.complete` to sensitive files so they cannot be read while the device is locked. Use `.completeUnlessOpen` for files that must be written in the background while locked.
- Set the Data Protection entitlement to `NSFileProtectionComplete` to raise the app-wide default, and pick the strictest class each feature allows.
- Data Protection only covers the device's lock state. Keep secrets in the Keychain and add app-level encryption where needed. Map to MASVS-STORAGE-1.

## References

- [MASTG-TEST-0299: Data Protection Classes for Files in Private Storage](https://mas.owasp.org/MASTG/tests/ios/MASVS-STORAGE/MASTG-TEST-0299/)
- [Apple Platform Security: Data Protection classes](https://support.apple.com/guide/security/data-protection-classes-secb010e978a/web)
- [Apple: Data Protection Entitlement](https://developer.apple.com/documentation/bundleresources/entitlements/com.apple.developer.default-data-protection)
- [CWE-311: Missing Encryption of Sensitive Data](https://cwe.mitre.org/data/definitions/311.html)
