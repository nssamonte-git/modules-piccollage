---
id: tenant-isolation-failure
title: Tenant-Isolation Failure
category: authz
platforms: [shared, web, backend]
owasp_refs: [A01:2025, API1:2023]
cwe: [CWE-639, CWE-668]
severity: high
last_verified: 2026-10-29
aliases: [cross-tenant-data-exposure]
---

# Tenant-Isolation Failure

## Why It Is a Threat

In a multi-tenant system, one tenant can read or modify another tenant's data because queries, caches, files, or background jobs are not consistently scoped to the tenant. Causes mass cross-customer data breaches and compliance failures.

## How to Find or Test

Authorized testing only, with two synthetic tenants (A and B) (follow the `smx-ai-vapt` authorization gate).

- As tenant A, reference tenant B's ids on read/list/update/delete/export and secondary endpoints.
- Probe shared infrastructure: caches keyed without tenant, file/object paths, search indexes, queued jobs, and reports.
- Check subdomain/host- or header-based tenant resolution for spoofable inputs.
- Source review by stack (`source-code-vapt`): Laravel queries and `Rule::exists` not scoped to the tenant; Postgres `SECURITY DEFINER` functions or RLS policies that skip tenant checks.

## How to Fix

- Scope every data path by a server-derived tenant id (row-level filters or database row-level security, per-tenant keys/prefixes, tenant-aware cache keys and queues).
- Resolve the tenant from the authenticated session, not client-controlled headers; consider per-tenant encryption keys for high-sensitivity data. Add cross-tenant regression tests. Map to API1:2023 and ASVS 5.0 V8 Authorization (8.4.1).

## References

- [OWASP Multi Tenant Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multi_Tenant_Security_Cheat_Sheet.html)
- [OWASP API1:2023 Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa1-broken-object-level-authorization/)
- [CWE-668: Exposure of Resource to Wrong Sphere](https://cwe.mitre.org/data/definitions/668.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
