---
id: password-spraying
title: Password Spraying
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025, WSTG-ATHN-03]
cwe: [CWE-307, CWE-521]
severity: high
last_verified: 2026-10-29
---

# Password Spraying

## Why It Is a Threat

An attacker tries a few common passwords across many accounts, staying under per-account lockout thresholds to evade brute-force defenses while still compromising weak accounts at scale. Legacy or non-MFA endpoints are favored targets.

## How to Find or Test

Authorized testing only, with synthetic accounts and SOC coordination (follow the `smx-ai-vapt` authorization gate).

- Simulate low-and-slow attempts of common passwords across many synthetic accounts; confirm whether per-IP, per-password, and tenant-wide anomaly detection triggers (not just per-account lockout).

## How to Fix

- Add detection across accounts (per-IP, per-password, global failure-rate), not only per-account lockout.
- Block common, context-specific, and breached passwords; require MFA on every path, including legacy protocols; alert on distributed failures. Map to ASVS 5.0 V6 Authentication (6.2.4, 6.2.11, 6.2.12, 6.3.1).

## References

- [MITRE ATT&CK T1110.003: Brute Force: Password Spraying](https://attack.mitre.org/techniques/T1110/003/)
- [CAPEC-565: Password Spraying](https://capec.mitre.org/data/definitions/565.html)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
- [CWE-307: Improper Restriction of Excessive Authentication Attempts](https://cwe.mitre.org/data/definitions/307.html)
