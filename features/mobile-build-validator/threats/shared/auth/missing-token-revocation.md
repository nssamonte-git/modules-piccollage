---
id: missing-token-revocation
title: Missing Token Revocation
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-SESS-06, A07:2025]
cwe: [CWE-1464, CWE-1465, CWE-1466]
severity: high
last_verified: 2026-10-29
---

# Missing Token Revocation

## Why It Is a Threat

There is no way to invalidate active sessions or tokens, so after logout, password reset, factor change, role change, account disablement, or compromise, existing tokens keep working, defeating incident response and offboarding. Common with stateless JWTs and long-lived refresh tokens.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- After logout, password change, MFA change, account disablement, and role revocation, confirm whether previously issued sessions, access tokens, and refresh tokens still authenticate and authorize.

## How to Fix

- Invalidate server-side sessions on termination; for tokens, keep access tokens short-lived and revoke refresh tokens (OAuth revocation endpoint), or use a denylist, introspection, or token versioning bumped on credential/role change.
- Revoke on logout, reset, factor change, disablement, and suspected compromise; let users and admins terminate sessions and revoke grants; propagate federated logout (for example OIDC back-channel logout). Map to ASVS 5.0 V7 Session Management (7.4) and V10 OAuth and OIDC (10.4.9).

## References

- [OWASP WSTG-SESS-06: Testing for Logout Functionality](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/06-Logout_Functionality)
- [RFC 7009: OAuth 2.0 Token Revocation](https://www.rfc-editor.org/rfc/rfc7009.html)
- [OWASP Session Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html)
- [CWE-1464: Improper Session Invalidation](https://cwe.mitre.org/data/definitions/1464.html)
