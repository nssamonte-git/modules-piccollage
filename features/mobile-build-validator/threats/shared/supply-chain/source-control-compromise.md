---
id: source-control-compromise
title: Source-Control Compromise
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [CICD-SEC-2, CICD-SEC-1, A03:2025, A08:2025]
cwe: [CWE-284, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [source-control-account-compromise, unauthorized-code-changes, malicious-pull-requests, branch-protection-bypass]
---

# Source-Control Compromise

## Why It Is a Threat

An attacker gains write access to the repository (stolen account or token, weak branch protection, bypass exemptions, a malicious PR merged, force-push) and injects backdoors or unauthorized changes that flow into builds and releases.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review repo and organization access, MFA enforcement, personal access tokens and app grants, branch protection or rulesets, required reviews and status checks, bypass lists, and signed-commit enforcement; assess merge, auto-merge, and force-push controls.

## How to Fix

- Enforce MFA, least-privilege repo access, short-lived fine-grained tokens, and protected branches or rulesets with required reviews (no self-approval) and status checks; minimize bypass exemptions.
- Require signed commits where practical; audit and alert on permission, rule, and history changes. Map to CICD-SEC-2, CICD-SEC-1 (see `insufficient-pipeline-flow-control`), A03:2025, and SLSA Source track.

## References

- [OWASP CICD-SEC-1: Insufficient Flow Control Mechanisms](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-01-Insufficient-Flow-Control-Mechanisms)
- [OWASP CICD-SEC-2: Inadequate Identity and Access Management](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-02-Inadequate-Identity-And-Access-Management)
- [SLSA v1.2: Source requirements](https://slsa.dev/spec/v1.2/source-requirements)
- [GitHub Docs: About protected branches](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)
