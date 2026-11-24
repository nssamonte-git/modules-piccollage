---
id: geo-restriction-bypass
title: Geographical-Restriction Bypass
category: business-logic
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, A06:2025, WSTG-BUSL-02]
cwe: [CWE-807, CWE-602]
severity: medium
last_verified: 2026-10-29
---

# Geographical-Restriction Bypass

## Why It Is a Threat

Geo or region gating (licensing, pricing, legal compliance) is bypassed via VPNs, spoofed device location, forged client IP headers, or client-controlled region fields, exposing the business to licensing breaches, regulatory issues, and price arbitrage.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Test region gating with altered locale or region parameters, forged `X-Forwarded-For`-style headers, mock device location, and proxy or VPN egress; confirm whether the server enforces the rule and which signals it trusts.

## How to Fix

- Enforce geo decisions server-side on trusted signals (verified billing region, server-side IP geolocation taken only from trusted proxy headers) rather than client-supplied location.
- Accept residual VPN risk explicitly and layer compliance controls. Map to ASVS 5.0 V8 Authorization (8.1.3, 8.2.4) and ASVS 5.0 V15 Secure Coding and Architecture (15.3.4).

## References

- [CWE-807: Reliance on Untrusted Inputs in a Security Decision](https://cwe.mitre.org/data/definitions/807.html)
- [OWASP WSTG-BUSL-02 Ability to Forge Requests](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/02-Ability_to_Forge_Requests/)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
