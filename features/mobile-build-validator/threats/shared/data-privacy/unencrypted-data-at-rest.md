---
id: unencrypted-data-at-rest
title: Unencrypted Data at Rest
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-STORAGE]
cwe: [CWE-311, CWE-312]
severity: high
last_verified: 2026-10-29
---

# Unencrypted Data at Rest

## Why It Is a Threat

Sensitive data is stored without encryption in databases, files, object storage, or device storage, so disk theft, backup leakage, misconfigured buckets, or device access exposes it directly. Storage or disk encryption alone does not protect against an attacker who can query the application or database.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify where sensitive data is persisted; verify encryption at the storage level and at the field level for the most sensitive values, and where the keys live.
- On mobile, inspect local stores (see `ios-keychain-accessibility`, `sensitive-data-in-client-storage`).

## How to Fix

- Encrypt sensitive data at rest (volume or storage encryption plus field-level encryption for the most sensitive values) with approved algorithms (ASVS 5.0 V11 Cryptography); manage keys separately in a KMS or HSM (ASVS 5.0 V13.3).
- On devices, use platform protection: Keychain and Data Protection classes on iOS, Android Keystore-backed keys for app-level encryption (MASVS-STORAGE-1).

## References

- [OWASP Cryptographic Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)
- [CWE-311: Missing Encryption of Sensitive Data](https://cwe.mitre.org/data/definitions/311.html)
- [OWASP MASVS-STORAGE](https://mas.owasp.org/MASVS/05-MASVS-STORAGE/)
- [NIST SP 800-111: Guide to Storage Encryption Technologies for End User Devices](https://csrc.nist.gov/pubs/sp/800/111/final)
