---
id: weak-random-number-generation
title: Weak Random-Number Generation / Predictable Tokens
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-CRYPTO]
cwe: [CWE-338, CWE-330]
severity: high
last_verified: 2026-10-29
aliases: [predictable-tokens, insecure-randomness]
---

# Weak Random-Number Generation / Predictable Tokens

## Why It Is a Threat

Security-sensitive values (session ids, password-reset/OTP tokens, keys, nonces) are generated with non-cryptographic RNGs (e.g. `Math.random`, `rand()`) or predictable seeds, letting attackers guess or reproduce them.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify token/id/key generation; flag non-CSPRNG sources, fixed or time-based seeds, and low entropy (including UUIDs used as secrets).
- Collect samples and assess predictability/entropy.
- Source review by stack (`source-code-vapt`): Android `java.util.Random` or `kotlin.random.Random`; Flutter `Random()` instead of `Random.secure()`; React Native `Math.random()`; PHP `rand(`, `mt_rand(`, `uniqid(`; Python `random` instead of `secrets`; Rust non-cryptographic RNGs such as `fastrand` for tokens.

## How to Fix

- Use a cryptographically secure RNG for all security-sensitive values with at least 128 bits of entropy: `crypto.randomBytes`/`crypto.getRandomValues` (JavaScript), Python `secrets`, Java `SecureRandom`, iOS `SecRandomCopyBytes`.
- Never seed CSPRNGs with predictable values, and do not treat UUIDs as secret tokens. Map to ASVS 5.0 V11 Cryptography (11.5.1) and MASVS-CRYPTO-1.

## References

- [CWE-338: Use of Cryptographically Weak Pseudo-Random Number Generator (PRNG)](https://cwe.mitre.org/data/definitions/338.html)
- [NIST SP 800-90A Rev. 1: Random Number Generation Using Deterministic Random Bit Generators](https://csrc.nist.gov/pubs/sp/800/90/a/r1/final)
- [MDN: Crypto.getRandomValues()](https://developer.mozilla.org/en-US/docs/Web/API/Crypto/getRandomValues)
- [OWASP Cryptographic Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)
