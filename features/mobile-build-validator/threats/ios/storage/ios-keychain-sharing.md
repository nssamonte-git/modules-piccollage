---
id: ios-keychain-sharing
title: Keychain Sharing / Sync Mistakes
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-STORAGE, M9:2024]
cwe: [CWE-200, CWE-732]
severity: medium
last_verified: 2026-10-29
aliases: [keychain-access-group-mistakes, keychain-icloud-sync]
---

# Keychain Sharing / Sync Mistakes

## Why It Is a Threat

Keychain access groups (the `keychain-access-groups` entitlement, or an app group used as an access group) share items with every app from the same team that holds the group. A group that is too broad, or items written to a shared group by default, can expose secrets to more apps and extensions than intended. Setting `kSecAttrSynchronizable` syncs an item through iCloud Keychain to all of the user's devices, which widens exposure. This card is separate from accessibility-class issues (`ios-keychain-accessibility`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the Keychain access groups and app-group entitlements of every app and extension. Check which group each sensitive item is written to: by default it goes to the first group in the list.
- Review `kSecAttrSynchronizable` on sensitive items. Confirm that only the intended apps share items and that high-risk secrets do not sync.

## How to Fix

- Limit shared access groups to the apps and extensions that must share. Set `kSecAttrAccessGroup` explicitly, and keep app-private secrets in the app's own group.
- Do not set `kSecAttrSynchronizable` for device-bound secrets. Use a `ThisDeviceOnly` accessibility class, which cannot sync.
- Review entitlements on every release. Map to MASVS-STORAGE-1. See `ios-entitlement-misconfig`.

## References

- [Apple: Sharing access to keychain items among a collection of apps](https://developer.apple.com/documentation/security/sharing-access-to-keychain-items-among-a-collection-of-apps)
- [Apple: kSecAttrSynchronizable](https://developer.apple.com/documentation/security/ksecattrsynchronizable)
- [Apple Platform Security: Keychain data protection](https://support.apple.com/guide/security/keychain-data-protection-secb0694df1a/web)
- [CWE-732: Incorrect Permission Assignment for Critical Resource](https://cwe.mitre.org/data/definitions/732.html)
