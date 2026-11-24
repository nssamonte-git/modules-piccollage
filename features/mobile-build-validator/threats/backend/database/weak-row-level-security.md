---
id: weak-row-level-security
title: Weak Row-Level Security
category: backend-database
platforms: [backend]
owasp_refs: [A01:2025, API1:2023]
cwe: [CWE-285, CWE-639]
severity: high
last_verified: 2026-10-29
aliases: [multi-tenant-table-leakage]
---

# Weak Row-Level Security

## Why It Is a Threat

When tenant or owner isolation depends only on application query filters (no database row-level security), a missing `WHERE tenant_id = ?`, an injection, or a direct database path exposes other tenants' rows. Where RLS exists, it can still fail if the app connects as a role the policies do not apply to, or if a pooled connection carries the previous request's tenant context. Related: `tenant-isolation-failure`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with two synthetic tenants.

- Review whether tenant scoping is enforced only in app code; test queries and paths (reports, exports, admin tools) for cross-tenant rows; check RLS policies, the role the app connects as, and how tenant context is set per request.
- Source review by stack (`source-code-vapt`): Postgres tables in PostgREST-exposed schemas without `ENABLE ROW LEVEL SECURITY`, policies `USING (true)`, `FOR INSERT` or `FOR UPDATE` policies without `WITH CHECK`, and views over RLS tables created without `security_invoker = true` (Postgres 15+).

## How to Fix

- Enforce row-level security at the database (RLS policies or per-tenant schemas) as a backstop to app filters; set the tenant context from the authenticated session on every request and clear it before a connection is reused.
- Connect as a non-owner role that the policies apply to, and test cross-tenant access on every data path. Map to API1:2023 and ASVS 5.0 V8.4 Other Authorization Considerations (8.4.1).

## References

- [OWASP Multi-Tenant Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multi_Tenant_Security_Cheat_Sheet.html)
- [Microsoft Learn: Row-Level Security (SQL Server)](https://learn.microsoft.com/en-us/sql/relational-databases/security/row-level-security)
- [AWS Prescriptive Guidance: Row-level security recommendations](https://docs.aws.amazon.com/prescriptive-guidance/latest/saas-multitenant-managed-postgresql/rls.html)
- [CWE-639: Authorization Bypass Through User-Controlled Key](https://cwe.mitre.org/data/definitions/639.html)
