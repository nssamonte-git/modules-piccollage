---
id: insecure-pagination
title: Insecure Pagination Exposing Data
category: backend-api-authz
platforms: [backend]
owasp_refs: [API1:2023, API4:2023]
cwe: [CWE-639, CWE-200, CWE-770]
severity: medium
last_verified: 2026-10-29
---

# Insecure Pagination Exposing Data

## Why It Is a Threat

Pagination implemented with predictable cursors or offsets and weak per-record authorization lets an attacker walk through other users' or tenants' records by manipulating page tokens, limits, or offsets. It is a bulk variant of `idor-bola` that quietly leaks large datasets, and an uncapped page size also lets one request pull or compute far more than intended.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with synthetic accounts.

- Manipulate page cursors, offsets, and limits and confirm results stay scoped to the authorized principal or tenant; test very large page sizes and predictable or reusable cursors.

## How to Fix

- Scope every page query by the authenticated principal and tenant; use opaque, signed or server-validated cursors bound to the caller; enforce a maximum page size.
- Authorize each record, not just the first page. Map to API1:2023, API4:2023, and ASVS 5.0 V8.2 General Authorization Design (8.2.2).

## References

- [OWASP API1:2023 Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa1-broken-object-level-authorization/)
- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [OWASP Insecure Direct Object Reference Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Insecure_Direct_Object_Reference_Prevention_Cheat_Sheet.html)
- [CWE-639: Authorization Bypass Through User-Controlled Key](https://cwe.mitre.org/data/definitions/639.html)
