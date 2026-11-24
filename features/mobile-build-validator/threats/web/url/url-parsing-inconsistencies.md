---
id: url-parsing-inconsistencies
title: URL Parsing Inconsistencies
category: web-url
platforms: [web]
owasp_refs: [A05:2025, A01:2025]
cwe: [CWE-436, CWE-22]
severity: high
last_verified: 2026-10-29
aliases: [path-normalization-bypass, unicode-normalization-attack]
---

# URL Parsing Inconsistencies

## Why It Is a Threat

Different components (CDN, proxy, framework router, URL library, allowlist) parse the same URL differently (for example RFC 3986 versus WHATWG URL rules), so an attacker crafts a URL that one layer treats as safe and another resolves to a sensitive target, bypassing access control, path-based routing, SSRF allowlists, and authentication via path, encoding, or Unicode normalization tricks.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Test path normalization (`../`, `..%2f`, double-encoding, `;` path parameters, trailing dots and slashes, backslashes) and Unicode/case normalization against access-control and routing decisions; compare proxy and app interpretation.
- For URL allowlists, test userinfo (`@`), backslash, and encoded host variants against the parser actually used to make the request.

## How to Fix

- Decode and canonicalize URLs and paths once, consistently, before any security decision; reject ambiguous or over-encoded input.
- Make all layers use the same parser and rules, and validate with the same library that later performs the request or routing.
- Map to ASVS 5.0 V1.5.3 (consistent parsers) and V1.1.1 (decode once); see `path-traversal`, `ssrf`.

## References

- [CWE-436: Interpretation Conflict](https://cwe.mitre.org/data/definitions/436.html)
- [OWASP WSTG: Test for Path Confusion (WSTG-CONF-13)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/13-Path_Confusion/)
- [RFC 3986: Uniform Resource Identifier (URI): Generic Syntax](https://www.rfc-editor.org/rfc/rfc3986.html)
- [OWASP Server Side Request Forgery Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html)
