---
id: sensitive-data-exposure
title: Sensitive Information Disclosure
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, A06:2025, API3:2023]
cwe: [CWE-200]
severity: high
last_verified: 2026-10-29
aliases: [sensitive-information-disclosure, pii-exposure, financial-data-exposure, health-data-exposure]
---

# Sensitive Information Disclosure

## Why It Is a Threat

The system exposes sensitive data (PII, financial, health, credentials, private content) to parties who should not see it — via responses, error messages, debug output, caches, or weak access control. Drives breaches and regulatory penalties. Field-level over-exposure is `broken-property-level-authorization`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory and classify sensitive data flows; inspect API responses, errors, and debug pages for data beyond what the user needs.
- Check access control on sensitive records and exports (see `idor-bola`, `tenant-isolation-failure`).

## How to Fix

- Classify data into protection levels (ASVS 5.0 V14.1); return only required fields and mask or tokenize sensitive values (ASVS 5.0 V14.2.6 and V15.3.1); enforce authorization per record and per field (ASVS 5.0 V8.2.2 and V8.2.3).
- Encrypt in transit and at rest, keep sensitive data out of shared caches (ASVS 5.0 V14.2.2), and return generic error messages (ASVS 5.0 V16.5.1). Apply applicable privacy law.

## References

- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP API Security Top 10 2023 API3: Broken Object Property Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa3-broken-object-property-level-authorization/)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
- [NIST SP 800-122: Guide to Protecting the Confidentiality of PII](https://csrc.nist.gov/pubs/sp/800/122/final)
