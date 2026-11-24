---
id: trial-subscription-abuse
title: Trial or Subscription Abuse
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-05, API6:2023]
cwe: [CWE-837, CWE-799]
severity: medium
last_verified: 2026-10-29
---

# Trial or Subscription Abuse

## Why It Is a Threat

Attackers repeatedly obtain free trials or premium access via fake accounts (OAT-019), payment-method cycling, or entitlement gaps such as access retained after cancellation, refund, or expiry, causing revenue loss.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Attempt repeated trials across controlled identities and payment methods; test access immediately after cancellation, refund, or expiry.

## How to Fix

- Tie trial eligibility to durable signals (verified identity, device, payment instrument fingerprint) and enforce one-trial limits; re-check entitlement server-side on every privileged use.
- Apply entitlement changes immediately on cancel or expiry (`missing-token-revocation`). Map to ASVS 5.0 V2 Validation and Business Logic (2.3.2) and ASVS 5.0 V8 Authorization (8.3.2).

## References

- [OWASP WSTG-BUSL-05 Number of Times a Function Can Be Used Limits](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/05-Number_of_Times_a_Function_Can_Be_Used_Limits/)
- [OWASP OAT-019 Account Creation](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-019_Account_Creation)
- [CWE-837: Improper Enforcement of a Single, Unique Action](https://cwe.mitre.org/data/definitions/837.html)
