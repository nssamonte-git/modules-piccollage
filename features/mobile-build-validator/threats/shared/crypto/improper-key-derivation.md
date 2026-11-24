---
id: improper-key-derivation
title: Improper Key Derivation
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-CRYPTO]
cwe: [CWE-916, CWE-326]
severity: medium
last_verified: 2026-10-29
---

# Improper Key Derivation

## Why It Is a Threat

Deriving keys from passwords or shared secrets with a weak KDF, low iteration count, missing salt, or by directly using a low-entropy input makes brute-force feasible and weakens all data protected by the derived key.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review key-derivation code for the KDF, parameters (iterations/memory), salt usage, input entropy, and whether one derived key is reused for several purposes.

## How to Fix

- From passwords: use a memory-hard or iterated KDF with current parameters (Argon2id, scrypt, or PBKDF2-HMAC-SHA-256 at OWASP-recommended iterations) and a unique random salt.
- From high-entropy secrets (e.g. key-agreement output): use an approved KDF such as HKDF or an SP 800-108 KDF with distinct context labels per purpose. Never use raw hashes, raw shared secrets, or low-entropy inputs as keys. Map to ASVS 5.0 V11 Cryptography (11.4.4, 11.2.3).

## References

- [NIST SP 800-132: Recommendation for Password-Based Key Derivation](https://csrc.nist.gov/pubs/sp/800/132/final)
- [NIST SP 800-108 Rev. 1: Recommendation for Key Derivation Using Pseudorandom Functions](https://csrc.nist.gov/pubs/sp/800/108/r1/upd1/final)
- [RFC 5869: HMAC-based Extract-and-Expand Key Derivation Function (HKDF)](https://www.rfc-editor.org/rfc/rfc5869)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
