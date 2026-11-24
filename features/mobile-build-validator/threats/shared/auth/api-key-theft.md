---
id: api-key-theft
title: API-Key Theft or Leakage
category: auth
platforms: [shared, web, backend, android, ios]
owasp_refs: [A07:2025, API2:2023]
cwe: [CWE-522, CWE-798]
severity: high
last_verified: 2026-10-29
aliases: [api-key-leakage]
---

# API-Key Theft or Leakage

## Why It Is a Threat

API keys leak via source control, client bundles, mobile binaries, URLs, logs, or error messages, then are reused by attackers to access data and run up cost. Keys are often long-lived and over-privileged, magnifying impact. Anything shipped in a web bundle, APK, or IPA should be treated as public. See also `secrets-in-source-control` (supply-chain) and `crypto` key handling.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Search repos (including history), client bundles, mobile IPA/APK, logs, URLs, and responses for embedded keys; check whether found keys are scoped, restricted, rotatable, and still live (validate only against the owner's approved scope).

## How to Fix

- Keep secrets server-side; never embed privileged keys in clients or repos. For client apps, proxy calls through your backend or issue short-lived per-user tokens; restrict any unavoidable client key by app, origin, and API.
- Store keys in a secrets manager with least privilege, expiry, and rotation; keep them out of URLs; run secret scanning with push protection in CI and revoke immediately on leak. Map to ASVS 5.0 V13 Configuration (13.3), V14 Data Protection (14.2.1), and API2:2023.

## References

- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [OWASP API2:2023 Broken Authentication](https://owasp.org/API-Security/editions/2023/en/0xa2-broken-authentication/)
- [CWE-798: Use of Hard-coded Credentials](https://cwe.mitre.org/data/definitions/798.html)
- [MITRE ATT&CK T1552.001: Unsecured Credentials: Credentials In Files](https://attack.mitre.org/techniques/T1552/001/)
- [GitHub Docs: Secret scanning](https://docs.github.com/en/code-security/secret-scanning/introduction/about-secret-scanning)
