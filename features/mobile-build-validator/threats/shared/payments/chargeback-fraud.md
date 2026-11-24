---
id: chargeback-fraud
title: Chargeback Fraud
category: payments
platforms: [shared, web, backend]
owasp_refs: [API6:2023]
cwe: []
severity: medium
last_verified: 2026-10-29
aliases: [friendly-fraud]
---

# Chargeback Fraud

## Why It Is a Threat

A customer receives goods or services and then disputes the legitimate charge ("friendly fraud") to get a refund while keeping the product, or attackers buy with stolen cards (OWASP OAT-012 Cashing Out) and the real cardholders dispute the charges. The merchant bears direct loss and fees, and a high dispute ratio can trigger card-scheme monitoring programs and processor penalties. Related: `payment-abuse`, `fraud-legitimate-requests`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate); controls review.

- Review dispute rates and patterns, transaction-evidence capture (AVS/CVV results, 3-D Secure outcome, device, IP, delivery and usage proof), and whether high-risk transactions get extra verification.

## How to Fix

- Capture and retain strong transaction and delivery evidence; use 3-D Secure where supported and risk scoring for high-risk orders (confirm liability rules with your acquirer); monitor dispute ratios and block repeat abusers.
- Delay or hold fulfillment for risky orders and use clear billing descriptors to reduce mistaken disputes. Map to API6:2023 and ASVS 5.0 V2 Validation and Business Logic (2.3.2).

## References

- [OWASP OAT-012 Cashing Out](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-012_Cashing_Out)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [OWASP Third Party Payment Gateway Integration Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Third_Party_Payment_Gateway_Integration_Cheat_Sheet.html)
