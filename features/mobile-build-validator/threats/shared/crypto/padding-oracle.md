---
id: padding-oracle
title: Padding-Oracle Attack
category: crypto
platforms: [shared, web, backend]
owasp_refs: [WSTG-CRYP-02, A04:2025]
cwe: [CWE-209, CWE-696]
severity: high
last_verified: 2026-10-29
---

# Padding-Oracle Attack

## Why It Is a Threat

When a system reveals (via error, status, or timing) whether decryption padding was valid, an attacker can decrypt or forge ciphertext under CBC-style modes without the key, byte by byte. The same class affects RSA PKCS#1 v1.5 decryption (Bleichenbacher-style oracles).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify decryption endpoints using unauthenticated CBC or RSA PKCS#1 v1.5; submit tampered ciphertexts and observe distinguishable padding-error responses or timing.

## How to Fix

- Use authenticated encryption (AES-GCM) so tampering is detected before padding logic; with CBC, verify a MAC before decrypting (encrypt-then-MAC). Use RSA-OAEP instead of PKCS#1 v1.5.
- Fail securely: return uniform errors with constant-time handling. Map to ASVS 5.0 V11 Cryptography (11.2.5, 11.3.1, 11.3.3, 11.3.5) and `improper-encryption-mode`.

## References

- [OWASP WSTG-CRYP-02: Padding Oracle](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/09-Weak_Cryptography/02-Padding_Oracle/)
- [CWE-209: Generation of Error Message Containing Sensitive Information](https://cwe.mitre.org/data/definitions/209.html)
- [OWASP Cryptographic Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)
