---
id: sensitive-data-in-client-storage
title: Sensitive Data in Client Storage and Surfaces
category: data-privacy
platforms: [shared, web, android, ios]
owasp_refs: [MASVS-STORAGE, MASVS-PLATFORM]
cwe: [CWE-922, CWE-312, CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [sensitive-data-in-backups, sensitive-data-in-temp-files, sensitive-data-in-caches, sensitive-data-in-notifications, sensitive-data-in-screenshots, sensitive-data-in-clipboard]
---

# Sensitive Data in Client Storage and Surfaces

## Why It Is a Threat

Sensitive data lands in unprotected client sinks — device and cloud backups, temp files, HTTP and WebView caches, browser storage (localStorage, IndexedDB), lock-screen notifications, app-switcher screenshots, and the clipboard/pasteboard — where other apps, device access, backup extraction, or shoulder-surfing can read it. Especially impactful on mobile.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on an owner-authorized device.

- After key flows, inspect backups, caches, temp files, browser storage, notification content, the app-switcher snapshot, and the clipboard for sensitive values (see `ios-keychain-accessibility`).
- Review Android backup settings (`allowBackup`, data extraction rules) and iOS backup exclusion for sensitive files.
- Source review by stack (`source-code-vapt`): React Native `AsyncStorage`, `redux-persist`, MMKV without `encryptionKey`; Flutter `shared_preferences`, Hive without `HiveAesCipher`; Flask's default signed-cookie `session` holding sensitive values.

## How to Fix

- Keep secrets in Keychain/Keystore; exclude sensitive files from backups (Android data extraction rules, iOS `isExcludedFromBackup`); clear caches and temp files; hide sensitive content in lock-screen notifications.
- Hide sensitive screens from the app switcher and screenshots (Android `FLAG_SECURE`, an iOS privacy overlay when the scene enters the background); mark sensitive clipboard items as sensitive (Android 13+) or local-only with an expiry (iOS), and avoid copying secrets where possible (MASVS-STORAGE-2).
- On the web, send `Cache-Control: no-store` for sensitive responses, keep sensitive data out of browser storage, and clear it at logout (ASVS 5.0 V14.3).

## References

- [OWASP MASVS-STORAGE](https://mas.owasp.org/MASVS/05-MASVS-STORAGE/)
- [Android Developers: Security recommendations for backups](https://developer.android.com/privacy-and-security/risks/backup-best-practices)
- [Android Developers: Copy and paste (sensitive content)](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste)
- [CWE-922: Insecure Storage of Sensitive Information](https://cwe.mitre.org/data/definitions/922.html)
