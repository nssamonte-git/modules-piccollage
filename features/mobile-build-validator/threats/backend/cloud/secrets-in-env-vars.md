---
id: secrets-in-env-vars
title: Secrets in Environment Variables / Manifests
category: backend-cloud
platforms: [backend]
owasp_refs: [A02:2025]
cwe: [CWE-526, CWE-540]
severity: medium
last_verified: 2026-10-29
aliases: [secrets-in-deployment-manifests]
---

# Secrets in Environment Variables / Manifests

## Why It Is a Threat

Secrets passed as environment variables or baked into deployment manifests, IaC, or image layers leak through process inspection, crash dumps, child processes, debug endpoints, logs, and committed config. It is a broad, easily overlooked exposure path. Related: `secrets-leakage`, `secrets-in-source-control`, `k8s-secret-exposure`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check whether secrets are injected as plain env vars or embedded in manifests, IaC, or images; review logs, crash dumps, diagnostic endpoints, and child-process inheritance.

## How to Fix

- Prefer a secrets manager with runtime retrieval (mounted file, CSI driver, or SDK) over env vars; never commit secrets to manifests or IaC; restrict who can read them and rotate them.
- Redact secrets from logs and diagnostics. Map to A02:2025, ASVS 5.0 V13.3 Secret Management (13.3.1), and `improper-secrets-management`.

## References

- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-526: Cleartext Storage of Sensitive Information in an Environment Variable](https://cwe.mitre.org/data/definitions/526.html)
- [Kubernetes: Good practices for Kubernetes Secrets](https://kubernetes.io/docs/concepts/security/secrets-good-practices/)
