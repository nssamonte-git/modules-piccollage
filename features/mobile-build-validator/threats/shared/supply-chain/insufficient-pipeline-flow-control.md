---
id: insufficient-pipeline-flow-control
title: Insufficient CI/CD Flow Control Mechanisms
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [CICD-SEC-1, A08:2025, A03:2025]
cwe: [CWE-654, CWE-284]
severity: high
last_verified: 2026-10-29
aliases: [insufficient-flow-control, missing-deployment-approval, unreviewed-code-to-production, auto-merge-abuse]
---

# Insufficient CI/CD Flow Control Mechanisms

## Why It Is a Threat

Insufficient flow control means a single identity, human or machine, can push code or artifacts through the CI/CD pipeline to production without an independent review or approval. Anyone who gains one foothold (a developer account, a CI token, artifact-repository write access) can then ship malicious changes by pushing to an auto-deploying branch, manually triggering a production deploy, abusing auto-merge rules or branch-protection exemptions, uploading a look-alike artifact, or changing production directly. This differs from `source-control-compromise`, which is about gaining repository write access; this card is about the missing gates that let one such account reach production alone.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map the path from commit to production for each service and mark every step that lacks a second person or verified system check.
- Review branch protection or rulesets on production branches: required reviews and status checks, code-owner review, bypass lists, and admin enforcement.
- Check auto-merge rules and bots, who can trigger or re-run deploy pipelines, and whether accounts that can push unreviewed code can also deploy.
- Confirm that deploy pipelines accept only artifacts built and attested by the approved CI identity, and that drift between production and its CI source is detected.

## How to Fix

- Protect production branches with required reviews, status checks, and no or tightly audited bypass exceptions.
- Gate production deployments with deployment environments, required reviewers, and branch restrictions; separate the right to merge from the right to deploy.
- Limit auto-merge to narrow, reviewed rules that do not import third-party code.
- Promote only artifacts built by the approved CI service account and verify their provenance before deploy (see `artifact-tampering`).
- Detect and reconcile changes made directly in production outside the pipeline.

## References

- [OWASP CICD-SEC-1: Insufficient Flow Control Mechanisms](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-01-Insufficient-Flow-Control-Mechanisms)
- [GitHub Docs: About protected branches](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)
- [GitHub Docs: Deployments and environments](https://docs.github.com/en/actions/reference/workflows-and-actions/deployments-and-environments)
- [CWE-654: Reliance on a Single Factor in a Security Decision](https://cwe.mitre.org/data/definitions/654.html)
