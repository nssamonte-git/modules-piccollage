---
id: graphql-field-suggestion-leakage
title: GraphQL Field-Suggestion Information Leakage
category: backend-graphql
platforms: [backend]
owasp_refs: [API8:2023, API9:2023]
cwe: [CWE-209, CWE-200]
severity: low
last_verified: 2026-10-29
---

# GraphQL Field-Suggestion Information Leakage

## Why It Is a Threat

Even with introspection disabled, "did you mean" field suggestions in error messages let an attacker reconstruct the schema by probing field names, leaking internal or admin fields and operations that aid further attack.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send queries with near-miss field names and observe whether suggestion hints reveal valid field or type names.

## How to Fix

- Disable field suggestions in production and return generic errors; pair with disabled introspection (`graphql-introspection-exposure`).
- Avoid verbose, schema-revealing errors. Map to API8:2023, ASVS 5.0 V13.4 Unintended Information Leakage and V16.5 Error Handling, and `verbose-errors`.

## References

- [OWASP GraphQL Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/GraphQL_Cheat_Sheet.html)
- [OWASP API8:2023 Security Misconfiguration](https://owasp.org/API-Security/editions/2023/en/0xa8-security-misconfiguration/)
- [CWE-209: Generation of Error Message Containing Sensitive Information](https://cwe.mitre.org/data/definitions/209.html)
