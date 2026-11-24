---
id: compromised-build-tools
title: Compromised Build Tools / IDE Plugins
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025, CICD-SEC-9, CICD-SEC-3, A08:2025]
cwe: [CWE-506, CWE-1357, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [build-tool-compromise, ide-plugin-compromise]
---

# Compromised Build Tools / IDE Plugins

## Why It Is a Threat

Compilers, build plugins, CI helper scripts, linters, or IDE extensions are compromised and inject malicious code or steal secrets at build or development time, bypassing source review because the source looks clean (a "trusting trust" style attack; the Codecov uploader and SolarWinds build compromises are real examples).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory build tools, build plugins, downloaded CI scripts, and IDE extensions with their sources, versions, and checksums; review what extensions can access (filesystem, secrets, network).

## How to Fix

- Pin build tools and plugins by version and checksum; verify the published hash or signature of any script fetched during a build; allowlist and limit IDE extensions.
- Build in clean, ephemeral, isolated environments and compare reproducible-build outputs where feasible. Map to A03:2025, CICD-SEC-9, A08:2025, and SLSA Build track.

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [OWASP CICD-SEC-9: Improper Artifact Integrity Validation](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-09-Improper-Artifact-Integrity-Validation)
- [CAPEC-670: Software Development Tools Maliciously Altered](https://capec.mitre.org/data/definitions/670.html)
- [MITRE ATT&CK T1195.001: Compromise Software Dependencies and Development Tools](https://attack.mitre.org/techniques/T1195/001/)
