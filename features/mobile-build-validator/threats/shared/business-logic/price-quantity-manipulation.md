---
id: price-quantity-manipulation
title: Price or Quantity Manipulation
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-10, WSTG-BUSL-01, A06:2025]
cwe: [CWE-472, CWE-1284, CWE-20]
severity: high
last_verified: 2026-10-29
aliases: [negative-value-attacks, price-tampering]
---

# Price or Quantity Manipulation

## Why It Is a Threat

Clients send price, quantity, currency, discount, or total fields that the server trusts, letting an attacker set arbitrary, fractional, or negative values: buying at near zero, refunding more than paid, or inflating balances via negative quantities.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Tamper with price, quantity, total, currency, and optional amounts (tips, donations) in requests; submit negative, zero, decimal, overflow, and mismatched values and confirm server handling (`request-tampering`, `integer-overflow`).

## How to Fix

- Compute prices and totals server-side from trusted catalog data; never accept client-supplied money or quantity as authoritative.
- Validate ranges and types (positive integers where required) and check that related values are consistent before fulfillment. Map to ASVS 5.0 V2 Validation and Business Logic (2.2.1, 2.2.3, 2.3.2).

## References

- [OWASP WSTG-BUSL-10 Payment Functionality](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/10-Payment_Functionality/)
- [OWASP Business Logic Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Business_Logic_Security_Cheat_Sheet.html)
- [CWE-472: External Control of Assumed-Immutable Web Parameter](https://cwe.mitre.org/data/definitions/472.html)
- [CWE-1284: Improper Validation of Specified Quantity in Input](https://cwe.mitre.org/data/definitions/1284.html)
