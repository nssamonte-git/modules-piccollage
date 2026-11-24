---
id: scraping
title: Scraping / Data Harvesting
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [API4:2023, API6:2023]
cwe: [CWE-799, CWE-200]
severity: medium
last_verified: 2026-10-29
---

# Scraping / Data Harvesting

## Why It Is a Threat

Automated clients collect content or personal data at scale (OWASP OAT-011 Scraping), enabling competitive theft, mass PII collection, and re-identification. Often via public or weakly authorized endpoints (`data-export-abuse`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Enumerate endpoints exposing bulk or sequential data; assess rate limits, server-side page-size caps, and bot detection with a small, capped request set.

## How to Fix

- Rate-limit and cap enumeration; require authentication and authorization for bulk data; use non-sequential ids (not as a substitute for access control) and bot mitigation.
- Return only the fields the client needs and monitor for harvesting patterns. Map to API4:2023, ASVS 5.0 V2 Validation and Business Logic (2.4.1) and ASVS 5.0 V14 Data Protection (14.2.6).

## References

- [OWASP OAT-011 Scraping](https://owasp.org/www-project-automated-threats-to-web-applications/assets/oats/EN/OAT-011_Scraping)
- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
