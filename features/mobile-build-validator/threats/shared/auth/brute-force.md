---
id: brute-force
title: Brute-Force Attack
category: auth
platforms: [shared, web, backend, android, ios]
owasp_refs: [WSTG-ATHN-03, A07:2025]
cwe: [CWE-307]
severity: high
last_verified: 2026-10-29
---

# Brute-Force Attack

## Why It Is a Threat

An attacker tries many passwords or codes against one account until one succeeds, exploiting weak passwords and missing rate limits. Leads to account takeover; also drives cost and availability impact on login and OTP endpoints.

## How to Find or Test

Authorized testing only, with synthetic accounts (follow the `smx-ai-vapt` authorization gate).

- Send repeated failed logins and confirm whether throttling, lockout, or step-up controls trigger; check per-account, per-IP, and per-credential limits, and whether parallel requests slip past the counter.
- Verify the same protection applies to API, mobile, and legacy auth paths.

## How to Fix

- Rate-limit failed attempts per account (NIST SP 800-63B-4 caps consecutive failures at 100 per authenticator) with progressive delays, CAPTCHA, or step-up; prefer throttling over hard lockout so attackers cannot lock out real users.
- Require strong, blocklist-screened passwords and MFA; document the anti-automation design; monitor and alert on failed-login spikes. Map to ASVS 5.0 V6 Authentication (6.1.1, 6.3.1).

## References

- [OWASP WSTG-ATHN-03: Testing for Weak Lock Out Mechanism](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/03-Weak_Lock_Out_Mechanism)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
- [CWE-307: Improper Restriction of Excessive Authentication Attempts](https://cwe.mitre.org/data/definitions/307.html)
- [MITRE ATT&CK T1110.001: Brute Force: Password Guessing](https://attack.mitre.org/techniques/T1110/001/)
