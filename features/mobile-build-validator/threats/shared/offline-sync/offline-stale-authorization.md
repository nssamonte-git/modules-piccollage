---
id: offline-stale-authorization
title: Stale Offline Authorization
category: offline-sync
platforms: [shared, android, ios]
owasp_refs: [A01:2025, MASVS-AUTH]
cwe: [CWE-1464, CWE-862]
severity: high
last_verified: 2026-10-29
aliases: [offline-privilege-abuse, unauthorized-offline-access]
---

# Stale Offline Authorization

## Why It Is a Threat

Offline-capable apps cache permissions/roles/entitlements that go stale: a revoked user, downgraded role, or expired entitlement still works offline, letting someone act with authority that was withdrawn. Cached credentials and long-lived tokens may also keep granting access after server-side revocation (`missing-token-revocation`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with test accounts.

- Revoke access or change a role server-side, then exercise the app offline; confirm whether cached authorization still permits actions and for how long.
- On reconnect, check whether queued actions from the revoked principal are accepted.

## How to Fix

- Bound offline authorization with short cache lifetimes and signed, expiring entitlements; apply authorization changes immediately where possible and document mitigations where not (ASVS 5.0 V8.3.2).
- Re-check every queued action server-side on reconnect and reject actions from revoked principals; require online authorization for sensitive actions; invalidate cached sessions and credentials on revocation (ASVS 5.0 V7.4). See `missing-token-revocation`.

## References

- [CWE-1464: Improper Session Invalidation](https://cwe.mitre.org/data/definitions/1464.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP MASVS-AUTH](https://mas.owasp.org/MASVS/07-MASVS-AUTH/)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
