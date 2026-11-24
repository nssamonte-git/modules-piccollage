---
id: session-hijacking
title: Session Hijacking
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-SESS-09, A07:2025]
cwe: [CWE-1462, CWE-287]
severity: high
last_verified: 2026-10-29
---

# Session Hijacking

## Why It Is a Threat

An attacker obtains a valid session identifier (via XSS, sniffing, malware, a phishing proxy, or prediction) and uses it to act as the victim without credentials. Weak session id entropy, long lifetimes, and missing invalidation widen the window.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Assess session id entropy, transport (HTTPS-only, HSTS), cookie attributes, idle/absolute timeout, and invalidation on logout/password change.
- Confirm a captured id cannot be reused after logout or from an anomalous context.

## How to Fix

- Use CSPRNG session ids with at least 128 bits of entropy verified by the backend, `Secure`/`HttpOnly`/`SameSite` cookies, HSTS, short idle and absolute timeouts, and server-side invalidation on logout and credential change.
- Detect use from new contexts and require re-authentication for sensitive actions. See `cookie-token-theft`. Map to ASVS 5.0 V7 Session Management (7.2, 7.3, 7.4) and V3 Web Frontend Security (3.3, 3.4.1).

## References

- [OWASP WSTG-SESS-09: Testing for Session Hijacking](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/09-Session_Hijacking)
- [OWASP Session Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html)
- [CAPEC-593: Session Hijacking](https://capec.mitre.org/data/definitions/593.html)
- [CWE-1462: Improper Session Management](https://cwe.mitre.org/data/definitions/1462.html)
