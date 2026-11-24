---
id: improper-secrets-management
title: Improper Secrets Management
category: crypto
platforms: [shared, web, backend]
owasp_refs: [A04:2025, A02:2025]
cwe: [CWE-522, CWE-798]
severity: high
last_verified: 2026-10-29
---

# Improper Secrets Management

## Why It Is a Threat

Secrets (keys, tokens, DB passwords, signing material) are handled without a managed lifecycle — committed to repos, baked into images/env files, shared across environments, never rotated — so a single exposure is durable and far-reaching. Outcomes: `secrets-leakage`; source-control case: `secrets-in-source-control`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review how secrets are stored, injected, scoped, and rotated; check repos, images, CI variables, env files, and orchestrator objects (Kubernetes Secrets are only base64-encoded unless encryption at rest is enabled); confirm prod/test separation.

## How to Fix

- Use a secrets manager/KMS with least-privilege access, per-environment secrets, documented rotation and expiry, and audit; inject at runtime, never bake in. Prefer short-lived, identity-based credentials (workload identity/OIDC federation) over static secrets.
- Scan for committed secrets in CI. Map to ASVS 5.0 V13 Configuration (13.1.4, 13.3.1–13.3.4) and the `security-review` secrets-handling reference.

## References

- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-798: Use of Hard-coded Credentials](https://cwe.mitre.org/data/definitions/798.html)
- [Kubernetes: Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
