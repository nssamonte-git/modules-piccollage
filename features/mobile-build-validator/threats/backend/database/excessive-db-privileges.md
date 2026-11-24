---
id: excessive-db-privileges
title: Excessive Database Privileges
category: backend-database
platforms: [backend]
owasp_refs: [A01:2025, A02:2025]
cwe: [CWE-250, CWE-269]
severity: medium
last_verified: 2026-10-29
---

# Excessive Database Privileges

## Why It Is a Threat

The application's database account has more rights than it needs (DDL, admin or owner rights, cross-schema access, file or OS command features), so an injection or app compromise escalates into dropping tables, reading every schema, or running database-level commands, turning a small bug into total data loss.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Compare the app account's grants with what the app actually uses; check for DDL, superuser or owner, file, and command-execution privileges and cross-database access.
- Source review by stack (`source-code-vapt`): Postgres application or PostgREST roles that are superuser, table owner, or `BYPASSRLS`; `GRANT ALL`, `GRANT ... ON ALL TABLES IN SCHEMA`, and broad `ALTER DEFAULT PRIVILEGES`.

## How to Fix

- Grant least privilege per service (DML on needed objects only, no DDL or superuser in app accounts); use a separate migration account; disable unused risky database features.
- Use distinct accounts per service or tenant where feasible. Map to ASVS 5.0 V13.2 Backend Communication Configuration (13.2.2); this limits `sql-injection` blast radius.

## References

- [OWASP Database Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Database_Security_Cheat_Sheet.html)
- [OWASP SQL Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html)
- [CWE-250: Execution with Unnecessary Privileges](https://cwe.mitre.org/data/definitions/250.html)
