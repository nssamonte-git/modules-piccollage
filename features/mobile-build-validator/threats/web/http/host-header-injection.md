---
id: host-header-injection
title: Host-Header Injection
category: web-http
platforms: [web]
owasp_refs: [A02:2025, WSTG-INPV-17]
cwe: [CWE-644]
severity: medium
last_verified: 2026-10-29
---

# Host-Header Injection

## Why It Is a Threat

The app trusts the client-supplied `Host` (or `X-Forwarded-Host`, `Forwarded`) header to build absolute links, password-reset URLs, redirects, or routing and cache keys, letting an attacker poison password-reset links (`password-reset-abuse`), cache entries (`web-cache-poisoning`), or reach internal virtual hosts.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send requests with an unexpected `Host`, duplicate `Host`, absolute request URI, or `X-Forwarded-Host`/`Forwarded` value and observe whether it appears in generated links, emails, redirects, or cached responses, or changes routing.

## How to Fix

- Build absolute URLs from a server-side configured canonical host; never use the `Host` header for security decisions or reset links.
- Reject requests whose `Host` is not on an allowlist (default virtual host returns an error), and have the edge strip or overwrite forwarding headers so clients cannot set them.
- Map to ASVS 5.0 V4.1.3 (intermediary-set headers cannot be overridden) and the Forgot Password Cheat Sheet.

## References

- [OWASP WSTG-INPV-17: Testing for Host Header Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/17-Testing_for_Host_Header_Injection/)
- [OWASP Forgot Password Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html)
- [CWE-644: Improper Neutralization of HTTP Headers for Scripting Syntax](https://cwe.mitre.org/data/definitions/644.html)
