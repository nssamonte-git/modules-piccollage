---
id: spam-content-flooding
title: Spam and Content Flooding
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API6:2023, API4:2023]
cwe: [CWE-799, CWE-400]
severity: medium
last_verified: 2026-10-29
---

# Spam and Content Flooding

## Why It Is a Threat

Attackers flood user-generated-content surfaces (comments, messages, forms, invites) with spam or abusive volume (OWASP OAT-017 Spamming), degrading the product, enabling phishing, and consuming resources and cost (overlaps `availability`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit a bounded burst of content or messages from test accounts and confirm that rate limits, content moderation, and abuse detection apply per user and IP.

## How to Fix

- Rate-limit content creation and messaging; add spam and abuse detection and moderation; require verified accounts for high-volume actions.
- Throttle invites and notifications to prevent amplification. Map to API4:2023 and ASVS 5.0 V2 Validation and Business Logic (2.4.1).

## References

- [OWASP OAT-017 Spamming](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-017_Spamming)
- [OWASP API6:2023 Unrestricted Access to Sensitive Business Flows](https://owasp.org/API-Security/editions/2023/en/0xa6-unrestricted-access-to-sensitive-business-flows/)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
