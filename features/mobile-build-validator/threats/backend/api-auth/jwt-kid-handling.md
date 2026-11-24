---
id: jwt-kid-handling
title: Unsafe JWT kid Handling
category: backend-api-auth
platforms: [backend]
owasp_refs: [API2:2023, A07:2025]
cwe: [CWE-347, CWE-22, CWE-89, CWE-90]
severity: high
last_verified: 2026-10-29
aliases: [unsafe-jwt-kid]
---

# Unsafe JWT kid Handling

## Why It Is a Threat

The JWT `kid` (key ID) header tells the verifier which key to use. If the server passes `kid` into a file path, SQL or LDAP lookup, or URL, or trusts key sources named in the token (`jku`, `jwk`, `x5u`), an attacker can steer verification to a key they know or control (through path traversal, injection, or SSRF) and forge tokens the server accepts. Complements `jwt-algorithm-confusion` and `jwt-signature-validation`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review how `kid` is resolved to a key and whether `jku`, `jwk`, or `x5u` headers are honoured; tamper with `kid` (path traversal, SQLi payloads, external URLs) and confirm the server rejects tokens that name unknown keys or untrusted key sources.

## How to Fix

- Treat `kid` as an opaque lookup key into a fixed, pre-configured keystore or the issuer's trusted JWKS; reject unknown ids and never use `kid` in file, SQL, LDAP, or URL operations.
- Ignore key material or key URLs supplied in the token unless they match an allowlist for that issuer; pin the expected algorithm per key. Map to API2:2023 and ASVS 5.0 V9 Self-contained Tokens (9.1.1, 9.1.3).

## References

- [RFC 8725: JSON Web Token Best Current Practices (section 3.10)](https://www.rfc-editor.org/rfc/rfc8725.html)
- [OWASP JSON Web Token Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_Cheat_Sheet.html)
- [OWASP WSTG: Testing JSON Web Tokens (WSTG-SESS-10)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/10-JSON_Web_Tokens)
- [CWE-347: Improper Verification of Cryptographic Signature](https://cwe.mitre.org/data/definitions/347.html)
- [OWASP API2:2023 Broken Authentication](https://owasp.org/API-Security/editions/2023/en/0xa2-broken-authentication/)
