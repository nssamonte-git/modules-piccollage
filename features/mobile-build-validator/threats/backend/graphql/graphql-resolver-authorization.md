---
id: graphql-resolver-authorization
title: GraphQL Resolver Authorization Failure
category: backend-graphql
platforms: [backend]
owasp_refs: [API1:2023, API3:2023, API5:2023]
cwe: [CWE-285, CWE-639]
severity: high
last_verified: 2026-10-29
aliases: [over-permissive-graphql-fields]
---

# GraphQL Resolver Authorization Failure

## Why It Is a Threat

Authorization is enforced at the top-level query but not on nested resolvers, fields, mutations, or generic `node(id:)` lookups, so an attacker reaches sensitive data through relationships (for example `me { friends { paymentMethods } }`) or over-permissive fields. It is a GraphQL form of `idor-bola` and `broken-property-level-authorization`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with two synthetic accounts.

- Traverse nested fields and relationships to other users' or tenants' objects, fetch objects by global id, and query sensitive fields; confirm per-object and per-field authorization.

## How to Fix

- Enforce authorization in the business layer called by every resolver (object- and field-level), scoped to the authenticated principal and tenant; use dataloaders that carry auth context.
- Restrict sensitive fields by role. Map to API1:2023 and ASVS 5.0 V8.2 General Authorization Design (8.2.2, 8.2.3).

## References

- [OWASP GraphQL Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/GraphQL_Cheat_Sheet.html)
- [OWASP API1:2023 Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa1-broken-object-level-authorization/)
- [OWASP API3:2023 Broken Object Property Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa3-broken-object-property-level-authorization/)
- [OWASP WSTG: Testing GraphQL (WSTG-APIT-99)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/12-API_Testing/99-GraphQL)
