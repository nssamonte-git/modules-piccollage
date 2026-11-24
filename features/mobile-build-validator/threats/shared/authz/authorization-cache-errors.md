---
id: authorization-cache-errors
title: Authorization Cache Errors
category: authz
platforms: [shared, web, backend]
owasp_refs: [A01:2025]
cwe: [CWE-285, CWE-524]
severity: medium
last_verified: 2026-10-29
---

# Authorization Cache Errors

## Why It Is a Threat

Cached authorization decisions or cached responses are served incorrectly across users, roles, or tenants — granting access after a role/permission was revoked, or returning one user's authenticated response to another via a shared cache/CDN.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Change a principal's role/permissions and confirm old access is denied promptly (no stale grant from a decision cache or a long-lived self-contained token).
- Verify cache keys include principal/tenant/role; test CDN/proxy caching of authenticated responses for cross-user leakage (see `web-cache-deception`).

## How to Fix

- Key authorization and response caches by principal/tenant/role; invalidate on permission change, or where changes cannot apply immediately (e.g. claims in self-contained tokens) keep lifetimes short and add compensating controls.
- Send `Cache-Control: no-store` on sensitive per-user responses (at minimum `private`), and keep shared caches and CDNs from storing authenticated content. Map to ASVS 5.0 V8 Authorization (8.3.2) and V14 Data Protection (14.2.2, 14.3.2).

## References

- [CWE-524: Use of Cache Containing Sensitive Information](https://cwe.mitre.org/data/definitions/524.html)
- [MDN: Cache-Control header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cache-Control)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
