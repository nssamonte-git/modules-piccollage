---
id: secrets-in-source-control
title: Secret Leakage in Source Control
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [CICD-SEC-6, A02:2025, A03:2025]
cwe: [CWE-540, CWE-798, CWE-538]
severity: high
last_verified: 2026-10-29
---

# Secret Leakage in Source Control

## Why It Is a Threat

Committed secrets (API keys, tokens, DB passwords, private keys), even in history, deleted files, or forks, are harvested by attackers scanning public and breached repos within minutes of a push, and stay reachable long after removal. Related: `secrets-leakage`, `exposed-env-files`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Scan the repo and its full history for secret patterns; check public-repo and fork exposure, container image layers, and CI logs.
- Source review by stack (`source-code-vapt`): Laravel committed `.env` or `APP_KEY`; CodeIgniter `encryption_key` (3) or `encryption.key` in `.env` (4); Django or Flask `SECRET_KEY` in source; React Native `.env` with `react-native-config` or `EXPO_PUBLIC_*` values; Flutter `.env` loaded by `flutter_dotenv`; PostgREST `jwt-secret` in config files.

## How to Fix

- Never commit secrets; load them from a secrets manager; enable pre-commit and server-side secret scanning with push protection.
- Treat any exposed secret as compromised: revoke and rotate it first, then purge it from history. Map to CICD-SEC-6, ASVS 5.0 V13.3 Secret Management, and the `security-review` secrets-handling reference.

## References

- [OWASP CICD-SEC-6: Insufficient Credential Hygiene](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-06-Insufficient-Credential-Hygiene)
- [GitHub Docs: Push protection](https://docs.github.com/en/code-security/concepts/secret-security/push-protection)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-540: Inclusion of Sensitive Information in Source Code](https://cwe.mitre.org/data/definitions/540.html)
