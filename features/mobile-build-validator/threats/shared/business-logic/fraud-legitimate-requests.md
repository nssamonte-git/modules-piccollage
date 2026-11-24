---
id: fraud-legitimate-requests
title: Fraud Through Legitimate-Looking Requests
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-07, API6:2023]
cwe: []
severity: high
last_verified: 2026-10-29
aliases: [abuse-of-functionality]
---

# Fraud Through Legitimate-Looking Requests

## Why It Is a Threat

Each request is individually valid and authorized, but the aggregate intent is abusive: using features exactly as designed to commit fraud (money movement, abuse of generous policies, collusion). Input validation alone cannot catch it; insider variants overlap `insider-threats`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Model high-value flows for abuse of functionality: sequences of valid actions that yield fraud. Assess limits, velocity checks, and whether the application detects and responds to misuse rather than relying on per-request checks.

## How to Fix

- Document and enforce per-user and global business limits; add behavioral fraud detection, velocity and anomaly limits, and manual or multi-person review for high-risk actions; design policies (refunds, transfers) to resist abuse.
- Log and alert on intent-level patterns and attempts to bypass business rules. Map to ASVS 5.0 V2 Validation and Business Logic (2.1.3, 2.3.2, 2.3.5) and ASVS 5.0 V16 Security Logging and Error Handling (16.3.3).

## References

- [CAPEC-212: Functionality Misuse](https://capec.mitre.org/data/definitions/212.html)
- [OWASP WSTG-BUSL-07 Defenses Against Application Misuse](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/07-Defenses_Against_Application_Misuse/)
- [OWASP Business Logic Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Business_Logic_Security_Cheat_Sheet.html)
- [OWASP Transaction Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Transaction_Authorization_Cheat_Sheet.html)
