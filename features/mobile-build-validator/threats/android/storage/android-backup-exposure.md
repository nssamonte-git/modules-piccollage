---
id: android-backup-exposure
title: Android Backup Exposure
category: android-storage
platforms: [android]
owasp_refs: [MASVS-STORAGE, MASTG-TEST-0262, MASTG-TEST-0216, M8:2024]
cwe: [CWE-530, CWE-312]
severity: medium
last_verified: 2026-10-29
aliases: [unencrypted-application-backups, android-backup-extraction]
---

# Android Backup Exposure

## Why It Is a Threat

Auto Backup is on by default (`allowBackup` defaults to `true`), so app files and databases go to cloud backup and device-to-device (D2D) transfer unless excluded. `adb backup` can still extract data from apps targeting Android 11 or lower, and custom "export" features that write copies to shared storage leak data to other apps. Anyone with device, backup, or transfer access then bypasses in-app protections.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Check `allowBackup`, `dataExtractionRules` (Android 12+), and `fullBackupContent` (Android 11 and lower) for exclusions of sensitive files, including D2D rules (MASTG-TEST-0262).
- Trigger a backup on a test device and inspect it for tokens, keys, and PII (MASTG-TEST-0216).

## How to Fix

- Exclude sensitive files (tokens, keys, databases with PII) in both rule formats, including the `<device-transfer>` section, or set `allowBackup="false"` for high-risk apps (some devices still allow D2D transfer).
- Keep secrets as non-exportable Android Keystore keys so restored data is useless elsewhere, and avoid custom backup or export features that write to shared storage.
- Encrypt anything that must be backed up. Map to MASVS-STORAGE and M8:2024.

## References

- [Android Developers: Security recommendations for backups](https://developer.android.com/privacy-and-security/risks/backup-best-practices)
- [MASTG-TEST-0262: References to Backup Configurations Not Excluding Sensitive Data](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0262/)
- [Android Developers: Behavior changes for apps targeting Android 12](https://developer.android.com/about/versions/12/behavior-changes-12)
- [CWE-530: Exposure of Backup File to an Unauthorized Control Sphere](https://cwe.mitre.org/data/definitions/530.html)
