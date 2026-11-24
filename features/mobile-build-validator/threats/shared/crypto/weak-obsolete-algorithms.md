---
id: weak-obsolete-algorithms
title: Weak or Obsolete Cryptographic Algorithms
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-CRYPTO, WSTG-CRYP-04]
cwe: [CWE-327, CWE-328]
severity: high
last_verified: 2026-10-29
aliases: [weak-cipher-suites]
---

# Weak or Obsolete Cryptographic Algorithms

## Why It Is a Threat

The system uses broken or outdated primitives (MD5, SHA-1, DES/3DES, RC4) or weak TLS cipher suites, letting attackers break confidentiality/integrity through known cryptanalysis. NIST SP 800-131A Rev. 2 already disallows Triple-DES encryption and SHA-1 signature generation, and the Rev. 3 draft schedules SHA-1 and 224-bit hashes for retirement after 2030.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory crypto usage (code, libraries, certificates, and TLS configuration); flag deprecated algorithms, short keys, and weak cipher suites (scan TLS with an authorized tool).

## How to Fix

- Use modern, standard algorithms: AES-GCM or ChaCha20-Poly1305, SHA-256 or stronger, and keys giving at least 128-bit security for new designs (RSA-3072+, ECDSA P-256+, or Ed25519); enable only TLS 1.2+ with strong suites.
- Keep crypto libraries current, avoid custom crypto, and keep a crypto inventory with a migration plan: NIST IR 8547 sets out the move from RSA and elliptic-curve algorithms to the post-quantum standards (ML-KEM, ML-DSA, SLH-DSA). Map to ASVS 5.0 V11 Cryptography (11.1.2, 11.1.4, 11.2.3, 11.4.1) and V12 (12.1.1, 12.1.2).

## References

- [NIST SP 800-131A Rev. 2: Transitioning the Use of Cryptographic Algorithms and Key Lengths](https://csrc.nist.gov/pubs/sp/800/131/a/r2/final)
- [NIST IR 8547: Transition to Post-Quantum Cryptography Standards](https://csrc.nist.gov/pubs/ir/8547/final)
- [OWASP WSTG-CRYP-04: Weak Cryptographic Primitives](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/09-Weak_Cryptography/04-Weak_Cryptographic_Primitives/)
- [CWE-327: Use of a Broken or Risky Cryptographic Algorithm](https://cwe.mitre.org/data/definitions/327.html)
