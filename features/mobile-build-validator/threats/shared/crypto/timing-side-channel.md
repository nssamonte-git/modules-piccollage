---
id: timing-side-channel
title: Timing and Side-Channel Attacks
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025]
cwe: [CWE-208, CWE-203]
severity: medium
last_verified: 2026-10-29
aliases: [timing-attacks, side-channel-attacks]
---

# Timing and Side-Channel Attacks

## Why It Is a Threat

Secret-dependent variations in execution time, memory access, power, or cache behavior leak information — e.g. non-constant-time comparison of tokens/MACs lets an attacker recover secrets byte by byte.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review comparisons of secrets/MACs/tokens for early-exit (`==`) instead of constant-time; assess crypto operations for known side-channel-prone patterns.
- Where needed, measure response-time differences statistically over many samples in a controlled, authorized environment.
- Source review by stack (`source-code-vapt`): PHP `==` between tokens or hashes (use `hash_equals`); Python `==` on secrets (use `hmac.compare_digest`); Rust `==` on MACs (use `subtle::ConstantTimeEq`).

## How to Fix

- Use constant-time comparison for secrets/MACs (Python `hmac.compare_digest`, Node.js `crypto.timingSafeEqual`, Java `MessageDigest.isEqual`, OpenSSL `CRYPTO_memcmp`); use side-channel-resistant library implementations.
- Avoid secret-dependent branching/indexing. Map to ASVS 5.0 V11 Cryptography (11.2.1, 11.2.4).

## References

- [CWE-208: Observable Timing Discrepancy](https://cwe.mitre.org/data/definitions/208.html)
- [CWE-203: Observable Discrepancy](https://cwe.mitre.org/data/definitions/203.html)
- [OWASP Top 10:2025 A04 Cryptographic Failures](https://top10.owasp.org/2025/A04_2025-Cryptographic_Failures/)
