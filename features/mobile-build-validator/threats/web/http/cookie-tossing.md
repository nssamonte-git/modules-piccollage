---
id: cookie-tossing
title: Cookie Tossing / Shadowing
category: web-http
platforms: [web]
owasp_refs: [A02:2025, A07:2025]
cwe: [CWE-565, CWE-384]
severity: medium
last_verified: 2026-10-29
aliases: [cookie-shadowing, subdomain-cookie-injection]
---

# Cookie Tossing / Shadowing

## Why It Is a Threat

A compromised or attacker-controlled subdomain (or a sibling app on the same site) sets cookies scoped to the parent domain that shadow or override the app's cookies (same name, more specific path), enabling session fixation, CSRF-token overwrite, or logging the victim into an attacker's account. `SameSite` does not help, because sibling subdomains are same-site.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory which subdomains can set cookies on the parent domain (user content, third-party hosted, takeover-prone); test whether a parent-domain cookie with the same name overrides the app's cookie, and how the server picks between duplicates.

## How to Fix

- Name security cookies with the `__Host-` prefix (requires `Secure`, `Path=/`, and no `Domain`), or `__Secure-` where sharing is required; validate critical cookies server-side and reject unexpected duplicates.
- Regenerate the session on login, host untrusted content on a separate registrable domain, and fix subdomain takeovers.
- Map to ASVS 5.0 V3.3.1 and V3.3.3 (cookie prefixes), V3.5.4, and V7.2.4; see `session-fixation`.

## References

- [MDN: Set-Cookie header (cookie prefixes)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Set-Cookie)
- [IETF draft-ietf-httpbis-rfc6265bis: Cookies: HTTP State Management Mechanism](https://datatracker.ietf.org/doc/draft-ietf-httpbis-rfc6265bis/)
- [CWE-565: Reliance on Cookies without Validation and Integrity Checking](https://cwe.mitre.org/data/definitions/565.html)
- [OWASP WSTG: Testing for Cookies Attributes (WSTG-SESS-02)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/02-Cookies_Attributes/)
