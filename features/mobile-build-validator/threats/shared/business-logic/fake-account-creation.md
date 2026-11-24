---
id: fake-account-creation
title: Fake-Account Creation
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API6:2023, WSTG-IDNT-02]
cwe: [CWE-799]
severity: medium
last_verified: 2026-10-29
aliases: [mass-account-creation]
---

# Fake-Account Creation

## Why It Is a Threat

Mass automated account creation (OWASP OAT-019 Account Creation) enables spam, fraud, promotion and trial abuse, review manipulation, and resource exhaustion. It is the enabler for many other automated abuses.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the registration process and, with a small capped script, confirm whether rate limits, bot detection, email/phone verification, and disposable-domain checks throttle sign-ups.

## How to Fix

- Require verified email or phone, add bot mitigation and velocity limits on signup, and block disposable or known-abuse identifiers.
- Risk-score new accounts and delay sensitive capabilities until trust is established. Map to ASVS 5.0 V2 Validation and Business Logic (2.4.1).

## References

- [OWASP OAT-019 Account Creation](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-019_Account_Creation)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [OWASP WSTG-IDNT-02 Test User Registration Process](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/03-Identity_Management/02-User_Registration_Process/)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
