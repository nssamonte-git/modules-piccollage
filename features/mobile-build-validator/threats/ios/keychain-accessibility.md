---
id: ios-keychain-accessibility
title: Weak iOS Keychain Accessibility Class
platforms: [ios]
owasp_refs: [MASVS-STORAGE, M9:2024]
cwe: [CWE-922]
severity: medium
last_verified: 2026-10-29
---

# Weak iOS Keychain Accessibility Class

## Why It Is a Threat

Each Keychain item has an accessibility class that controls when it can be read and whether it can leave the device. The two risks:

- **Readable while locked.** `kSecAttrAccessibleAlways` and `AlwaysThisDeviceOnly` are deprecated since iOS 12, but legacy code still uses them. `AfterFirstUnlock` keeps secrets readable while the device is locked, once it has been unlocked after boot.
- **Migrating to other devices.** Classes without the `ThisDeviceOnly` suffix, including the default `WhenUnlocked`, move to a new device through encrypted backups.

Both widen exposure on lost, stolen, or cloned devices.

## How to Find or Test

Authorized testing only, on an owner-provided build and an authorized test device (follow the `smx-ai-vapt` authorization gate).

- Static: inspect `SecItemAdd`/`SecItemUpdate` calls for `kSecAttrAccessible`. Flag `Always`/`AlwaysThisDeviceOnly`, a missing attribute (default `WhenUnlocked`, which migrates), and `AfterFirstUnlock` used for secrets that are not needed in the background.
- Dynamic: on an authorized jailbroken test device, dump the Keychain (for example with objection) after the key flows. Check each item's class and whether it can be read while the device is locked.
- Check whether sensitive items are bound to user presence or biometrics through `SecAccessControl`.

## How to Fix

- Use the least-exposed class that still works, usually `kSecAttrAccessibleWhenUnlockedThisDeviceOnly`. Use `WhenPasscodeSetThisDeviceOnly` for the highest-value secrets.
- Use `AfterFirstUnlockThisDeviceOnly` only for items that background work needs. Never use `Always`.
- Use the `ThisDeviceOnly` variants for device-bound secrets (tokens, keys), so restoring a backup on another device cannot recover them.
- Protect sensitive items with `SecAccessControl` (`.biometryCurrentSet` or `.userPresence`), and keep server authorization independent of that local check. Map to MASVS-STORAGE-1. See `ios-keychain-sharing`.

## References

- [Apple: Restricting keychain item accessibility](https://developer.apple.com/documentation/security/restricting-keychain-item-accessibility)
- [Apple Platform Security: Keychain data protection](https://support.apple.com/guide/security/keychain-data-protection-secb0694df1a/web)
- [OWASP Mobile Top 10 2024 M9: Insecure Data Storage](https://owasp.org/www-project-mobile-top-10/2023-risks/m9-insecure-data-storage)
- [CWE-922: Insecure Storage of Sensitive Information](https://cwe.mitre.org/data/definitions/922.html)
