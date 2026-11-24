---
id: session-fixation
title: Session Fixation
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-SESS-03, A07:2025]
cwe: [CWE-384]
severity: high
last_verified: 2026-10-29
---

# Session Fixation

## Why It Is a Threat

The application keeps the same session identifier before and after authentication, so an attacker who plants a known session id (via a link, an injected cookie, or a cookie set from a sibling subdomain) is logged in as the victim once they authenticate.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Capture the pre-login session id, authenticate, and check whether the id changes; attempt to set a session id via URL/cookie and see if it survives login.

## How to Fix

- Issue a new session token on every authentication and re-authentication (login, step-up) and invalidate the pre-auth one; reject session ids the server did not issue.
- Use `__Host-`-prefixed `HttpOnly`/`Secure`/`SameSite` cookies and never accept session ids from URLs. Map to ASVS 5.0 V7 Session Management (7.2.4) and V3 Web Frontend Security (3.3).

## References

- [OWASP WSTG-SESS-03: Testing for Session Fixation](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/03-Session_Fixation)
- [OWASP Session Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html)
- [CWE-384: Session Fixation](https://cwe.mitre.org/data/definitions/384.html)
- [CAPEC-61: Session Fixation](https://capec.mitre.org/data/definitions/61.html)
