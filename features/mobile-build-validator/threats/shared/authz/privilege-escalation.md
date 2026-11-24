---
id: privilege-escalation
title: Privilege Escalation (Horizontal and Vertical)
category: authz
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, API5:2023, WSTG-ATHZ-03]
cwe: [CWE-269]
severity: high
last_verified: 2026-10-29
aliases: [horizontal-privilege-escalation, vertical-privilege-escalation, role-permission-manipulation]
---

# Privilege Escalation (Horizontal and Vertical)

## Why It Is a Threat

A user gains access beyond their level: horizontally (another user's data/actions at the same tier) or vertically (a higher tier, e.g. user → admin). Achieved by manipulating roles, ids, tokens, or workflow, leading to data theft, takeover, and full administrative control.

## How to Find or Test

Authorized testing only, with accounts at different tiers (follow the `smx-ai-vapt` authorization gate).

- Horizontal: swap object ids/account references between same-tier users (overlaps `idor-bola`).
- Vertical: tamper with role/group claims in requests, JWTs, or hidden fields; replay admin requests with a normal token; abuse self-service role/permission changes.
- Test invite/role-assignment flows for setting privileges above the inviter's, and confirm demoted users lose access at once.
- Source review by stack (`source-code-vapt`): Postgres `SECURITY DEFINER` functions without a pinned `SET search_path`; a PostgREST `authenticator` role that is not `NOINHERIT` or can switch to more roles than intended.

## How to Fix

- Derive privileges server-side from the trusted session; reject client-supplied role/tenant claims, and accept token claims only after signature and issuer validation.
- Enforce that a principal can never grant itself or others more than it holds; re-check on every sensitive action and apply role changes immediately. Add tests. Map to ASVS 5.0 V8 Authorization (8.2.1, 8.3.2) and V9 Self-contained Tokens (9.1.1).

## References

- [OWASP WSTG-ATHZ-03: Privilege Escalation](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/05-Authorization/03-Privilege_Escalation/)
- [CWE-269: Improper Privilege Management](https://cwe.mitre.org/data/definitions/269.html)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
