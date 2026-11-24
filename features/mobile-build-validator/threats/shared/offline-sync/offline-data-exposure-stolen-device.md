---
id: offline-data-exposure-stolen-device
title: Data Exposure from Stolen Devices
category: offline-sync
platforms: [shared, android, ios]
owasp_refs: [MASVS-STORAGE]
cwe: [CWE-312, CWE-311]
severity: high
last_verified: 2026-10-29
---

# Data Exposure from Stolen Devices

## Why It Is a Threat

Offline apps cache substantial data locally so the app works without connectivity; on a lost/stolen device that cached data (and any cached credentials or refresh tokens) is exposed if it is unencrypted, protected by a weak file protection class, or guarded only by an in-app gate. The offline model increases the local data footprint at risk and the time it stays valid.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on an owner-authorized device.

- Inspect the offline cache/DB for sensitive data, its encryption, and the iOS Data Protection class or Android storage location used.
- Assess what is recoverable from a locked device or a device backup, and whether remote session revocation and wipe exist.

## How to Fix

- Encrypt offline data with hardware-backed keys that require user authentication (Android Keystore, iOS Keychain and Secure Enclave); use the strongest iOS Data Protection class the offline use case allows; minimize cached sensitive data and expire it (MASVS-STORAGE-1).
- Support remote revocation of tokens and wiping of local data on next launch or sync; require re-authentication after inactivity (MASVS-AUTH-2). See `android-insecure-data-storage`, `ios-insecure-data-storage`, `stolen-privileged-devices`.

## References

- [OWASP MASVS-STORAGE](https://mas.owasp.org/MASVS/05-MASVS-STORAGE/)
- [Apple Platform Security: Data Protection overview](https://support.apple.com/guide/security/data-protection-overview-secf6276da8a/web)
- [Android Developers: Android Keystore system](https://developer.android.com/privacy-and-security/keystore)
- [CWE-312: Cleartext Storage of Sensitive Information](https://cwe.mitre.org/data/definitions/312.html)
