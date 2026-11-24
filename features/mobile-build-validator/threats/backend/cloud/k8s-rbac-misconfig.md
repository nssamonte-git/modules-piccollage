---
id: k8s-rbac-misconfig
title: Kubernetes RBAC Misconfiguration
category: backend-cloud
platforms: [backend]
owasp_refs: [A01:2025, A02:2025]
cwe: [CWE-269, CWE-732]
severity: high
last_verified: 2026-10-29
---

# Kubernetes RBAC Misconfiguration

## Why It Is a Threat

Over-broad RBAC (cluster-admin or `system:masters` membership, wildcard verbs or resources, service accounts with Secret, exec, or workload-creation rights) lets a compromised pod or user escalate to cluster control, read all Secrets, or run workloads. Some permissions escalate indirectly: `escalate`, `bind`, and `impersonate` verbs, creating pods (which can mount any Secret in the namespace), and `nodes/proxy` access to the kubelet.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review Roles, ClusterRoles, and bindings for wildcards and cluster-admin; check service-account token automounting and the escalation-prone rights above (`pods/exec`, `secrets`, `create pods`, `escalate`, `bind`, `impersonate`, `nodes/proxy`).

## How to Fix

- Apply least-privilege, namespace-scoped RBAC (no wildcards or cluster-admin for workloads); set `automountServiceAccountToken: false` where tokens are not needed; avoid `system:masters`.
- Use admission policies and review RBAC regularly. Map to A01:2025 and `over-permissive-iam`.

## References

- [Kubernetes: Role Based Access Control Good Practices](https://kubernetes.io/docs/concepts/security/rbac-good-practices/)
- [OWASP Kubernetes Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Kubernetes_Security_Cheat_Sheet.html)
- [OWASP Kubernetes Top Ten](https://owasp.org/www-project-kubernetes-top-ten/)
- [CWE-269: Improper Privilege Management](https://cwe.mitre.org/data/definitions/269.html)
