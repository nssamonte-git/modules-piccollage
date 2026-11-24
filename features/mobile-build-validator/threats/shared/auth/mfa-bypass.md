---
id: mfa-bypass
title: MFA Bypass
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHN-11, A07:2025]
cwe: [CWE-288, CWE-287, CWE-305]
severity: high
last_verified: 2026-10-29
---

# MFA Bypass

## Why It Is a Threat

A flaw lets an attacker skip the second factor: an endpoint that issues a session before MFA, forced browsing past the MFA step, a client-trusted "MFA passed" flag, OTPs not bound to the user or login attempt, "remember device" abuse, a password reset that disables MFA, or missing MFA on some paths (API, legacy, social login). Reduces auth to a single factor.

## How to Find or Test

Authorized testing only, with a synthetic MFA-enabled account (follow the `smx-ai-vapt` authorization gate).

- After the password step, attempt to reach authenticated endpoints directly without completing MFA; test all entry points (API, mobile, SSO, recovery, MFA disable/re-enroll).
- Check whether MFA state is enforced server-side per request and whether a code from one session or account is accepted in another.

## How to Fix

- Enforce MFA server-side on every authentication path before issuing an authenticated session; a partial session can reach only the MFA step. Bind codes to the original login attempt and use them once.
- Require re-authentication to disable or change factors, keep reset from bypassing MFA, and scope "remember device" tightly; check IdP `amr`/`acr` for federated logins. Map to ASVS 5.0 V6 Authentication (6.3.4, 6.4.3, 6.6.2, 6.8.4) and V7 Session Management (7.5.1).

## References

- [OWASP WSTG-ATHN-11: Testing Multi-Factor Authentication](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/11-Multi-Factor_Authentication)
- [OWASP Multifactor Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)
- [CWE-288: Authentication Bypass Using an Alternate Path or Channel](https://cwe.mitre.org/data/definitions/288.html)
- [CWE-287: Improper Authentication](https://cwe.mitre.org/data/definitions/287.html)
