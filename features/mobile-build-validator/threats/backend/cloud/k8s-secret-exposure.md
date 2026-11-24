---
id: k8s-secret-exposure
title: Kubernetes Secret Exposure
category: backend-cloud
platforms: [backend]
owasp_refs: [A02:2025]
cwe: [CWE-522, CWE-312]
severity: high
last_verified: 2026-10-29
---

# Kubernetes Secret Exposure

## Why It Is a Threat

Kubernetes Secrets are only base64-encoded and, by default, stored unencrypted in etcd. They are exposed through etcd or backup access, over-broad RBAC, anyone allowed to create pods in the namespace, environment variables, and committed manifests, so a pod compromise or cluster access harvests credentials. Related: `k8s-rbac-misconfig`, `secrets-in-env-vars`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check etcd encryption at rest, who can read Secrets or create pods via RBAC, whether secrets are in env vars, manifests, or Git, and use of an external secrets store.

## How to Fix

- Enable encryption at rest for Secrets, preferably with a KMS provider; restrict `get`, `list`, and `watch` on Secrets and pod creation via RBAC; use an external secrets manager or CSI driver; prefer volume mounts over env vars.
- Keep secrets out of manifests and Git, and restrict etcd access. Map to A02:2025, ASVS 5.0 V13.3 Secret Management, and `improper-secrets-management`.

## References

- [Kubernetes: Good practices for Kubernetes Secrets](https://kubernetes.io/docs/concepts/security/secrets-good-practices/)
- [Kubernetes: Encrypting Confidential Data at Rest](https://kubernetes.io/docs/tasks/administer-cluster/encrypt-data/)
- [Kubernetes: Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
