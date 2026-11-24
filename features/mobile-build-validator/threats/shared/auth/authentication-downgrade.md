---
id: authentication-downgrade
title: Authentication Downgrade
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHN-10, A07:2025]
cwe: [CWE-288, CWE-1390, CWE-757]
severity: medium
last_verified: 2026-10-29
---

# Authentication Downgrade

## Why It Is a Threat

The system allows falling back to a weaker authentication method (legacy protocols, basic auth, app-specific passwords, an older mobile API, SMS when a passkey is enrolled, or skipping step-up), letting an attacker choose the weakest path to bypass stronger controls.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Enumerate all authentication methods and endpoints (legacy, API, mobile, SSO, app-specific passwords, "try another way" pickers); test whether a weaker method is accepted for an account configured for strong auth.
- For federated login, check whether the app verifies the authentication strength the IdP reports (`acr`, `amr`, `auth_time`).

## How to Fix

- Document every authentication pathway and enforce the same strength on each; disable legacy and weak protocols and app-password loopholes.
- Enforce the strongest enrolled factor and require step-up consistently; when relying on an IdP, reject assertions that do not meet the required strength. Map to ASVS 5.0 V6 Authentication (6.1.3, 6.3.4, 6.8.4).

## References

- [OWASP WSTG-ATHN-10: Testing for Weaker Authentication in Alternative Channel](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/10-Weaker_Authentication_in_Alternative_Channel)
- [CWE-288: Authentication Bypass Using an Alternate Path or Channel](https://cwe.mitre.org/data/definitions/288.html)
- [CWE-1390: Weak Authentication](https://cwe.mitre.org/data/definitions/1390.html)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
