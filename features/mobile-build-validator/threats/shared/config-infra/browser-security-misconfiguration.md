---
id: browser-security-misconfiguration
title: CORS and Security-Header Misconfiguration
category: config-infra
platforms: [shared, web]
owasp_refs: [A02:2025]
cwe: [CWE-942, CWE-1021, CWE-693]
severity: medium
last_verified: 2026-10-29
aliases: [misconfigured-cors, missing-browser-security-headers]
---

# CORS and Security-Header Misconfiguration

## Why It Is a Threat

A permissive CORS policy that echoes any `Origin`, trusts `null`, or uses loose suffix/regex matching while also sending `Access-Control-Allow-Credentials: true` lets a malicious site read authenticated responses. Browsers refuse `*` together with credentials, but `*` still exposes endpoints that rely on network position or non-cookie auth. Missing or weak security headers (CSP, HSTS, `X-Content-Type-Options`, CSP `frame-ancestors`, `Referrer-Policy`) remove browser defenses against XSS, clickjacking, MIME sniffing, and HTTPS downgrade. Pairs with the `smx-ai-vapt` client-side-security reference.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send requests with arbitrary, `null`, and look-alike origins; flag any response that reflects them in `Access-Control-Allow-Origin` with credentials allowed, and review preflight handling.
- Inspect responses for missing or weak CSP (`unsafe-inline`, broad sources), short or missing HSTS, missing `nosniff`, and missing `frame-ancestors`.
- Source review by stack (`source-code-vapt`): Laravel `config/cors.php` `allowed_origins => ['*']` with `supports_credentials`; FastAPI `CORSMiddleware(allow_origins=["*"], allow_credentials=True)`; Rust `CorsLayer::permissive()` or `allow_origin(Any)` with credentials; PostgREST `server-cors-allowed-origins` unset for cookie clients.

## How to Fix

- Validate `Origin` against an exact server-side allowlist; never reflect it or allow `null` with credentials; send `Vary: Origin`.
- Set a strict nonce- or hash-based CSP with `frame-ancestors`, HSTS with a max-age of at least one year, `X-Content-Type-Options: nosniff`, and a `Referrer-Policy`. Map to A02:2025 and ASVS 5.0 V3 Web Frontend Security (V3.4 Browser Security Mechanism Headers).

## References

- [OWASP HTTP Headers Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/HTTP_Headers_Cheat_Sheet.html)
- [CWE-942: Permissive Cross-domain Security Policy with Untrusted Domains](https://cwe.mitre.org/data/definitions/942.html)
- [MDN: Cross-Origin Resource Sharing (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/CORS)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
