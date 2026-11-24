---
id: ios-backup-icloud-exposure
title: Backup / iCloud Exposure
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-STORAGE, MASTG-TEST-0215, M9:2024]
cwe: [CWE-530, CWE-312]
severity: medium
last_verified: 2026-10-29
aliases: [device-backup-leakage, icloud-backup-leakage, icloud-container-leakage]
---

# Backup / iCloud Exposure

## Why It Is a Threat

Sensitive files in the app container that are not excluded from backup end up in Finder/iTunes backups and iCloud Backup. Data stored in iCloud (CloudKit, iCloud Drive/ubiquity containers, `NSUbiquitousKeyValueStore`) also leaves the device. Anyone with access to the backup, the backup password, or the iCloud account can then read it. Without Advanced Data Protection, many iCloud categories, including iCloud Backup, use keys that Apple holds, so on-device protections no longer apply. Keychain items can also sync or migrate unintentionally (`ios-keychain-sharing`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Find sensitive files that are eligible for backup and not marked `isExcludedFromBackup` (MASTG-TEST-0215, MASTG-TEST-0298).
- Inspect an authorized test backup for secrets.
- Review what the app writes to CloudKit or other iCloud containers.

## How to Fix

- Do not keep secrets in files. Use the Keychain with `ThisDeviceOnly` and non-synchronizable attributes.
- Exclude sensitive or regenerable files from backup (`URLResourceValues.isExcludedFromBackup`), and put temporary data in `tmp/` or `Caches/`, which are not backed up. Exclusion is a hint for backup only; it does not protect the file on the device.
- Do not put secrets or unneeded personal data in iCloud containers. Encrypt with a key the app controls anything that must sync. Map to MASVS-STORAGE-2.

## References

- [MASTG-TEST-0215: Sensitive Data Not Marked For Backup Exclusion](https://mas.owasp.org/MASTG/tests/ios/MASVS-STORAGE/MASTG-TEST-0215/)
- [MASTG-TEST-0298: Runtime Monitoring of Files Eligible for Backup](https://mas.owasp.org/MASTG/tests/ios/MASVS-STORAGE/MASTG-TEST-0298/)
- [Apple Platform Security: iCloud Backup security](https://support.apple.com/guide/security/icloud-backup-security-sec2c21e7f49/web)
- [CWE-530: Exposure of Backup File to an Unauthorized Control Sphere](https://cwe.mitre.org/data/definitions/530.html)
