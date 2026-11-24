---
id: exposed-env-files
title: Exposed Environment / Config Files
category: config-infra
platforms: [shared, web, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-538, CWE-527, CWE-530, CWE-552]
severity: high
last_verified: 2026-10-29
---

# Exposed Environment / Config Files

## Why It Is a Threat

Config and environment files (`.env`, `.git/`, `config.*`, `*.bak`, editor swap files) are reachable from the web root or published in images and repos, leaking database credentials, API keys, and source code directly to attackers. Closely tied to `secrets-leakage`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Request common sensitive paths (`/.env`, `/.git/config`, backup and config files) and review the deployed web root, container images, and repos for committed secrets.
- Source review by stack (`source-code-vapt`): Laravel `.env` committed or under the web root; CodeIgniter 4 document root not set to `public/`; React Native and Flutter `.env*` files committed or bundled as assets.

## How to Fix

- Keep config and secret files outside the web root and out of version control; deploy without `.git`/`.svn` metadata; inject secrets at runtime from a secrets manager.
- Serve only an allowlist of file extensions and block dotfile and backup paths at the server. Map to A02:2025 and ASVS 5.0 V13.4 (no source-control metadata, allowed extensions only) and V13.3 Secret Management; see `improper-secrets-management`.

## References

- [OWASP WSTG: Review Old Backup and Unreferenced Files for Sensitive Information](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/04-Review_Old_Backup_and_Unreferenced_Files_for_Sensitive_Information)
- [CWE-527: Exposure of Version-Control Repository to an Unauthorized Control Sphere](https://cwe.mitre.org/data/definitions/527.html)
- [CWE-538: Insertion of Sensitive Information into Externally-Accessible File or Directory](https://cwe.mitre.org/data/definitions/538.html)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
