---
id: credential-stuffing
title: Credential Stuffing
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-307, CWE-308]
severity: high
last_verified: 2026-10-29
aliases: [stolen-credentials, breach-replay, credential-replay]
---

# Credential Stuffing

## Why It Is a Threat

Attackers replay username/password pairs stolen from other breaches against your login, exploiting password reuse. Automated at scale across rotating IPs, it produces account takeovers that look like valid logins.

## How to Find or Test

Authorized testing only, with synthetic accounts and test credential lists, never real breached credentials (follow the `smx-ai-vapt` authorization gate).

- Test login with high-volume automated patterns from varied IPs/user-agents; confirm bot detection, rate limits, breached-credential checks, and suspicious-login notifications trigger.

## How to Fix

- Require MFA, ideally passkeys, which remove the reusable secret; screen passwords against breach corpora at signup, change, and login (k-anonymity range queries) and force a reset on a match.
- Deploy bot mitigation, device and IP reputation, and anomaly-based step-up; notify users of suspicious attempts. Map to ASVS 5.0 V6 Authentication (6.2.12, 6.3.1, 6.3.5).

## References

- [OWASP Credential Stuffing Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Credential_Stuffing_Prevention_Cheat_Sheet.html)
- [MITRE ATT&CK T1110.004: Brute Force: Credential Stuffing](https://attack.mitre.org/techniques/T1110/004/)
- [CAPEC-600: Credential Stuffing](https://capec.mitre.org/data/definitions/600.html)
- [CWE-307: Improper Restriction of Excessive Authentication Attempts](https://cwe.mitre.org/data/definitions/307.html)
