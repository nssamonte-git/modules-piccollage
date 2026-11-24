---
id: hidden-admin-parameter
title: Hidden Administrative Parameter Abuse
category: backend-api-authz
platforms: [backend]
owasp_refs: [API5:2023, API3:2023]
cwe: [CWE-472, CWE-285, CWE-915]
severity: high
last_verified: 2026-10-29
---

# Hidden Administrative Parameter Abuse

## Why It Is a Threat

Undocumented or hidden parameters (`?admin=true`, `debug`, `role`, `internal`, `impersonate`) toggle privileged behavior when supplied, letting an attacker who guesses or discovers them escalate privilege or reach internal functionality. The server wrongly treats a client-controlled value as trusted. Related: `backend-mass-assignment`, `broken-function-level-authorization`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Fuzz for common admin, debug, and role parameters in bodies, queries, cookies, and headers; review code for parameters that change authorization or behavior.

## How to Fix

- Never derive privilege from request parameters; authorize server-side from the authenticated session and role.
- Remove debug and admin toggles from production and bind input with an explicit allowlist. Map to API5:2023 and ASVS 5.0 V8.3 Operation Level Authorization (8.3.1), V15.3 Defensive Coding (15.3.3), and V13.4 Unintended Information Leakage (13.4.2).

## References

- [OWASP API5:2023 Broken Function Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa5-broken-function-level-authorization/)
- [OWASP API3:2023 Broken Object Property Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa3-broken-object-property-level-authorization/)
- [CWE-472: External Control of Assumed-Immutable Web Parameter](https://cwe.mitre.org/data/definitions/472.html)
- [OWASP Mass Assignment Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Mass_Assignment_Cheat_Sheet.html)
