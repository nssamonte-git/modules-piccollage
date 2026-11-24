---
id: race-condition
title: Race Conditions / TOCTOU
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-10, A06:2025]
cwe: [CWE-362, CWE-367]
severity: high
last_verified: 2026-10-29
aliases: [toctou, duplicate-transaction-execution, idempotency-key-abuse, replay-of-financial-operations]
---

# Race Conditions / TOCTOU

## Why It Is a Threat

Concurrent requests exploit the gap between check and use (TOCTOU) to double-spend, over-withdraw, redeem a coupon twice, or oversell stock, for example by firing many parallel requests before a balance or limit updates. Weak idempotency lets duplicate transactions execute.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send a bounded batch of concurrent requests at limited or financial operations (redeem, withdraw, transfer, purchase) and confirm whether limits hold or double execution occurs.

## How to Fix

- Perform check and update as one atomic operation: database transactions, unique constraints, row locks, or conditional updates; enforce server-side idempotency keys for sensitive operations.
- Avoid check-then-act gaps in application code. Map to ASVS 5.0 V15 Secure Coding and Architecture (15.4.2) and ASVS 5.0 V2 Validation and Business Logic (2.3.3, 2.3.4); see `replay-attack`.

## References

- [CWE-367: Time-of-check Time-of-use (TOCTOU) Race Condition](https://cwe.mitre.org/data/definitions/367.html)
- [CWE-362: Concurrent Execution using Shared Resource with Improper Synchronization](https://cwe.mitre.org/data/definitions/362.html)
- [OWASP WSTG-BUSL-10 Payment Functionality (race conditions)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/10-Payment_Functionality/)
- [OWASP Business Logic Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Business_Logic_Security_Cheat_Sheet.html)
