---
id: inventory-reservation-abuse
title: Inventory / Reservation Hoarding
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API6:2023, WSTG-BUSL-04]
cwe: [CWE-799]
severity: medium
last_verified: 2026-10-29
aliases: [ticket-hoarding, appointment-hoarding]
---

# Inventory / Reservation Hoarding

## Why It Is a Threat

Attackers reserve large quantities of limited stock, tickets, or appointment slots without completing purchase (OWASP OAT-021 Denial of Inventory), denying availability to real users and enabling scalping (`scalping-automation`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Create a bounded number of concurrent or abandoned reservations; check whether holds expire, per-user caps apply, and stock is released atomically.

## How to Fix

- Time-box reservations with automatic release; cap concurrent holds per user, payment instrument, and device; require verification for bulk holds.
- Use business-logic locking and atomic stock decrement so limited items cannot be double-booked (`race-condition`). Map to ASVS 5.0 V2 Validation and Business Logic (2.3.4, 2.4.1).

## References

- [OWASP OAT-021 Denial of Inventory](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-021_Denial_of_Inventory)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [OWASP WSTG-BUSL-04 Process Timing](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/04-Process_Timing/)
