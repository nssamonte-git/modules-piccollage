---
id: graphql-complexity
title: GraphQL Complexity Attacks
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023]
cwe: [CWE-770, CWE-400]
severity: medium
last_verified: 2026-10-29
---

# GraphQL Complexity Attacks

## Why It Is a Threat

A single GraphQL query with deep nesting, many aliases, batched operations, or fan-out resolvers can trigger enormous work and database load, causing denial of service. Introspection makes such queries easy to craft (`graphql-introspection-exposure`, `graphql-batching-abuse`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit bounded nested, aliased, and batched queries and measure cost; check for depth, amount, and complexity limits, timeouts, and resolver-level batching.

## How to Fix

- Enforce query depth, amount, and cost limits (or an allowlist of trusted persisted queries), pagination caps, timeouts, and per-client rate limits; cap batch and alias counts; use data loaders to avoid N+1 fan-out.
- Disable introspection in production unless the API is meant for other parties. Map to API4:2023 and ASVS 5.0 V4 API and Web Service (4.3.1, 4.3.2).

## References

- [OWASP GraphQL Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/GraphQL_Cheat_Sheet.html)
- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
