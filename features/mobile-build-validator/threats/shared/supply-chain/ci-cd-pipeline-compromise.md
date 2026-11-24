---
id: ci-cd-pipeline-compromise
title: CI/CD Pipeline / Build-Runner Compromise
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025, CICD-SEC-5, CICD-SEC-6, CICD-SEC-4, A08:2025]
cwe: [CWE-284, CWE-829, CWE-494]
severity: critical
last_verified: 2026-10-29
aliases: [build-runner-compromise]
---

# CI/CD Pipeline / Build-Runner Compromise

## Why It Is a Threat

CI/CD systems hold powerful credentials and produce release artifacts. A compromised pipeline or build runner (poisoned workflow, untrusted pull-request code running on privileged or persistent self-hosted runners, leaked CI secrets, compromised third-party actions) lets attackers inject code, steal secrets, and ship backdoored releases.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review workflow token permissions, secret scoping, runner isolation, and triggers that run fork or pull-request code with secrets or write tokens (e.g. GitHub `pull_request_target` checking out PR code).
- Check that third-party actions and build images are pinned and reviewed.

## How to Fix

- Use least-privilege, job-scoped, short-lived credentials (OIDC federation instead of stored cloud keys); set default token permissions to read-only; never expose secrets or write tokens to untrusted PR code.
- Use ephemeral, isolated runners; pin third-party actions to a full commit SHA; require approvals for deployment environments; log and alert on pipeline changes. Map to A03:2025, CICD-SEC-4/5/6 (see `poisoned-pipeline-execution`, `insufficient-pipeline-access-control`), NIST SP 800-204D, and SLSA Build track.

## References

- [OWASP CICD-SEC-5: Insufficient Pipeline-Based Access Controls](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-05-Insufficient-PBAC)
- [GitHub Docs: Secure use reference for GitHub Actions](https://docs.github.com/en/actions/reference/security/secure-use)
- [OWASP CI/CD Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/CI_CD_Security_Cheat_Sheet.html)
- [NIST SP 800-204D: Software Supply Chain Security in DevSecOps CI/CD Pipelines](https://csrc.nist.gov/pubs/sp/800/204/d/final)
- [SLSA v1.2: Build requirements](https://slsa.dev/spec/v1.2/build-requirements)
