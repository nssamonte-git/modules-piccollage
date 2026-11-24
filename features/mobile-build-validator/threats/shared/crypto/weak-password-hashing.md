---
id: weak-password-hashing
title: Weak or Unsalted Password Hashing
category: crypto
platforms: [shared, web, backend]
owasp_refs: [A04:2025]
cwe: [CWE-916, CWE-759, CWE-327]
severity: high
last_verified: 2026-10-29
aliases: [unsalted-password-hashes]
---

# Weak or Unsalted Password Hashing

## Why It Is a Threat

Passwords stored with fast/general hashes (MD5, SHA-1/256) or without per-user salts are cracked quickly with GPUs and rainbow tables after a database breach, leading to mass account compromise.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the password-storage routine for the hashing algorithm, per-user salt, work factor, and any encryption (reversible) storage of passwords.
- Source review by stack (`source-code-vapt`): Android and iOS `MessageDigest` or `CC_MD5`/`CC_SHA1` for passwords; PHP `md5(`, `sha1(`, `crypt(` instead of `password_hash`; Python `hashlib.md5`/`sha1` instead of the framework hasher or `argon2`; Rust `md5` or `sha1` crates instead of `argon2`.

## How to Fix

- Use a dedicated password hashing function with a unique salt and tuned cost: Argon2id preferred (OWASP minimum 19 MiB memory, 2 iterations, parallelism 1), else scrypt; bcrypt (cost 10+, 72-byte input limit) only for legacy; PBKDF2-HMAC-SHA-256 at 600,000+ iterations when FIPS-140 is required. Never plain, encrypted, or fast hashes.
- Rehash on login when parameters change; optionally add a pepper held outside the database (KMS/HSM). Map to ASVS 5.0 V11 Cryptography (11.4.2).

## References

- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
- [CWE-916: Use of Password Hash With Insufficient Computational Effort](https://cwe.mitre.org/data/definitions/916.html)
- [CWE-759: Use of a One-Way Hash without a Salt](https://cwe.mitre.org/data/definitions/759.html)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
