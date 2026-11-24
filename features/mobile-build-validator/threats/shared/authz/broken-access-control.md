---
id: broken-access-control
title: Broken Access Control
category: authz
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, WSTG-ATHZ]
cwe: [CWE-284, CWE-862, CWE-863]
severity: high
last_verified: 2026-10-29
aliases: [excessive-permissions]
---

# Broken Access Control

## Why It Is a Threat

Access-control rules are missing, incomplete, or enforced in the wrong place, letting users act outside their intended permissions — viewing, modifying, or deleting data and functions that should be denied. It is the top-ranked risk in the OWASP Top 10:2025 and the umbrella for IDOR/BOLA, function-level, and property-level failures.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map every resource and action to the roles allowed; test each as a lower-privileged and unauthenticated principal.
- Force-browse to restricted URLs/endpoints, replay privileged requests with a low-priv token, and tamper with ids, verbs, and headers.
- Check that denials are server-side and consistent across UI, API, mobile, and background paths.

## How to Fix

- Enforce deny-by-default authorization at a single trusted server-side chokepoint; never rely on the client hiding actions.
- Drive decisions from the authenticated session, not request-supplied identity/role; log access-control failures and alert on repeats.
- Cover every path (read/list/update/delete/export/jobs) at function, object, and field level, and add regression tests. Map to ASVS 5.0 V8 Authorization (8.2.1–8.2.3, 8.3.1).

## References

- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
- [OWASP WSTG-ATHZ-02: Bypassing Authorization Schema](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/05-Authorization/02-Bypassing_Authorization_Schema/)
- [CWE-284: Improper Access Control](https://cwe.mitre.org/data/definitions/284.html)
