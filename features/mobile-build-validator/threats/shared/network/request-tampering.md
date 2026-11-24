---
id: request-tampering
title: Request Tampering
category: network
platforms: [shared, web, backend, android, ios]
owasp_refs: [A08:2025, A06:2025, WSTG-BUSL-02]
cwe: [CWE-472, CWE-345, CWE-565]
severity: high
last_verified: 2026-10-29
---

# Request Tampering

## Why It Is a Threat

An attacker (on the network or via a client proxy) modifies request parameters, headers, or bodies that the server trusts — altering prices, ids, roles, or hidden fields. Closely tied to `broken-access-control`, `backend-mass-assignment`, and weak transport integrity.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Intercept and modify parameters/headers/hidden fields/cookies (price, quantity, role, id, totals) and confirm whether the server re-validates server-side.

## How to Fix

- Never trust client-supplied values for security or business decisions; recompute and authorize server-side at a trusted layer.
- Enforce TLS integrity and validate/allowlist inputs; sign (MAC) any client-held state and verify it on return; use per-message signatures for high-value requests. Map to ASVS 5.0 V2 Validation and Business Logic (2.2.1–2.2.3), V4 (4.1.5), and V8 Authorization.

## References

- [CWE-472: External Control of Assumed-Immutable Web Parameter](https://cwe.mitre.org/data/definitions/472.html)
- [OWASP WSTG-BUSL-02: Ability to Forge Requests](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/02-Ability_to_Forge_Requests/)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
