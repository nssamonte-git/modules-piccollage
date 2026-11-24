---
id: expensive-operations
title: Expensive Operations / Large Uploads
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023, A06:2025]
cwe: [CWE-405, CWE-400, CWE-770]
severity: medium
last_verified: 2026-10-29
aliases: [expensive-search-reporting, large-upload-attacks]
---

# Expensive Operations / Large Uploads

## Why It Is a Threat

A single request triggers disproportionate work: unbounded search or report queries, full-table scans, huge exports, or very large uploads. A few requests can exhaust the backend (asymmetric DoS).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify costly endpoints (search, reports, exports, uploads); test broad queries, large page sizes, and large files against documented limits.

## How to Fix

- Enforce server-side page-size caps, query timeouts or cost limits, and upload size limits checked before buffering; queue or precompute heavy reports.
- Index filterable fields and rate-limit expensive endpoints per user. Map to API4:2023, ASVS 5.0 V15 Secure Coding and Architecture (15.1.3, 15.2.2) and ASVS 5.0 V5 File Handling (5.2.1).

## References

- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-405: Asymmetric Resource Consumption (Amplification)](https://cwe.mitre.org/data/definitions/405.html)
- [OWASP File Upload Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html)
