---
id: payment-abuse
title: Payment-Status Manipulation and Refund Abuse
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-10, A06:2025]
cwe: [CWE-345]
severity: high
last_verified: 2026-10-29
aliases: [payment-status-manipulation, refund-abuse]
---

# Payment-Status Manipulation and Refund Abuse

## Why It Is a Threat

Attackers mark orders paid without paying (the app trusts a client redirect or unverified callback), alter the amount or currency between order creation and payment, or repeatedly trigger refunds to extract value. Often combined with weak webhook verification or missing reconciliation.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), in a sandbox.

- Attempt to set order status to paid directly, via the return URL, or via forged provider callbacks; compare the amount and currency actually paid with the order total.
- Test repeated, partial, and concurrent refunds; verify webhook signature checks and idempotency.

## How to Fix

- Treat the payment provider as the source of truth: verify signed webhooks or query the provider server-side, and reconcile amount, currency, and order id before fulfillment.
- Make refunds idempotent, capped at the captured amount, and audited. Map to ASVS 5.0 V2 Validation and Business Logic (2.3.2, 2.3.3) and `webhook-security`.

## References

- [OWASP WSTG-BUSL-10 Payment Functionality](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/10-Payment_Functionality/)
- [OWASP Third Party Payment Gateway Integration Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Third_Party_Payment_Gateway_Integration_Cheat_Sheet.html)
- [OWASP Webhook Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Webhook_Security_Cheat_Sheet.html)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
