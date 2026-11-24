---
id: compromised-registry
title: Compromised Package Registry
category: supply-chain
platforms: [shared, web, backend]
owasp_refs: [A03:2025, A08:2025, CICD-SEC-3]
cwe: [CWE-494, CWE-829]
severity: high
last_verified: 2026-10-29
aliases: [compromised-package-registry, registry-mirror-compromise]
---

# Compromised Package Registry

## Why It Is a Threat

A package or container registry, or an internal mirror or proxy, is compromised or impersonated and serves tampered packages to every consumer: a single point that can backdoor many downstream builds.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review which registries and mirrors builds trust, whether connections use TLS and authentication, whether lockfile hashes and signatures are enforced on install, and who can publish to or administer internal mirrors.

## How to Fix

- Enforce lockfile integrity hashes and, where the ecosystem supports it, signature or provenance verification on install; use authenticated, access-controlled, monitored mirrors.
- Pin versions, and alert on unexpected content changes for an already published version. Map to A03:2025, A08:2025, and SLSA.

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [OWASP Software Supply Chain Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Software_Supply_Chain_Security_Cheat_Sheet.html)
- [CWE-494: Download of Code Without Integrity Check](https://cwe.mitre.org/data/definitions/494.html)
- [SLSA v1.2: Threats and mitigations](https://slsa.dev/spec/v1.2/threats)
