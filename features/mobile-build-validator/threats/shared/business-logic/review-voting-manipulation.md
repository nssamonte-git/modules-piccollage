---
id: review-voting-manipulation
title: Review or Voting Manipulation
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API6:2023, WSTG-BUSL-05]
cwe: [CWE-837, CWE-799]
severity: medium
last_verified: 2026-10-29
---

# Review or Voting Manipulation

## Why It Is a Threat

Attackers inflate or suppress ratings, reviews, votes, or polls using fake accounts or repeated submissions (OWASP OAT-016 Skewing), distorting reputation systems and rankings for profit or harm.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Attempt repeated, concurrent, and multi-account voting or reviewing with controlled accounts; check one-action-per-eligible-user enforcement and verified-purchase or participation gating.

## How to Fix

- Enforce eligibility (verified purchase or participation) and one action per identity atomically on the server; detect coordinated multi-account behavior.
- Weight by account trust and rate-limit submissions. Map to ASVS 5.0 V2 Validation and Business Logic (2.3.2, 2.4.1).

## References

- [OWASP OAT-016 Skewing](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-016_Skewing)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [CWE-837: Improper Enforcement of a Single, Unique Action](https://cwe.mitre.org/data/definitions/837.html)
