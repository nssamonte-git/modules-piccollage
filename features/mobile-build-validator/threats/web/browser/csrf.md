---
id: csrf
title: Cross-Site Request Forgery
category: web-browser
platforms: [web]
owasp_refs: [A01:2025, WSTG-SESS-05]
cwe: [CWE-352]
severity: high
last_verified: 2026-10-29
---

# Cross-Site Request Forgery

## Why It Is a Threat

A malicious site causes the victim's browser to send an authenticated state-changing request to your app using the victim's ambient cookies, performing actions (transfer, change email, delete) without consent. Only some browsers default cookies to `SameSite=Lax`, and `Lax` still allows top-level GET navigations and same-site (sibling subdomain) requests, so browser defaults are not a complete defense.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify state-changing endpoints that rely on cookie auth; replay them cross-site without a CSRF token and check token, `SameSite`, `Origin`, and `Sec-Fetch-Site` enforcement.
- Check for state changes on GET, and for endpoints that accept "simple" content types (form-encoded, `text/plain`) that avoid a CORS preflight.
- Source review by stack (`source-code-vapt`): Laravel `validateCsrfTokens(except: [...])` or `VerifyCsrfToken::$except` wildcards; CodeIgniter `csrf_protection` FALSE (3) or the `csrf` filter missing from `app/Config/Filters.php` (4); Django `@csrf_exempt` on session views.

## How to Fix

- Use the synchronizer token pattern (stateful apps) or a signed, session-bound (HMAC) double-submit cookie (stateless apps); avoid the naive unsigned double-submit pattern.
- Reject cross-site state-changing requests using Fetch Metadata (`Sec-Fetch-Site`) with an `Origin` check as fallback; never change state on GET; set `SameSite=Lax` or `Strict` explicitly on session cookies.
- Map to ASVS 5.0 V3.5 Browser Origin Separation (V3.5.1 to V3.5.3) and V3.3.2, and the CSRF Prevention Cheat Sheet.

## References

- [OWASP Cross-Site Request Forgery Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
- [OWASP WSTG: Testing for Cross Site Request Forgery (WSTG-SESS-05)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/05-Cross_Site_Request_Forgery/)
- [CWE-352: Cross-Site Request Forgery (CSRF)](https://cwe.mitre.org/data/definitions/352.html)
- [MDN: Sec-Fetch-Site header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Sec-Fetch-Site)
