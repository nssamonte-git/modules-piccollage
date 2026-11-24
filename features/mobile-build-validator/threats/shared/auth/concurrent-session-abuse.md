---
id: concurrent-session-abuse
title: Concurrent-Session and Dormant-Account Abuse
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-SESS-11, A07:2025]
cwe: [CWE-1462, CWE-1464]
severity: medium
last_verified: 2026-10-29
aliases: [dormant-accounts, abandoned-accounts]
---

# Concurrent-Session and Dormant-Account Abuse

## Why It Is a Threat

Unlimited concurrent sessions let a stolen session persist alongside the owner's, often with no way to view or revoke other sessions. Dormant or abandoned accounts (former staff, unused logins) retain access with stale credentials and little monitoring, making them prime takeover targets.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check whether users and admins can list and revoke active sessions and devices; confirm a revoked or disabled account's sessions stop working immediately.
- Review the documented concurrent-session limit and the lifecycle for inactive accounts (disable/expire) and offboarding.

## How to Fix

- Define and enforce a concurrent-session policy; let users view and terminate sessions (after re-authenticating) and admins terminate them for any user; surface new-session alerts.
- Disable dormant accounts automatically, terminate all sessions when an account is disabled or deleted, and alert on reactivation. Map to ASVS 5.0 V7 Session Management (7.1.2, 7.4.2, 7.4.5, 7.5.2).

## References

- [OWASP WSTG-SESS-11: Testing for Concurrent Sessions](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/11-Concurrent_Sessions)
- [OWASP Session Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html)
- [CWE-1464: Improper Session Invalidation](https://cwe.mitre.org/data/definitions/1464.html)
- [CWE-1462: Improper Session Management](https://cwe.mitre.org/data/definitions/1462.html)
