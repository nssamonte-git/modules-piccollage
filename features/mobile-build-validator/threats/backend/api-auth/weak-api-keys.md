---
id: weak-api-keys
title: Weak API Key Handling
category: backend-api-auth
platforms: [backend]
owasp_refs: [API2:2023]
cwe: [CWE-522, CWE-798, CWE-598, CWE-331]
severity: high
last_verified: 2026-10-29
aliases: [api-keys-in-urls, api-key-reuse, weak-api-keys]
---

# Weak API Key Handling

## Why It Is a Threat

API keys that are low-entropy, placed in URLs (logged by proxies, browser history, and Referer headers), shared across clients or environments, or unscoped and non-rotatable are easily harvested and replayed, granting broad, durable access. An API key identifies a calling client, not a user, so it is weak as the only control on user data. Related: `api-key-theft`, `secrets-leakage`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check key entropy, whether keys appear in URLs or logs, reuse across clients and environments, and whether scoping, expiry, rotation, and revocation exist.

## How to Fix

- Issue high-entropy, per-client, scoped keys sent in a header (never the URL); support expiry, rotation, and revocation; use OAuth or mTLS where user-level or stronger authentication is needed.
- Store keys in a secrets manager and hash them at rest on the server. Map to API2:2023 and ASVS 5.0 V14.2 General Data Protection (14.2.1) and V13.3 Secret Management.

## References

- [OWASP API2:2023 Broken Authentication](https://owasp.org/API-Security/editions/2023/en/0xa2-broken-authentication/)
- [OWASP REST Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-598: Use of HTTP Request With Sensitive Query String](https://cwe.mitre.org/data/definitions/598.html)
