---
id: cache-stampede
title: Cache Stampede
category: availability
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-400]
severity: medium
last_verified: 2026-10-29
aliases: [thundering-herd]
---

# Cache Stampede

## Why It Is a Threat

When a hot cache entry expires, or an attacker forces misses (cache-busting query parameters, mass invalidation), many concurrent requests recompute the same value at once and hammer the origin or database, causing overload and cascading failure (thundering herd).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review how expensive results are cached: TTLs that expire together, behavior on mass expiry or invalidation, and whether unknown query parameters create new cache keys.
- In non-production, expire a hot key under concurrent load and observe origin request fan-out.

## How to Fix

- Use request coalescing (single-flight) or a lock-and-recompute pattern, add random jitter to TTLs, and serve stale content while revalidating (`stale-while-revalidate`, `stale-if-error`).
- Normalize cache keys, pre-warm hot keys, and rate-limit invalidation endpoints. Map to ASVS 5.0 V15 Secure Coding and Architecture (15.2.2) and the infrastructure runbook.

## References

- [RFC 5861: HTTP Cache-Control Extensions for Stale Content](https://www.rfc-editor.org/rfc/rfc5861)
- [Microsoft Learn: Cache-Aside Pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/cache-aside)
- [CWE-400: Uncontrolled Resource Consumption](https://cwe.mitre.org/data/definitions/400.html)
