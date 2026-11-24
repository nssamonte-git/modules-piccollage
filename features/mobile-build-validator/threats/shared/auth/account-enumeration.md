---
id: account-enumeration
title: Account Enumeration
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-IDNT-04, A07:2025]
cwe: [CWE-204, CWE-203, CWE-208]
severity: medium
last_verified: 2026-10-29
---

# Account Enumeration

## Why It Is a Threat

Login, registration, password-reset, MFA, or API responses reveal whether an account exists (through different messages, status codes, redirects, or response time), letting attackers build valid-user lists to target with spraying, stuffing, and phishing.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Compare responses for existing vs non-existing synthetic accounts across login, signup, reset, email/username-change, and API lookup flows: message text, status code, headers, redirects, and response length.
- Measure timing differences (for example, a password hash computed only for real users) over repeated requests.

## How to Fix

- Return identical messages, status codes, and redirects whether or not the account exists; for signup and reset, reply with a neutral "if an account exists, we sent a message" and continue out of band by email.
- Keep timing uniform (do the same hashing work for unknown users), and add rate limiting and anti-automation on enumeration-prone endpoints. Map to ASVS 5.0 V6 Authentication (6.3.8).

## References

- [OWASP WSTG-IDNT-04: Testing for Account Enumeration and Guessable User Account](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/03-Identity_Management/04-Account_Enumeration_and_Guessable_User_Account)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
- [CWE-204: Observable Response Discrepancy](https://cwe.mitre.org/data/definitions/204.html)
- [CWE-208: Observable Timing Discrepancy](https://cwe.mitre.org/data/definitions/208.html)
