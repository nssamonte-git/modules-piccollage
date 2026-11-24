---
id: cryptographic-downgrade
title: Cryptographic Downgrade Attack
category: crypto
platforms: [shared, web, backend]
owasp_refs: [A04:2025, WSTG-CRYP-01]
cwe: [CWE-757]
severity: medium
last_verified: 2026-10-29
---

# Cryptographic Downgrade Attack

## Why It Is a Threat

An attacker forces negotiation of a weaker protocol, cipher, or auth method (e.g. TLS version or cipher downgrade, HTTPS-to-HTTP stripping, fallback to a legacy algorithm), then exploits the weaker option to break confidentiality or integrity.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review whether the server/client allows weak protocol/cipher fallback or client-side "retry with an older version" logic; test negotiation with an authorized TLS scanner.
- Check application-level negotiation too (e.g. token `alg` or key-exchange choices taken from attacker-controlled input).

## How to Fix

- Enable only TLS 1.2 and 1.3 (prefer 1.3; TLS 1.0/1.1 are deprecated by RFC 8996), drop RSA and static/finite-field DH key exchange in TLS 1.2, and keep TLS 1.3's built-in downgrade check enabled in clients.
- Remove insecure version-fallback retries (use TLS_FALLBACK_SCSV only where fallback cannot be removed); use HSTS to stop HTTPS stripping; pin allowed algorithms in application crypto instead of negotiating them. Map to ASVS 5.0 V12 Secure Communication (12.1.1, 12.1.2, 12.2.1) and V9 (9.1.2).

## References

- [CWE-757: Selection of Less-Secure Algorithm During Negotiation](https://cwe.mitre.org/data/definitions/757.html)
- [OWASP WSTG-CRYP-01: Weak Transport Layer Security](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/09-Weak_Cryptography/01-Weak_Transport_Layer_Security/)
- [OWASP Transport Layer Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Transport_Layer_Security_Cheat_Sheet.html)
- [RFC 9846: The Transport Layer Security (TLS) Protocol Version 1.3](https://www.rfc-editor.org/rfc/rfc9846)
