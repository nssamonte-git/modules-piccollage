---
id: web-cache-deception
title: Web-Cache Deception
category: web-http
platforms: [web]
owasp_refs: [A02:2025, WSTG-CONF-13]
cwe: [CWE-524]
severity: medium
last_verified: 2026-10-29
---

# Web-Cache Deception

## Why It Is a Threat

An attacker lures a victim to a URL that the cache treats as static but the origin maps to a private, authenticated page (e.g. `/account/profile.css`, or paths using delimiters, encoded characters, or dot-segments that the cache and origin normalize differently). The cache stores the victim's response and the attacker retrieves it, leaking PII and session content.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Append static-looking extensions, path segments, delimiters (`;`, `%2F`, `%23`), and dot-segments to authenticated endpoints; check whether the origin still returns private content and whether the cache stores it and serves it to a second, unauthenticated client (use owner-approved test accounts).

## How to Fix

- Cache only by explicit origin `Cache-Control` and expected `Content-Type`, not by URL extension; mark authenticated responses `Cache-Control: no-store` (or `private`).
- Return 404 or a redirect for non-existent paths instead of serving the parent page, and align path normalization between cache and origin.
- Map to ASVS 5.0 V14.2.5 (cache only expected content types) and V14.2.2.

## References

- [OWASP WSTG: Test for Path Confusion (WSTG-CONF-13)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/13-Path_Confusion/)
- [CWE-524: Use of Cache Containing Sensitive Information](https://cwe.mitre.org/data/definitions/524.html)
- [RFC 9111: HTTP Caching](https://www.rfc-editor.org/rfc/rfc9111.html)
