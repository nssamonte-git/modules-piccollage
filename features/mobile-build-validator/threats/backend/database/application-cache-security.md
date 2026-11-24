---
id: application-cache-security
title: Application Cache Leakage / Poisoning
category: backend-database
platforms: [backend]
owasp_refs: [A01:2025, A02:2025]
cwe: [CWE-524, CWE-306, CWE-345]
severity: medium
last_verified: 2026-10-29
aliases: [cache-data-leakage, cache-poisoning-backend, cache-key-collision]
---

# Application Cache Leakage / Poisoning

## Why It Is a Threat

Server-side caches (Redis, Valkey, Memcached) leak data when keys omit the principal or tenant (one user's cached response served to another, a cache-key collision), expose data through an unauthenticated or network-reachable cache, or are poisoned with attacker-controlled values the application later trusts. Web edge variant: `web-cache-poisoning`; authorization angle: `authorization-cache-errors`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review cache keys for principal and tenant scoping and whether the cache requires authentication and TLS and sits on a private network; test for cross-user cache hits and attacker-writable keys.

## How to Fix

- Key caches by principal and tenant; avoid caching sensitive cross-user data under shared keys; enable cache authentication, TLS, and network isolation.
- Validate values read from cache as untrusted and sanitize data written to it. Map to ASVS 5.0 V14.2 General Data Protection (14.2.2), V1.3 Sanitization (1.3.9), and V13.2 Backend Communication Configuration.

## References

- [CWE-524: Use of Cache Containing Sensitive Information](https://cwe.mitre.org/data/definitions/524.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [Amazon ElastiCache: Authenticating with the Valkey and Redis OSS AUTH command](https://docs.aws.amazon.com/AmazonElastiCache/latest/dg/auth.html)
