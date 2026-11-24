---
id: missing-server-side-authorization
title: Missing Server-Side Authorization
category: authz
platforms: [shared, web, backend]
owasp_refs: [A01:2025, WSTG-ATHZ-02]
cwe: [CWE-862, CWE-602]
severity: high
last_verified: 2026-10-29
aliases: [authorization-via-ui, client-side-authorization]
---

# Missing Server-Side Authorization

## Why It Is a Threat

Authorization is enforced only in the client (hidden buttons, disabled fields, route guards, mobile app checks) or assumed from UI flow, while the server accepts any well-formed request. Attackers bypass the UI entirely and call the endpoint directly.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Call endpoints directly (proxy/replay) without going through the UI; remove client-side gating and resend.
- Confirm the server independently re-checks permission for every request, not just on the page that renders the action.
- Source review by stack (`source-code-vapt`): Jetpack Compose, SwiftUI, React Native, and Flutter screens or routes hidden by role checks (`if user.isAdmin`, `go_router` `redirect`) that the API does not enforce.

## How to Fix

- Treat the client as untrusted; enforce authorization at a trusted server-side layer for every endpoint and action.
- Use UI gating only for UX, never as the security boundary. Add tests that hit endpoints directly with insufficient privilege. Map to ASVS 5.0 V8 Authorization (8.3.1) and CWE-602.

## References

- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
- [CWE-862: Missing Authorization](https://cwe.mitre.org/data/definitions/862.html)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
