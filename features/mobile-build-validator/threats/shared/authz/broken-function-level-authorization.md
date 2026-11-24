---
id: broken-function-level-authorization
title: Broken Function-Level Authorization
category: authz
platforms: [shared, web, backend]
owasp_refs: [API5:2023, A01:2025, WSTG-APIT-04]
cwe: [CWE-285]
severity: high
last_verified: 2026-10-29
aliases: [bfla]
---

# Broken Function-Level Authorization

## Why It Is a Threat

Privileged or administrative functions are reachable by users who should not have them, because the function checks authentication but not the caller's role/privilege. Attackers invoke admin actions (create user, change role, delete records) directly via the API.

## How to Find or Test

Authorized testing only, with low- and high-privilege synthetic accounts (follow the `smx-ai-vapt` authorization gate).

- Enumerate admin/privileged endpoints (often `/admin`, management verbs, or undocumented routes); call them with a low-privilege token.
- Test HTTP verb tampering (GET vs POST/PUT/DELETE) and method-override headers on the same resource.
- Compare the function list the UI exposes per role against what the API actually enforces.
- Source review by stack (`source-code-vapt`): Laravel `Gate::before` returning true broadly; CodeIgniter 4 legacy auto-routing (`$autoRoute = true`); Postgres functions left executable by `PUBLIC` and reachable through PostgREST `/rpc/`.

## How to Fix

- Enforce role/privilege checks server-side on every function, deny-by-default; group admin routes behind a verified authorization gate.
- Accept only the HTTP methods each route explicitly supports; do not infer privilege from client-supplied fields. Add tests asserting low-privilege principals are rejected. Map to API5:2023 and ASVS 5.0 V8 Authorization (8.2.1) and V4 API and Web Service (4.1.4).

## References

- [OWASP API5:2023 Broken Function Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa5-broken-function-level-authorization/)
- [OWASP WSTG-APIT-04: API Broken Function Level Authorization](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/12-API_Testing/04-API_Broken_Function_Level_Authorization/)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
- [CWE-285: Improper Authorization](https://cwe.mitre.org/data/definitions/285.html)
