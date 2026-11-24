---
id: exposed-api-docs
title: Exposed API Documentation / Schema
category: backend-api-inventory
platforms: [backend]
owasp_refs: [API9:2023, API8:2023, A02:2025]
cwe: [CWE-200]
severity: low
last_verified: 2026-10-29
---

# Exposed API Documentation / Schema

## Why It Is a Threat

Publicly reachable API docs and specs (Swagger or OpenAPI UI, GraphQL schema, internal API catalogs) hand attackers a complete map of endpoints, parameters, and admin routes, speeding discovery of weak or internal functionality. Interactive doc UIs that can send live requests add further risk. Pairs with `graphql-introspection-exposure`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check for reachable `/swagger`, `/openapi.json`, `/graphql` introspection, and internal API catalogs in production; assess whether they expose internal or admin endpoints.
- Source review by stack (`source-code-vapt`): PostgREST `openapi-mode = "ignore-privileges"` and internal tables placed in `db-schemas`.

## How to Fix

- Restrict docs and specs to authenticated internal users or non-production; publish separate public specs that omit internal and admin endpoints.
- Disable interactive doc UIs and introspection in production where not needed. Map to API9:2023 and ASVS 5.0 V15.2 Security Architecture and Dependencies (15.2.3) and V4.3 GraphQL (4.3.2).

## References

- [OWASP API9:2023 Improper Inventory Management](https://owasp.org/API-Security/editions/2023/en/0xa9-improper-inventory-management/)
- [OWASP API8:2023 Security Misconfiguration](https://owasp.org/API-Security/editions/2023/en/0xa8-security-misconfiguration/)
- [OWASP REST Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
