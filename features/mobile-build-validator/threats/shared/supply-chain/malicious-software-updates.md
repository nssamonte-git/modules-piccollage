---
id: malicious-software-updates
title: Malicious Software Updates
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A08:2025, A03:2025]
cwe: [CWE-494, CWE-347]
severity: critical
last_verified: 2026-10-29
aliases: [compromised-update-servers]
---

# Malicious Software Updates

## Why It Is a Threat

A compromised vendor build, update server, or unverified update channel pushes malicious updates to clients and servers that apply them automatically (the SolarWinds-class attack). High blast radius across every installation. Depends on weak verification (`signature-verification-failure`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the update mechanism: is each update signed, verified against a pinned or trusted key before install, protected against rollback to older versions, and delivered over authenticated TLS? Could a server compromise alone push arbitrary code?

## How to Fix

- Sign updates and verify them against a pinned or trusted key before applying; reject downgrades; deliver over TLS; use an update framework with key separation and freshness checks.
- Protect the update server and signing keys (`signing-key-theft`) and use staged or canary rollouts to limit blast radius. Map to A08:2025 and A03:2025.

## References

- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [CWE-494: Download of Code Without Integrity Check](https://cwe.mitre.org/data/definitions/494.html)
- [CAPEC-669: Alteration of a Software Update](https://capec.mitre.org/data/definitions/669.html)
- [MITRE ATT&CK T1195.002: Compromise Software Supply Chain](https://attack.mitre.org/techniques/T1195/002/)
