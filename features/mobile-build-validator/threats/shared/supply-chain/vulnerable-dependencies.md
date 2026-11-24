---
id: vulnerable-dependencies
title: Vulnerable or Abandoned Dependencies
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025]
cwe: [CWE-1395, CWE-1104]
severity: high
last_verified: 2026-11-17
aliases: [abandoned-dependencies, outdated-components]
---

# Vulnerable or Abandoned Dependencies

## Why It Is a Threat

Using open-source components with known CVEs, or unmaintained packages that will never be patched, lets attackers exploit public vulnerabilities directly in your app, including through transitive dependencies you never chose. The most common supply-chain weakness.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Run dependency/SCA scanning against lockfiles or an SBOM; flag known-vulnerable (prioritizing known-exploited) and abandoned packages and transitive risk.
- Look up each identified component version live on CVE.org and OSV/GHSA; rank matches by CISA KEV listing, then EPSS, then CVSS, and treat version-only matches as candidates until reachability is confirmed (`smx-ai-vapt` known-vulnerability lookup).

## How to Fix

- Patch or upgrade within documented, risk-based time frames; replace abandoned packages; remove unused dependencies; pin and review transitive dependencies.
- Automate SCA in CI with a triage policy and subscribe to advisories (CVE.org, OSV/GHSA, CISA KEV; NVD scores only the CVEs it prioritizes). Map to A03:2025, ASVS 5.0 V15.1/V15.2 (remediation time frames and SBOM), and the `security-review` dependency-vulnerability-triage reference.

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [OWASP Vulnerable Dependency Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Vulnerable_Dependency_Management_Cheat_Sheet.html)
- [CWE-1395: Dependency on Vulnerable Third-Party Component](https://cwe.mitre.org/data/definitions/1395.html)
- [CISA Known Exploited Vulnerabilities Catalog](https://www.cisa.gov/known-exploited-vulnerabilities-catalog)
- [CVE Program: CVE records](https://www.cve.org/)
