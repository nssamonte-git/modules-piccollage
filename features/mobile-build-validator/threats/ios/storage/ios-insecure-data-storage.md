---
id: ios-insecure-data-storage
title: Insecure iOS Data Storage
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-STORAGE, MASTG-TEST-0302, M9:2024]
cwe: [CWE-312, CWE-922]
severity: high
last_verified: 2026-10-29
aliases: [sensitive-data-userdefaults, sensitive-data-plist, sensitive-data-sqlite-coredata, sensitive-data-files-ios]
---

# Insecure iOS Data Storage

## Why It Is a Threat

UserDefaults, plist files, SQLite/Core Data/SwiftData stores, caches (`URLCache`, web data), and ordinary files have no encryption at the app level. They rely only on the default Data Protection class, which keeps files readable after the first unlock. That data can be read by anyone with device access, from backups, or through forensic extraction. UserDefaults is a plain plist under `Library/Preferences`. Insecure storage is among the most common iOS weaknesses (M9:2024).

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- After the key flows, inspect the app sandbox (UserDefaults, plists, databases, caches, files) for secrets, tokens, and PII (MASTG-TEST-0302).
- Review code for APIs that write unencrypted data to private storage (MASTG-TEST-0300).

## How to Fix

- Keep secrets in the Keychain (`ios-keychain-accessibility`).
- Encrypt sensitive local data with a key held in the Keychain or Secure Enclave, and apply a strict Data Protection class (`ios-weak-data-protection`).
- Store only what you need, and clear caches of sensitive responses.
- Never store tokens or PII in UserDefaults or plaintext files. Map to MASVS-STORAGE-1.

## References

- [MASTG-TEST-0302: Sensitive Data Unencrypted in Private Storage Files](https://mas.owasp.org/MASTG/tests/ios/MASVS-STORAGE/MASTG-TEST-0302/)
- [MASTG-TEST-0300: References to APIs for Storing Unencrypted Data in Private Storage](https://mas.owasp.org/MASTG/tests/ios/MASVS-STORAGE/MASTG-TEST-0300/)
- [OWASP Mobile Top 10 2024 M9: Insecure Data Storage](https://owasp.org/www-project-mobile-top-10/2023-risks/m9-insecure-data-storage)
- [CWE-312: Cleartext Storage of Sensitive Information](https://cwe.mitre.org/data/definitions/312.html)
