---
id: insecure-stored-procedures
title: Insecure Stored Procedures
category: backend-database
platforms: [backend]
owasp_refs: [A05:2025]
cwe: [CWE-89, CWE-250]
severity: medium
last_verified: 2026-10-29
---

# Insecure Stored Procedures

## Why It Is a Threat

Stored procedures are not automatically safe. Procedures that build and execute dynamic SQL from their inputs are injectable (`sql-injection`) inside the database, and procedures running with elevated or definer rights can let a caller perform actions beyond their own privileges.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review procedures for dynamic SQL built by concatenation and for definer or elevated execution rights; test parameters for injection.
- Source review by stack (`source-code-vapt`): Postgres PL/pgSQL `EXECUTE '...' || param` or `format('%s', param)` instead of `%I`/`%L` or `EXECUTE ... USING`; MySQL `PREPARE ... FROM CONCAT(...)`; SQL Server `EXEC(@sql)`.

## How to Fix

- Use parameters inside procedures; avoid dynamic SQL, or bind values through the engine's parameterized execution call and allowlist any identifiers.
- Run procedures with least privilege (invoker rights where appropriate) and grant execute only to the roles that need it. Map to ASVS 5.0 V1.2 Injection Prevention and V13.2.2; see `excessive-db-privileges`.

## References

- [OWASP SQL Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html)
- [Microsoft Learn: SQL Injection (SQL Server)](https://learn.microsoft.com/en-us/sql/relational-databases/security/sql-injection)
- [CWE-89: Improper Neutralization of Special Elements used in an SQL Command](https://cwe.mitre.org/data/definitions/89.html)
