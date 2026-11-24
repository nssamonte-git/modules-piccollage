---
id: feature-flag-manipulation
title: Feature-Flag Manipulation
category: business-logic
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, A06:2025, A02:2025]
cwe: [CWE-602]
severity: medium
last_verified: 2026-10-29
---

# Feature-Flag Manipulation

## Why It Is a Threat

Feature flags or entitlements enforced client-side (in JS, mobile config, or request fields) can be toggled by an attacker to unlock paid, beta, or admin features or bypass gating, because the server trusts the client's flag state.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Flip client-held flags or entitlements (local storage, request body, mobile remote config) and confirm whether the server independently authorizes the gated capability.
- Inspect client flag payloads for unreleased features, internal endpoints, or secrets.

## How to Fix

- Evaluate flags and entitlements server-side and authorize the action regardless of client state; never ship secrets or unreleased-feature details in client flag payloads.
- Treat flags as UX, not a security boundary. Map to ASVS 5.0 V8 Authorization (8.2.1, 8.3.1); see `missing-server-side-authorization`.

## References

- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
