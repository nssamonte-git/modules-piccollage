---
id: iv-nonce-reuse
title: IV / Nonce Reuse
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-CRYPTO]
cwe: [CWE-323, CWE-329]
severity: high
last_verified: 2026-10-29
aliases: [static-iv, reused-iv, nonce-reuse]
---

# IV / Nonce Reuse

## Why It Is a Threat

Reusing or hard-coding an initialization vector or nonce breaks the security of stream and AEAD modes — for AES-GCM, nonce reuse can leak the authentication key and plaintext relationships, enabling forgery and decryption.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review encryption code for static/predictable IVs or nonces, counters that can repeat under the same key (restarts, multiple instances, cloned devices), and random nonces used past their safe limit.
- Source review by stack (`source-code-vapt`): Android constant IVs passed to `Cipher.init`; Flutter `encrypt` package `IV.fromLength(16)` (an all-zero IV) reused across messages.

## How to Fix

- Generate a fresh IV/nonce per encryption and never reuse it under one key: unpredictable random IVs for CBC; unique 96-bit nonces for GCM, and with random GCM nonces cap the number of encryptions per key (SP 800-38D limits this to 2^32).
- Rotate keys before the nonce budget is spent; where uniqueness is hard to guarantee, use a nonce-misuse-resistant AEAD such as AES-GCM-SIV. Map to ASVS 5.0 V11 Cryptography (11.3.4).

## References

- [NIST SP 800-38D: Galois/Counter Mode (GCM) and GMAC](https://csrc.nist.gov/pubs/sp/800/38/d/final)
- [CWE-323: Reusing a Nonce, Key Pair in Encryption](https://cwe.mitre.org/data/definitions/323.html)
- [CWE-329: Generation of Predictable IV with CBC Mode](https://cwe.mitre.org/data/definitions/329.html)
- [RFC 8452: AES-GCM-SIV: Nonce Misuse-Resistant Authenticated Encryption](https://www.rfc-editor.org/rfc/rfc8452)
