---
id: promotion-abuse
title: Coupon, Loyalty, and Referral Abuse
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-05, API6:2023, A06:2025]
cwe: [CWE-837, CWE-799]
severity: medium
last_verified: 2026-10-29
aliases: [coupon-abuse, loyalty-point-abuse, referral-fraud]
---

# Coupon, Loyalty, and Referral Abuse

## Why It Is a Threat

Attackers exploit promotions for profit: stacking or reusing coupons, farming loyalty points, or self-referral fraud with fake accounts. The result is direct financial loss and skewed incentives.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Attempt coupon reuse and stacking, repeated and concurrent redemption, point farming via reversible actions (purchase then refund), and referrals between controlled accounts.

## How to Fix

- Enforce single-use and per-user and global limits server-side with atomic checks; detect self-referral and fake-account patterns (`fake-account-creation`).
- Make point earning idempotent and claw back rewards when the earning action is reversed. Map to ASVS 5.0 V2 Validation and Business Logic (2.1.3, 2.3.2).

## References

- [OWASP WSTG-BUSL-05 Number of Times a Function Can Be Used Limits](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/05-Number_of_Times_a_Function_Can_Be_Used_Limits/)
- [CWE-837: Improper Enforcement of a Single, Unique Action](https://cwe.mitre.org/data/definitions/837.html)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
