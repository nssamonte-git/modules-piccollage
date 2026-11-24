---
id: poisoned-pipeline-execution
title: Poisoned Pipeline Execution (PPE)
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [CICD-SEC-4, A08:2025, A03:2025]
cwe: [CWE-829, CWE-94]
severity: critical
last_verified: 2026-10-29
aliases: [ppe, pwn-request, pull-request-target-abuse, direct-ppe, indirect-ppe, public-ppe]
---

# Poisoned Pipeline Execution (PPE)

## Why It Is a Threat

Poisoned pipeline execution is when an attacker who can change repository content, but not the build system, gets the CI pipeline to run their commands by editing the pipeline configuration or files it executes, such as a Makefile, test, build script, or linter config. It comes in three forms: direct PPE (editing the CI config in a branch or pull request), indirect PPE (editing scripts and files the protected config runs), and public PPE (an outsider's fork pull request triggering a privileged pipeline). The classic GitHub Actions case is a "pwn request": a `pull_request_target`, `workflow_run`, or `issue_comment` workflow checks out and runs fork code while holding repository secrets or a write-capable `GITHUB_TOKEN`. This card covers that entry vector; broader runner and pipeline compromise is in `ci-cd-pipeline-compromise`, and what the poisoned job can then reach is in `insufficient-pipeline-access-control`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- List pipelines triggered by pull requests, comments, or pushes to unprotected branches, and note which ones receive secrets, cloud credentials, or write tokens.
- In GitHub Actions, flag `pull_request_target`, `workflow_run`, and `issue_comment` workflows that check out the pull request head or merge ref, set `repository:` to the fork, or download fork artifacts, and then run build, test, install, or tool steps on that content.
- Check for expressions that place untrusted event fields (titles, branch names, comments) directly into `run:` scripts.
- Confirm fork-approval settings, whether self-hosted runners serve public repositories, and whether GitHub's default `pull_request_target` event policy is left in place.
- Validate only with a harmless, owner-approved marker in a test repository; never exfiltrate real secrets.

## How to Fix

- Prefer `pull_request` for untrusted code; if `pull_request_target` is required, never execute checked-out pull request content, and treat `workflow_run` artifacts as untrusted data.
- GitHub's default `pull_request_target` policy currently runs in evaluate mode and is enforced from December 2, 2026; use its policy insights to find workflows that would be blocked, and fix them rather than opting out.
- Set least-privilege `permissions:` for `GITHUB_TOKEN` and expose only the secrets each job needs.
- Keep CI configuration and the scripts it runs under branch protection and code-owner review; run untrusted builds on isolated, ephemeral runners without secrets.
- Pass untrusted event data through environment variables rather than inline expressions, enable CodeQL for Actions, and pin third-party actions to a full commit SHA.

## References

- [OWASP CICD-SEC-4: Poisoned Pipeline Execution (PPE)](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-04-Poisoned-Pipeline-Execution)
- [GitHub Docs: Securely using pull_request_target](https://docs.github.com/en/actions/reference/security/securely-using-pull_request_target)
- [GitHub Docs: Secure use reference (GitHub Actions security hardening)](https://docs.github.com/en/actions/reference/security/secure-use)
- [CWE-829: Inclusion of Functionality from Untrusted Control Sphere](https://cwe.mitre.org/data/definitions/829.html)
