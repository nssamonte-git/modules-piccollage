---
id: over-permissive-iam
title: Over-Permissive Cloud IAM
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-732, CWE-269, CWE-250]
severity: high
last_verified: 2026-10-29
aliases: [wildcard-iam, excessive-cloud-permissions]
---

# Over-Permissive Cloud IAM

## Why It Is a Threat

Cloud identities and roles granted wildcard or excessive permissions (`*:*`, broad admin, unscoped service accounts, cluster-admin bindings) let a single compromise escalate to full account control and lateral movement. Related: `service-account-compromise`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review IAM and RBAC policies for wildcards, unused permissions, broad or external trust relationships, long-lived access keys, and privilege-escalation paths (e.g. `iam:PassRole`, policy-attach, or role-binding rights).

## How to Fix

- Apply least privilege with scoped, reviewed policies; prefer short-lived credentials and workload identity federation over static keys; remove unused grants.
- Use access-analysis tooling to find external and unused access, set guardrails (permission boundaries or organization policies), and alert on privilege changes. Map to ASVS 5.0 V8 Authorization and V13.2 (least-privilege service accounts), and the `security-review` cloud-iam-review reference.

## References

- [AWS: Security best practices in IAM](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html)
- [AWS: IAM Access Analyzer](https://docs.aws.amazon.com/IAM/latest/UserGuide/what-is-access-analyzer.html)
- [Kubernetes: Role Based Access Control Good Practices](https://kubernetes.io/docs/concepts/security/rbac-good-practices/)
- [CWE-732: Incorrect Permission Assignment for Critical Resource](https://cwe.mitre.org/data/definitions/732.html)
