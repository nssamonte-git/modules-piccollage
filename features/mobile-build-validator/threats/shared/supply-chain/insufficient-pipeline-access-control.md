---
id: insufficient-pipeline-access-control
title: Insufficient Pipeline-Based Access Controls (PBAC)
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [CICD-SEC-5, A08:2025, A03:2025]
cwe: [CWE-250, CWE-269]
severity: high
last_verified: 2026-10-29
aliases: [insufficient-pbac, pbac, over-privileged-pipeline, over-privileged-ci-token]
---

# Insufficient Pipeline-Based Access Controls (PBAC)

## Why It Is a Threat

Insufficient pipeline-based access control means CI/CD jobs run with more access than they need: broad secrets, write-capable tokens, cloud roles, shared build hosts, and unrestricted network egress. Once any code runs inside the job, through `poisoned-pipeline-execution`, a malicious dependency install script, or a compromised action, it inherits all of that access and can steal secrets, move laterally to other pipelines or cloud accounts, and push artifacts toward production. This card is about limiting the blast radius per pipeline and step; the general runner-compromise scenario is in `ci-cd-pipeline-compromise`, and cloud IAM outside CI is in `over-permissive-iam`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- For each pipeline, list the secrets, tokens, cloud roles, and network destinations available to each job and step, and compare them with what the step actually uses.
- In GitHub Actions, check the default `GITHUB_TOKEN` permissions at organization and repository level and whether workflows declare narrow `permissions:` blocks.
- Look for long-lived cloud keys stored as CI secrets where OIDC federation with short-lived, claim-scoped roles would work.
- Check whether build nodes are shared between pipelines of different sensitivity, reused between runs, run jobs as root, or reach internal networks and the instance metadata service.
- Confirm that package install scripts run in a stage without access to deployment secrets.

## How to Fix

- Scope secrets and tokens per pipeline and step; set the default `GITHUB_TOKEN` to read-only and grant write scopes only in the jobs that need them.
- Replace stored cloud keys with OIDC-issued short-lived credentials whose trust policy is bound to repository, branch, and environment claims.
- Use ephemeral, isolated runners that reset after each run, do not share nodes across sensitivity levels, and run jobs with least OS privilege.
- Restrict runner egress and internal network reach to required destinations; block the metadata service where it is not needed (see `metadata-service-exposure`).
- Run dependency installation in a separate context without secrets.

## References

- [OWASP CICD-SEC-5: Insufficient PBAC (Pipeline-Based Access Controls)](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-05-Insufficient-PBAC)
- [GitHub Docs: Use GITHUB_TOKEN for authentication in workflows](https://docs.github.com/en/actions/tutorials/authenticate-with-github_token)
- [GitHub Docs: OpenID Connect](https://docs.github.com/en/actions/concepts/security/openid-connect)
- [CWE-250: Execution with Unnecessary Privileges](https://cwe.mitre.org/data/definitions/250.html)
