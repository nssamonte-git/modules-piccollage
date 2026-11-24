---
id: scalping-automation
title: Automated Purchasing / Scalping
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API6:2023, WSTG-BUSL-05]
cwe: [CWE-799]
severity: medium
last_verified: 2026-10-29
aliases: [automated-purchasing, sneaker-bots]
---

# Automated Purchasing / Scalping

## Why It Is a Threat

Bots buy limited high-demand inventory faster than humans (OWASP OAT-005 Scalping), exhausting stock for resale. This harms customers and the brand and drives infrastructure cost.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Assess bot resistance on high-demand purchase flows: rate limits, queueing, per-user and per-payment-instrument caps, and detection of non-human timing (for example add-to-cart and checkout within a second).

## How to Fix

- Add bot mitigation (challenges, device and behavior signals), fair queueing, and per-identity and per-payment-instrument purchase caps.
- Detect velocity anomalies, require realistic human timing, and require verified accounts for limited drops. Map to ASVS 5.0 V2 Validation and Business Logic (2.4.1, 2.4.2).

## References

- [OWASP OAT-005 Scalping](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-005_Scalping)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
