---
id: typosquatting
title: Typosquatting Packages
category: supply-chain
platforms: [shared, web, backend]
owasp_refs: [CICD-SEC-3, A03:2025, A08:2025]
cwe: [CWE-506]
severity: medium
last_verified: 2026-10-29
---

# Typosquatting Packages

## Why It Is a Threat

Attackers publish malicious packages with names resembling popular ones (misspellings, alternate separators, brand look-alikes, or names that AI assistants hallucinate), so a typo or copy-paste installs malware (`malicious-packages`) with build or developer privileges.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Audit dependency names for look-alikes of intended packages; verify exact names, publishers, download history, and repository links.

## How to Fix

- Verify package names and publishers before adding; use lockfiles with integrity hashes and an allowlist or curated internal mirror.
- Review new dependencies in pull requests with dependency-review tooling. Map to CICD-SEC-3 and A03:2025.

## References

- [OWASP CICD-SEC-3: Dependency Chain Abuse](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-03-Dependency-Chain-Abuse)
- [CAPEC-630: TypoSquatting](https://capec.mitre.org/data/definitions/630.html)
- [CWE-506: Embedded Malicious Code](https://cwe.mitre.org/data/definitions/506.html)
