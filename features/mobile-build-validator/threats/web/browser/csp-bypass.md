---
id: csp-bypass
title: Content Security Policy Bypass
category: web-browser
platforms: [web]
owasp_refs: [A02:2025, WSTG-CONF-12]
cwe: [CWE-693]
severity: medium
last_verified: 2026-10-29
---

# Content Security Policy Bypass

## Why It Is a Threat

A weak or bypassable CSP fails to contain XSS: `unsafe-inline`/`unsafe-eval`, wildcard or broad host allowlists, trusted-but-abusable CDNs and JSONP endpoints on allowlisted hosts, reused or predictable nonces, missing `object-src`/`base-uri`, or a policy left in `Report-Only` mode let injected script run despite the policy.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Collect the enforced policy (header and `<meta>`) per page; flag unsafe keywords, wildcards, scheme-only sources (`https:`, `data:`), allowlisted hosts that serve JSONP or user content, and `Report-Only` without an enforced policy.
- Confirm nonces change on every response, and check `object-src`, `base-uri`, and `frame-ancestors`.

## How to Fix

- Use a strict nonce- or hash-based policy with `'strict-dynamic'` instead of host allowlists; remove `unsafe-inline`/`unsafe-eval`; set `object-src 'none'` and `base-uri 'none'` (or `'self'`); generate a fresh random nonce per response.
- Add Trusted Types (`require-trusted-types-for 'script'`) to lock down DOM sinks, and configure violation reporting.
- Treat CSP as defense in depth, not the primary XSS fix (`xss`, `dom-xss`). Map to ASVS 5.0 V3.4.3 and V3.4.7, and the CSP Cheat Sheet.

## References

- [OWASP Content Security Policy Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Content_Security_Policy_Cheat_Sheet.html)
- [OWASP WSTG: Test for Content Security Policy (WSTG-CONF-12)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/12-Content_Security_Policy/)
- [W3C: Content Security Policy Level 3](https://www.w3.org/TR/CSP3/)
- [CWE-693: Protection Mechanism Failure](https://cwe.mitre.org/data/definitions/693.html)
