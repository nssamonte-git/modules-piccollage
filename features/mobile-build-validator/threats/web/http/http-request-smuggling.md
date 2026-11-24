---
id: http-request-smuggling
title: HTTP Request Smuggling / Desync
category: web-http
platforms: [web]
owasp_refs: [A06:2025, A02:2025, WSTG-INPV-15]
cwe: [CWE-444]
severity: high
last_verified: 2026-10-29
aliases: [http-desync, request-smuggling]
---

# HTTP Request Smuggling / Desync

## Why It Is a Threat

Front-end and back-end servers disagree on request boundaries (Content-Length vs Transfer-Encoding, HTTP/2-to-HTTP/1.1 downgrade translation, h2c upgrades), letting an attacker smuggle a hidden request that poisons the next user's response, bypasses front-end controls, or captures other users' requests and sessions. High impact, infrastructure-level.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), against non-production.

- Map the request chain (CDN, load balancer, proxy, app server) and whether HTTP/2 is downgraded to HTTP/1.1; probe for CL.TE, TE.CL, TE.TE, and H2 downgrade discrepancies with timing-based detection; confirm desync without disrupting other users (owner-approved).

## How to Fix

- Use HTTP/2 or HTTP/3 end to end where possible and avoid protocol downgrades; make every hop determine message length the same way and reject (and close the connection on) requests with both or ambiguous `Content-Length`/`Transfer-Encoding`.
- Reject connection-specific headers and CR/LF in HTTP/2 and HTTP/3 header fields, disable h2c upgrade at the edge unless required, and keep proxies and servers patched.
- Map to ASVS 5.0 V4.2 HTTP Message Structure Validation (V4.2.1 to V4.2.4).

## References

- [OWASP WSTG-INPV-15: Testing for HTTP Splitting Smuggling](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/15-Testing_for_HTTP_Splitting_Smuggling/)
- [CWE-444: Inconsistent Interpretation of HTTP Requests ('HTTP Request/Response Smuggling')](https://cwe.mitre.org/data/definitions/444.html)
- [RFC 9112: HTTP/1.1 (message body length)](https://www.rfc-editor.org/rfc/rfc9112.html)
- [CAPEC-33: HTTP Request Smuggling](https://capec.mitre.org/data/definitions/33.html)
