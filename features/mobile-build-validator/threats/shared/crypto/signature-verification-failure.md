---
id: signature-verification-failure
title: Signature-Verification Failure
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, A08:2025]
cwe: [CWE-347]
severity: high
last_verified: 2026-10-29
aliases: [unsigned-updates, improperly-signed-updates]
---

# Signature-Verification Failure

## Why It Is a Threat

The system fails to verify digital signatures correctly — accepting unsigned content, skipping verification, or trusting attacker-chosen keys or algorithms — so forged data, tokens, or software updates are accepted. Unsigned/improperly-signed updates enable malicious update delivery (see supply-chain `malicious-software-updates`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit tampered/unsigned payloads and updates; confirm verification against a trusted key occurs before use (see `jwt-signature-validation` for tokens).
- Check that the verified bytes are the bytes actually used, and that the algorithm and key cannot be chosen by the message itself.

## How to Fix

- Verify signatures against a pinned/pre-configured trusted public key and an algorithm allowlist before processing; reject unsigned content and use approved signature schemes (FIPS 186-5).
- Sign and verify software updates (code signing) with a protected key. Map to ASVS 5.0 V9 Self-contained Tokens (9.1.1–9.1.3) and V11 Cryptography (11.6.1).

## References

- [CWE-347: Improper Verification of Cryptographic Signature](https://cwe.mitre.org/data/definitions/347.html)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [OWASP JSON Web Token Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_Cheat_Sheet.html)
- [NIST FIPS 186-5: Digital Signature Standard (DSS)](https://csrc.nist.gov/pubs/fips/186-5/final)
