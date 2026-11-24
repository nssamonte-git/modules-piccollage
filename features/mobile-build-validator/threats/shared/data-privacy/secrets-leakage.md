---
id: secrets-leakage
title: Credential, Token, or Key Leakage
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, A02:2025]
cwe: [CWE-200, CWE-522, CWE-321]
severity: high
last_verified: 2026-10-29
aliases: [credential-leakage, token-leakage, cryptographic-key-leakage]
---

# Credential, Token, or Key Leakage

## Why It Is a Threat

Secrets (passwords, tokens, API keys, crypto keys) leak through responses, logs, error pages, client bundles, mobile binaries, URLs and referrers, or build artifacts, then get reused by attackers. Anything shipped in a web bundle or APK/IPA is extractable. Closely related to `api-key-theft`, `secrets-in-source-control` (supply-chain), and `hardcoded-encryption-keys` (crypto).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Scan responses, logs, error output, client bundles, source maps, and mobile IPA/APK for secret patterns.
- Check whether tokens appear in URLs or query strings and leak through referrers or proxies.

## How to Fix

- Keep secrets server-side in a secrets manager with least-privilege access (ASVS 5.0 V13.3 Secret Management); never log, return, or embed them in clients or repos; proxy third-party calls through the backend instead of shipping keys.
- Keep secrets out of URLs (ASVS 5.0 V14.2.1); scope, expire, rotate, and revoke secrets; add automated secret scanning in CI. See `secrets-in-source-control`.

## References

- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
- [OWASP Top 10:2025 A04 Cryptographic Failures](https://top10.owasp.org/2025/A04_2025-Cryptographic_Failures/)
