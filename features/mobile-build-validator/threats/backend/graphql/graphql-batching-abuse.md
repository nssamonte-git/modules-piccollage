---
id: graphql-batching-abuse
title: GraphQL Batching / Alias Amplification
category: backend-graphql
platforms: [backend]
owasp_refs: [API4:2023]
cwe: [CWE-770, CWE-799, CWE-307]
severity: medium
last_verified: 2026-10-29
aliases: [alias-based-amplification, batch-query-abuse, circular-query-attack]
---

# GraphQL Batching / Alias Amplification

## Why It Is a Threat

Query batching and field aliasing let one HTTP request perform thousands of operations (for example many aliased `login` attempts or expensive lookups), bypassing per-request rate limits to brute-force, enumerate, or exhaust resources. Circular fragments and deeply nested queries amplify cost further. Complements `graphql-complexity`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send batched or aliased queries (many aliases of a sensitive or costly operation) and confirm whether per-operation limits and rate limiting apply, not just per-request.

## How to Fix

- Limit operations and aliases per request and apply cost-based rate limiting per operation; disable or cap batching; protect sensitive mutations such as login with per-account attempt limits.
- Reject circular fragments (forbidden by the GraphQL spec) and enforce depth and complexity limits (`graphql-complexity`). Map to API4:2023 and ASVS 5.0 V4.3 GraphQL (4.3.1) and V2.4 Anti-automation.

## References

- [OWASP GraphQL Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/GraphQL_Cheat_Sheet.html)
- [OWASP WSTG: Testing GraphQL (WSTG-APIT-99)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/12-API_Testing/99-GraphQL)
- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
