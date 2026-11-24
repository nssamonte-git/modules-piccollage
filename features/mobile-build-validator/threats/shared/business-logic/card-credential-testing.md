---
id: card-credential-testing
title: Credential or Payment-Card Testing
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API6:2023, API4:2023]
cwe: [CWE-799, CWE-307]
severity: high
last_verified: 2026-10-29
aliases: [carding, payment-card-testing]
---

# Credential or Payment-Card Testing

## Why It Is a Threat

Attackers use your auth or payment endpoints as an oracle to validate stolen credentials (OAT-008, `credential-stuffing`) or stolen card data (OAT-001 Carding), or to guess missing expiry dates and security codes (OAT-010 Card Cracking), via many small authorizations at scale. The result is fraud, processor fees and penalties, and chargebacks.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), in a sandbox with the provider's test cards.

- Assess login, add-card, and payment-authorization endpoints for velocity limits per account, device, IP, and card, plus bot detection.
- Check whether decline responses reveal which field failed (CVV vs expiry), and whether low-value or high-decline-rate bursts trigger alerts.

## How to Fix

- Rate-limit and bot-mitigate auth, add-card, and payment attempts; cap cards tried per account or device; require step-up or block after repeated declines.
- Return generic decline messages, use the processor's fraud and velocity tools, and alert on decline-rate spikes. Map to ASVS 5.0 V2 Validation and Business Logic (2.4.1, 2.4.2) and ASVS 5.0 V6 Authentication (6.1.1); see `credential-stuffing`.

## References

- [OWASP OAT-001 Carding](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-001_Carding)
- [OWASP OAT-010 Card Cracking](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-010_Card_Cracking)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
