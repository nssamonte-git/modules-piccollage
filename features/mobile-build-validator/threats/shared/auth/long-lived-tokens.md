---
id: long-lived-tokens
title: Long-Lived Tokens
category: auth
platforms: [shared, web, backend, android, ios]
owasp_refs: [WSTG-SESS-07, A07:2025]
cwe: [CWE-1463]
severity: medium
last_verified: 2026-10-29
---

# Long-Lived Tokens

## Why It Is a Threat

Access tokens, refresh tokens, API tokens, or sessions with very long (or no) expiry remain valid long after issuance, so a single theft grants extended access and undermines logout, role changes, and offboarding.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inspect token `exp`, refresh-token lifetime, and session idle/absolute limits; confirm whether old tokens still work hours or days later and after logout or permission changes.

## How to Fix

- Keep access tokens short-lived; give refresh tokens rotation or sender-constraint plus an absolute expiry; enforce documented idle and absolute session limits (NIST SP 800-63B-4 suggests at most 1 hour idle and 24 hours overall at AAL2).
- Re-evaluate authorization on each use rather than trusting claims in a long-lived token; expire API tokens and personal access tokens by default. Map to ASVS 5.0 V7 Session Management (7.3) and V10 OAuth and OIDC (10.4.8).

## References

- [OWASP WSTG-SESS-07: Testing Session Timeout](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/07-Session_Timeout)
- [OWASP Session Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [CWE-1463: Insufficient Session Timeout Value](https://cwe.mitre.org/data/definitions/1463.html)
