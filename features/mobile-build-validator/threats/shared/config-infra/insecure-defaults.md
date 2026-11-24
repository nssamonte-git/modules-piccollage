---
id: insecure-defaults
title: Insecure Defaults / Prod-Test Mixing
category: config-infra
platforms: [shared, web, backend]
owasp_refs: [A02:2025]
cwe: [CWE-1188, CWE-276]
severity: medium
last_verified: 2026-11-18
aliases: [shared-production-credentials, improper-production-test-separation, incorrect-default-permissions]
---

# Insecure Defaults / Prod-Test Mixing

## Why It Is a Threat

Shipping with insecure default settings (including files, directories, services, or shares installed with world-writable or overly broad permissions), sharing credentials across environments, or mixing production and test (test endpoints, data, or keys in prod; prod data in test) creates exposure and lets a compromise of a weaker test environment reach production.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review default configurations against vendor hardening guidance; check installed files, directories, services, and shares for overly broad default permissions; check for secrets shared across environments, network paths between test and prod, and production data in non-production systems.
- Source review by stack (`source-code-vapt`): Django settings that fail `python manage.py check --deploy`; Postgres before version 15, which grants `CREATE` on schema `public` to `PUBLIC`.

## How to Fix

- Make the secure setting the default, including least-privilege file and service permissions at install time (restrictive umask, no world-writable paths); use distinct per-environment secrets, accounts, and isolated infrastructure; never copy production data into test without masking.
- Enforce environment separation in IaC and CI, and ship only required functionality to production. Map to A02:2025 and ASVS 5.0 V13 Configuration and V15.2 (no test code or extraneous functionality in production).

## References

- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
- [CWE-1188: Initialization of a Resource with an Insecure Default](https://cwe.mitre.org/data/definitions/1188.html)
- [CWE-276: Incorrect Default Permissions](https://cwe.mitre.org/data/definitions/276.html)
- [CISA Secure by Design](https://www.cisa.gov/securebydesign)
