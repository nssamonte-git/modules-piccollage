---
id: data-export-abuse
title: Data Export Abuse
category: data-privacy
platforms: [shared, web, backend]
owasp_refs: [A01:2025, API1:2023, API4:2023]
cwe: [CWE-200, CWE-639]
severity: medium
last_verified: 2026-10-29
---

# Data Export Abuse

## Why It Is a Threat

Bulk export, reporting, or download features are abused to extract large volumes of data — scraping a whole tenant, exporting other users' records by changing ids or filters, or exfiltrating through legitimate export paths that lack per-record authorization, volume limits, and monitoring. Long-lived or guessable export download links extend the exposure. Related: `idor-bola`, `scraping` (business-logic), `csv-formula-injection`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with synthetic users or tenants.

- Test export/report endpoints for per-record authorization, tenant scoping, volume limits, and whether ids, filters, or page sizes can widen the export.
- Check how generated files are delivered (link lifetime, authentication) and whether large exports are logged and alerted.

## How to Fix

- Enforce authorization and tenant scoping on every exported record (ASVS 5.0 V8.2.2 and V8.4.1); return only required fields.
- Rate-limit and cap export size (ASVS 5.0 V2.4.1 anti-automation); deliver files through short-lived, authenticated links; log and alert on large or unusual exports; watermark or track sensitive exports.

## References

- [OWASP API Security Top 10 2023 API1: Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa1-broken-object-level-authorization/)
- [OWASP API Security Top 10 2023 API4: Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [CWE-639: Authorization Bypass Through User-Controlled Key](https://cwe.mitre.org/data/definitions/639.html)
