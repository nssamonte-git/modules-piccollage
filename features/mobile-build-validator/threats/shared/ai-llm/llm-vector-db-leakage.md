---
id: llm-vector-db-leakage
title: Vector / Embedding Weaknesses
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM08:2025]
cwe: [CWE-200, CWE-639]
severity: high
last_verified: 2026-10-29
aliases: [vector-database-data-leakage, embedding-leakage, rag-authorization-failure]
---

# Vector / Embedding Weaknesses

## Why It Is a Threat

RAG vector stores often hold a denormalized copy of data without per-user authorization or tenant filtering, so retrieval returns other users'/tenants' content into the prompt. Embeddings can be inverted to recover much of the source text, a shared index mixes contexts across users or data sources (`llm-sensitive-data-disclosure`), and poisoned or hidden-text documents in the store steer answers (`llm-data-poisoning`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with two synthetic users/tenants.

- Test whether retrieval enforces per-user/tenant authorization and filtering; attempt to retrieve another principal's documents via queries.
- Review who can write to the index, how sources are validated, and whether stale permissions persist after access changes.

## How to Fix

- Apply authorization/tenant filters to every retrieval (metadata filters scoped to the principal); partition indexes per tenant where needed (ASVS 5.0 V8.2.2 and V8.4.1); minimize and classify sensitive content before embedding.
- Validate and authenticate data sources, scan documents for hidden content, re-index on permission changes, and keep immutable logs of retrieval. See `tenant-isolation-failure`, `search-index-leakage`.

## References

- [OWASP LLM08:2025 Vector and Embedding Weaknesses](https://genai.owasp.org/llmrisk/llm082025-vector-and-embedding-weaknesses/)
- [OWASP Multi Tenant Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multi_Tenant_Security_Cheat_Sheet.html)
- [CWE-639: Authorization Bypass Through User-Controlled Key](https://cwe.mitre.org/data/definitions/639.html)
