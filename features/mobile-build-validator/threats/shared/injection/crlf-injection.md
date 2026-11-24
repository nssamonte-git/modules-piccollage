---
id: crlf-injection
title: CRLF Injection / HTTP Response Splitting
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-15]
cwe: [CWE-113, CWE-93]
severity: medium
last_verified: 2026-10-29
aliases: [http-response-splitting, http-header-injection]
---

# CRLF Injection / HTTP Response Splitting

## Why It Is a Threat

Carriage-return/line-feed characters in untrusted input reach HTTP headers or other line-delimited protocols, letting an attacker inject headers, split responses, set cookies, poison caches, or forge log entries. Modern frameworks usually reject CR/LF in header values, so the risk concentrates in custom header writing, raw sockets, proxies, and outbound requests the backend builds.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inject encoded CR/LF (`%0d%0a`, and Unicode/double-encoded variants) into values reflected into response headers (redirect `Location`, `Set-Cookie`, custom headers) and into values the backend places in outbound request headers.
- Observe whether an injected header or split response appears; check intermediaries (proxies, CDNs) as well as the origin.

## How to Fix

- Strip or reject CR, LF, and other control characters in any value placed into headers; use framework header APIs that reject control characters (ASVS 5.0 V1.2 Injection Prevention).
- When building outbound requests, validate header and URI values (ASVS 5.0 V4.2 HTTP Message Structure Validation), and reject HTTP/2 or HTTP/3 header fields containing CR/LF.
- Validate redirect targets and header values against allowlists.

## References

- [OWASP WSTG v4.2 WSTG-INPV-15: Testing for HTTP Splitting Smuggling](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/15-Testing_for_HTTP_Splitting_Smuggling/)
- [CWE-113: Improper Neutralization of CRLF Sequences in HTTP Headers ('HTTP Request/Response Splitting')](https://cwe.mitre.org/data/definitions/113.html)
- [CWE-93: Improper Neutralization of CRLF Sequences ('CRLF Injection')](https://cwe.mitre.org/data/definitions/93.html)
- [OWASP Community: HTTP Response Splitting](https://owasp.org/www-community/attacks/HTTP_Response_Splitting)
