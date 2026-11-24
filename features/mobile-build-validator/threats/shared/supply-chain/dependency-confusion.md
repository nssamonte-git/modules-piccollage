---
id: dependency-confusion
title: Dependency Confusion
category: supply-chain
platforms: [shared, web, backend]
owasp_refs: [CICD-SEC-3, A03:2025, A08:2025]
cwe: [CWE-427, CWE-829]
severity: high
last_verified: 2026-10-29
aliases: [substitution-attack]
---

# Dependency Confusion

## Why It Is a Threat

A package manager resolves a malicious public package that has the same name as a private or internal one (often with a higher version), because the client searches public and private registries together, and then runs attacker code in developer machines and builds, often via install scripts.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify internal package names and check whether they are unclaimed on public registries; review client configuration for mixed sources (e.g. `--extra-index-url`, unscoped npm names, multiple Maven/NuGet feeds) that let a public package win.

## How to Fix

- Use scopes or namespaces for internal packages and map each scope to the private registry only; route all installs through one proxy that does not fall back to public sources for internal names; claim internal names or scopes on public registries.
- Enforce lockfiles with integrity hashes and verify each dependency's source registry. Map to CICD-SEC-3, A03:2025, and ASVS 5.0 V15.2 (components from the expected repository).

## References

- [OWASP CICD-SEC-3: Dependency Chain Abuse](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-03-Dependency-Chain-Abuse)
- [OWASP Software Supply Chain Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Software_Supply_Chain_Security_Cheat_Sheet.html)
- [CWE-427: Uncontrolled Search Path Element](https://cwe.mitre.org/data/definitions/427.html)
