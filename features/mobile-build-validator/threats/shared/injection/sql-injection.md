---
id: sql-injection
title: SQL Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-05]
cwe: [CWE-89]
severity: high
last_verified: 2026-10-29
---

# SQL Injection

## Why It Is a Threat

Untrusted input is concatenated into SQL, letting an attacker alter the query to read, modify, or delete data, bypass authentication, or in some cases execute commands on the database host. High impact and widespread: CWE-89 is second in the 2025 CWE Top 25. ORMs do not remove the risk when raw-query, `ORDER BY`, or dynamic-identifier features are fed user input.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate; confirmation methodology: `smx-ai-vapt` `web-injection-recipes.md`).

- Send a syntax-breaking vs syntax-neutral value and compare responses/errors; confirm with a boolean-differential or bounded time-based check — never destructive/stacked statements.
- Review code for string-built SQL, ORM raw-query APIs, and dynamic sort/column parameters.
- Demonstrate access to a single non-sensitive marker row; do not dump tables.
- Source review by stack (`source-code-vapt`): Android `rawQuery(` or provider `query(` with concatenated selections; Laravel `DB::raw(`, `whereRaw(`, `orderByRaw(`, and `orderBy($request->input(...))`; PHP `mysqli_query` or `$pdo->query` with interpolation; CodeIgniter `$this->db->query("... $id")` (3) or `$db->query()` without binds and `RawSql` (4); Python `cursor.execute(f"...")` and SQLAlchemy `text(f"...")`; Django `.raw(`, `.extra(`, `RawSQL(`; Rust `sqlx::query(&format!(...))` and diesel `sql_query(format!(...))`; SQL `PREPARE ... FROM CONCAT(...)` and `EXEC(@sql)`; React Native and Flutter native modules or channels that build SQL from JS or Dart arguments.

## How to Fix

- Use parameterized queries / prepared statements or a vetted ORM's bound parameters; never build SQL by string concatenation, including inside stored procedures (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.4).
- Identifiers and keywords (table/column names, sort direction) cannot be bound as parameters: map them from a server-side allowlist.
- Validate and constrain input types, apply least-privilege database accounts, and add regression tests.

## References

- [OWASP SQL Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html)
- [OWASP Query Parameterization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Query_Parameterization_Cheat_Sheet.html)
- [OWASP WSTG v4.2 WSTG-INPV-05: Testing for SQL Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/05-Testing_for_SQL_Injection/)
- [CWE-89: Improper Neutralization of Special Elements used in an SQL Command ('SQL Injection')](https://cwe.mitre.org/data/definitions/89.html)
