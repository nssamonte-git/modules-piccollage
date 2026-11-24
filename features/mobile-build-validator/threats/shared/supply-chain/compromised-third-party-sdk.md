---
id: compromised-third-party-sdk
title: Compromised Third-Party SDK
category: supply-chain
platforms: [shared, web, android, ios]
owasp_refs: [A03:2025, A08:2025]
cwe: [CWE-506, CWE-1357, CWE-829]
severity: high
last_verified: 2026-10-29
---

# Compromised Third-Party SDK

## Why It Is a Threat

A legitimate SDK or hosted script (analytics, ads, auth, payments) is compromised or turns malicious in an update, exfiltrating data or injecting behavior across every app or site that embeds it. High blast radius, especially in mobile clients and pages that load third-party JavaScript from a CDN.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory embedded SDKs and third-party scripts with their versions, permissions, and data access; review update diffs and provenance; capture and compare SDK network traffic between versions.

## How to Fix

- Minimize SDKs; pin versions and review updates before release; use Subresource Integrity or self-host third-party scripts; monitor SDK network behavior and permissions.
- Prefer maintained SDKs from verified publishers, isolate their access, and declare their data collection (e.g. Apple privacy manifests). Map to A03:2025 and A08:2025; see `compromised-developer-devices` for build-side risk.

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [OWASP Third Party JavaScript Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Third_Party_Javascript_Management_Cheat_Sheet.html)
- [CWE-1357: Reliance on Insufficiently Trustworthy Component](https://cwe.mitre.org/data/definitions/1357.html)
