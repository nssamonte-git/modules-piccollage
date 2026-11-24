---
id: search-index-leakage
title: Search-Index Data Leakage
category: backend-database
platforms: [backend]
owasp_refs: [A01:2025, A02:2025]
cwe: [CWE-200, CWE-668]
severity: medium
last_verified: 2026-10-29
---

# Search-Index Data Leakage

## Why It Is a Threat

Search engines (Elasticsearch, OpenSearch, Algolia, Azure AI Search) often hold a denormalized copy of data without the source database's authorization or tenant filters, so queries return other users' or tenants' records, or the index is exposed directly on the network. Related: `tenant-isolation-failure`, `public-data-store`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with synthetic accounts.

- Check whether search queries enforce per-user and per-tenant filters server-side; confirm the index is not network-reachable or queryable without authentication, and that client-side search keys are restricted.

## How to Fix

- Apply authorization and tenant filters to every search query server-side (security trimming or document-level security); index only necessary fields; secure the search cluster with authentication and a private network.
- Re-index or update permissions when access changes. Map to API1:2023 and ASVS 5.0 V8.2 General Authorization Design (8.2.2) and V8.4 (8.4.1).

## References

- [Microsoft Learn: Security filter pattern in Azure AI Search](https://learn.microsoft.com/en-us/azure/search/search-security-trimming-for-azure-search)
- [Amazon OpenSearch Service: Fine-grained access control](https://docs.aws.amazon.com/opensearch-service/latest/developerguide/fgac.html)
- [CWE-668: Exposure of Resource to Wrong Sphere](https://cwe.mitre.org/data/definitions/668.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
