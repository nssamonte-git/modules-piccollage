---
id: same-origin-policy-bypass
title: Same-Origin-Policy Bypass
category: web-http
platforms: [web]
owasp_refs: [A02:2025, WSTG-CLNT-07]
cwe: [CWE-942, CWE-346]
severity: high
last_verified: 2026-10-29
---

# Same-Origin-Policy Bypass

## Why It Is a Threat

Cross-origin data is read despite the Same-Origin Policy through misconfigured CORS (reflected or `null` origins with credentials), JSONP endpoints and other cross-site script inclusion, legacy `crossdomain.xml`/`clientaccesspolicy.xml` policies, `document.domain` relaxation, or `postMessage` flaws, letting a malicious origin read authenticated responses. Overlaps `browser-security-misconfiguration` and `unsafe-postmessage`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Test CORS with arbitrary, suffix/prefix-matched, and `null` origins and check whether `Access-Control-Allow-Origin` reflects them with `Access-Control-Allow-Credentials: true`.
- Check for JSONP endpoints, authenticated data in script responses, legacy cross-domain policy files, and `document.domain` assignments.

## How to Fix

- Allowlist exact CORS origins (no reflection with credentials, never `null`, no `*` for authenticated data); remove JSONP and legacy cross-domain policy files; keep authorized data out of script responses.
- Stop setting `document.domain` (deprecated, and ignored in origin-keyed agent clusters); validate `postMessage` origins.
- Map to ASVS 5.0 V3.4.2 (CORS), V3.5.5 to V3.5.7, and V3.7.1; see `browser-security-misconfiguration`.

## References

- [OWASP WSTG: Testing Cross Origin Resource Sharing (WSTG-CLNT-07)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/07-Cross_Origin_Resource_Sharing/)
- [CWE-942: Permissive Cross-domain Security Policy with Untrusted Domains](https://cwe.mitre.org/data/definitions/942.html)
- [MDN: Cross-Origin Resource Sharing (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/CORS)
- [MDN: Same-origin policy](https://developer.mozilla.org/en-US/docs/Web/Security/Same-origin_policy)
