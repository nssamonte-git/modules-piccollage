---
id: improper-encryption-mode
title: Improper Encryption Mode
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-CRYPTO, WSTG-CRYP-04]
cwe: [CWE-327, CWE-353]
severity: high
last_verified: 2026-10-29
---

# Improper Encryption Mode

## Why It Is a Threat

Using an insecure block-cipher mode (ECB, or unauthenticated CBC/CTR without integrity) leaks plaintext patterns and enables tampering and padding-oracle attacks (`padding-oracle`). The mode choice undermines an otherwise strong cipher.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review encryption code for ECB usage (including library defaults such as Java `Cipher.getInstance("AES")`), unauthenticated modes, and RSA PKCS#1 v1.5 encryption; check whether ciphertext integrity is verified.
- Source review by stack (`source-code-vapt`): Android `Cipher.getInstance("AES")` (ECB by default); iOS `kCCOptionECBMode`; Flutter `encrypt` package `AESMode.ecb`.

## How to Fix

- Use authenticated encryption (AES-GCM or ChaCha20-Poly1305) and always specify the full mode explicitly; never ECB (NIST's SP 800-131A Rev. 3 draft proposes disallowing ECB for encryption). Use RSA-OAEP, not PKCS#1 v1.5, for RSA encryption.
- If a non-AEAD mode is unavoidable, use encrypt-then-MAC and verify the MAC before decrypting; prefer a vetted high-level crypto library. Map to ASVS 5.0 V11 Cryptography (11.3.1–11.3.3, 11.3.5).

## References

- [NIST SP 800-38D: Galois/Counter Mode (GCM) and GMAC](https://csrc.nist.gov/pubs/sp/800/38/d/final)
- [OWASP Cryptographic Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)
- [OWASP WSTG-CRYP-04: Weak Cryptographic Primitives](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/09-Weak_Cryptography/04-Weak_Cryptographic_Primitives/)
- [CWE-327: Use of a Broken or Risky Cryptographic Algorithm](https://cwe.mitre.org/data/definitions/327.html)
