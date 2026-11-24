---
id: malicious-packages
title: Malicious Packages
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025, CICD-SEC-3, A08:2025]
cwe: [CWE-506]
severity: high
last_verified: 2026-10-29
---

# Malicious Packages

## Why It Is a Threat

An installed package intentionally contains malware (credential and token stealers, backdoors, install-time scripts, or self-propagating worms that republish packages with stolen publish tokens) and runs with developer or build privileges. Delivered directly, through hijacked maintainer accounts, via `typosquatting`, or via `dependency-confusion`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review new and updated dependencies, their install scripts, and maintainer or ownership changes; check them against malicious-package advisories; analyze suspicious packages only in an isolated sandbox.

## How to Fix

- Vet dependencies before adding; pin versions with lockfiles and integrity hashes; disable or sandbox install scripts; delay adoption of brand-new releases.
- Install through vetted internal mirrors; protect publish tokens with MFA and short lifetimes; rotate exposed secrets after an incident. Map to A03:2025, CICD-SEC-3, and SLSA practices.

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [OWASP CICD-SEC-3: Dependency Chain Abuse](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-03-Dependency-Chain-Abuse)
- [CWE-506: Embedded Malicious Code](https://cwe.mitre.org/data/definitions/506.html)
- [CISA: Widespread Supply Chain Compromise Impacting npm Ecosystem](https://www.cisa.gov/news-events/alerts/2025/09/23/widespread-supply-chain-compromise-impacting-npm-ecosystem)
