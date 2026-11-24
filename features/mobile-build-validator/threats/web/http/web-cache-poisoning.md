---
id: web-cache-poisoning
title: Web-Cache Poisoning
category: web-http
platforms: [web]
owasp_refs: [A02:2025, WSTG-INPV-17]
cwe: [CWE-349, CWE-444]
severity: high
last_verified: 2026-10-29
aliases: [cdn-cache-poisoning, cache-key-confusion]
---

# Web-Cache Poisoning

## Why It Is a Threat

An attacker gets a harmful response cached, through unkeyed inputs (forwarding headers, cookies, unkeyed query parameters, request bodies on GET) or request smuggling, so it is served to all subsequent users, delivering XSS, malicious redirects, or denial of service at scale through the CDN or cache. Cache-key confusion (normalization differences) broadens which inputs are exploitable.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), against non-production.

- Identify unkeyed request inputs that influence the response, using a unique cache-buster per test; check whether a modified response is cached and served to other clients, and test error responses (cached 4xx/5xx DoS).

## How to Fix

- Include every response-affecting input in the cache key (or `Vary` on it), or strip and normalize unkeyed inputs at the edge; never reflect untrusted headers into cacheable responses.
- Cache only explicitly cacheable responses, avoid caching error responses, and mark user-specific responses `no-store`.
- Map to ASVS 5.0 V4.1.3 and V14.2.5; see `host-header-injection`, `http-request-smuggling`.

## References

- [OWASP: Cache Poisoning](https://owasp.org/www-community/attacks/Cache_Poisoning)
- [RFC 9111: HTTP Caching](https://www.rfc-editor.org/rfc/rfc9111.html)
- [CWE-349: Acceptance of Extraneous Untrusted Data With Trusted Data](https://cwe.mitre.org/data/definitions/349.html)
- [MDN: Vary header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Vary)
