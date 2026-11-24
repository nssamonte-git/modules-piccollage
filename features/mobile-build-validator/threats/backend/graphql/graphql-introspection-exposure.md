---
id: graphql-introspection-exposure
title: GraphQL Introspection Exposure
category: backend-graphql
platforms: [backend]
owasp_refs: [API8:2023, API9:2023, A02:2025]
cwe: [CWE-200]
severity: medium
last_verified: 2026-10-29
---

# GraphQL Introspection Exposure

## Why It Is a Threat

Introspection enabled in production lets anyone download the full schema (every type, query, mutation, and field, including internal and admin operations), giving attackers a complete map to find weak or unauthorized resolvers (`graphql-resolver-authorization`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send an introspection query (`__schema`) to the production endpoint and confirm whether the full schema is returned.

## How to Fix

- Disable introspection in production unless the API is meant for third parties (or restrict it to authenticated internal users); ship the schema to trusted clients out-of-band.
- Do not rely on hiding the schema as the only control; still authorize every resolver. Map to API8:2023 and ASVS 5.0 V4.3 GraphQL (4.3.2).

## References

- [OWASP GraphQL Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/GraphQL_Cheat_Sheet.html)
- [OWASP WSTG: Testing GraphQL (WSTG-APIT-99)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/12-API_Testing/99-GraphQL)
- [OWASP API8:2023 Security Misconfiguration](https://owasp.org/API-Security/editions/2023/en/0xa8-security-misconfiguration/)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
