---
id: improper-key-management
title: Improper Key Management
category: crypto
platforms: [shared, web, backend]
owasp_refs: [A04:2025, MASVS-CRYPTO]
cwe: [CWE-522, CWE-324]
severity: high
last_verified: 2026-10-29
aliases: [keys-stored-with-encrypted-data, improper-key-rotation, missing-key-revocation]
---

# Improper Key Management

## Why It Is a Threat

Weak key lifecycle undermines encryption: storing keys next to the data they protect, never rotating, or having no way to revoke a compromised key. A leaked or stale key then exposes all data it covers.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review where keys live relative to ciphertext, whether a key inventory exists, cryptoperiods and rotation cadence, and revocation/rewrap procedures; check access control and audit on key material.

## How to Fix

- Separate keys from data using a KMS/HSM with least-privilege access and audit; define cryptoperiods per NIST SP 800-57 and rotate on schedule and on compromise, re-encrypting or re-wrapping data.
- Use envelope encryption (per-data keys wrapped by a key-encryption key), keep a key inventory, and support revocation. Map to ASVS 5.0 V11 Cryptography (11.1.1, 11.1.2) and V13 Configuration (13.3.1, 13.3.3, 13.3.4).

## References

- [NIST SP 800-57 Part 1 Rev. 5: Recommendation for Key Management](https://csrc.nist.gov/pubs/sp/800/57/pt1/r5/final)
- [OWASP Key Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Key_Management_Cheat_Sheet.html)
- [OWASP Cryptographic Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)
- [CWE-324: Use of a Key Past its Expiration Date](https://cwe.mitre.org/data/definitions/324.html)
