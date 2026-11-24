---
id: missing-supply-chain-controls
title: Missing Pinning, Signatures, or SBOM
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025, A08:2025, CICD-SEC-9]
cwe: [CWE-1357, CWE-494, CWE-353]
severity: medium
last_verified: 2026-10-29
aliases: [missing-dependency-pinning, missing-artifact-signatures, missing-sbom]
---

# Missing Pinning, Signatures, or SBOM

## Why It Is a Threat

Without foundational controls (pinned dependencies, signed and verified artifacts, build provenance, and a software bill of materials) you cannot show what runs in production or respond quickly when a dependency is found vulnerable or malicious.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check for lockfiles with integrity hashes, artifact signing and verification, provenance generation, and SBOM generation, storage, and use in the pipeline.

## How to Fix

- Pin dependencies with integrity hashes; sign and verify artifacts; generate an SBOM (CycloneDX or SPDX) for every release and use it for vulnerability and incident impact analysis.
- Generate SLSA build provenance and verify it at deploy. Map to A03:2025, A08:2025, ASVS 5.0 V15.1 (SBOM and component inventory), and NIST SSDF.

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [CISA: Software Bill of Materials (SBOM)](https://www.cisa.gov/sbom)
- [OWASP Dependency Graph SBOM Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Dependency_Graph_SBOM_Cheat_Sheet.html)
- [SLSA v1.2 specification](https://slsa.dev/spec/v1.2/)
