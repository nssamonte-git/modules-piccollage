---
id: hardcoded-encryption-keys
title: Hard-Coded Encryption Keys
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-CRYPTO]
cwe: [CWE-321, CWE-798]
severity: high
last_verified: 2026-10-29
---

# Hard-Coded Encryption Keys

## Why It Is a Threat

Encryption/signing keys embedded in source, config, or client binaries are extractable by anyone with the code or app, defeating the encryption entirely. Especially severe in mobile/client binaries, where obfuscation only slows extraction. Related: `secrets-leakage`, `api-key-theft`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Search source, configs, and mobile IPA/APK for embedded keys/secrets (string literals, byte arrays, assets); check whether a single shared key protects all data or all installs.
- Source review by stack (`source-code-vapt`): iOS keys in Swift constants instead of CryptoKit with Keychain storage; React Native encryption keys stored next to the data in JS.

## How to Fix

- Generate keys at runtime and keep them in a KMS/HSM or the platform keystore (Android Keystore, ideally hardware-backed; iOS Keychain/Secure Enclave) as non-exportable keys; never embed in code or clients.
- Where a secret must stay hidden from the device holder, keep the operation server-side. Derive per-user/per-record keys where possible. Map to ASVS 5.0 V11 Cryptography (11.1.1) and V13 Configuration (13.3.1), MASVS-CRYPTO-2, and `improper-key-management`.

## References

- [CWE-321: Use of Hard-coded Cryptographic Key](https://cwe.mitre.org/data/definitions/321.html)
- [OWASP MASVS-CRYPTO](https://mas.owasp.org/MASVS/06-MASVS-CRYPTO/)
- [OWASP Key Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Key_Management_Cheat_Sheet.html)
- [Android Keystore system](https://developer.android.com/privacy-and-security/keystore)
