---
id: android-insecure-data-storage
title: Insecure Android Data Storage
category: android-storage
platforms: [android]
owasp_refs: [MASVS-STORAGE, MASTG-TEST-0207, MASTG-TEST-0200, M9:2024]
cwe: [CWE-312, CWE-922]
severity: high
last_verified: 2026-10-29
aliases: [sensitive-data-sharedpreferences, sensitive-data-datastore, sensitive-data-room-sqlite, sensitive-data-files, sensitive-data-external-storage]
---

# Insecure Android Data Storage

## Why It Is a Threat

Sensitive data stored unencrypted in SharedPreferences, DataStore, Room/SQLite, or internal files is exposed through backups, rooted or compromised devices, and debug builds. Data on external or shared storage is worse: other apps with storage access can read it (any app with storage permission before scoped storage). It falls under MASVS-STORAGE.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- After key flows, inspect `/data/data/<pkg>` (prefs, DataStore, databases, files) for secrets, tokens, and PII (MASTG-TEST-0207), and check files written to external or shared storage (MASTG-TEST-0200).

## How to Fix

- Keep secrets as Android Keystore keys (`android-keystore-misuse`) and encrypt sensitive local data with Keystore-backed keys through platform crypto APIs or a maintained library; Jetpack `security-crypto` is deprecated.
- Never put sensitive data on external or shared storage; exclude sensitive files from backup.
- Minimize what is persisted and clear it at logout. Map to MASVS-STORAGE and M9:2024.

## References

- [Android Developers: Sensitive data stored in external storage](https://developer.android.com/privacy-and-security/risks/sensitive-data-external-storage)
- [MASTG-TEST-0207: Runtime Storage of Unencrypted Data in the App Sandbox](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0207/)
- [MASTG-TEST-0200: Files Written to External Storage](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0200/)
- [OWASP Mobile Top 10 2024 M9: Insecure Data Storage](https://owasp.org/www-project-mobile-top-10/2023-risks/m9-insecure-data-storage)
- [CWE-312: Cleartext Storage of Sensitive Information](https://cwe.mitre.org/data/definitions/312.html)
