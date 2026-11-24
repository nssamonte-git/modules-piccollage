---
id: insecure-iac
title: Insecure Infrastructure-as-Code
category: backend-cloud
platforms: [backend]
owasp_refs: [A02:2025, A08:2025]
cwe: [CWE-1188, CWE-1357]
severity: medium
last_verified: 2026-10-29
---

# Insecure Infrastructure-as-Code

## Why It Is a Threat

IaC (Terraform, CloudFormation, Helm) encodes insecure defaults at scale (public buckets, open security groups, wildcard IAM, unencrypted storage, disabled audit logging, hardcoded secrets), so one bad module is deployed everywhere repeatedly. State files can also hold secrets in plain text. Misconfiguration becomes systemic.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Scan IaC with policy and security tools for public exposure, over-permissive IAM or security groups, missing encryption or logging, and embedded secrets; review module provenance and how state files are stored and accessed.

## How to Fix

- Run IaC security scanning (policy-as-code) in CI and block insecure changes; use vetted, pinned modules; store state encrypted with restricted access; keep secrets out of IaC.
- Enforce secure defaults with reusable hardened modules and detect drift. Map to A02:2025 and ASVS 5.0 V13 Configuration; see `public-data-store`, `open-ports-firewall`, `over-permissive-iam`, `cloud-audit-logging-gaps`.

## References

- [OWASP Infrastructure as Code Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Infrastructure_as_Code_Security_Cheat_Sheet.html)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
- [CWE-1188: Initialization of a Resource with an Insecure Default](https://cwe.mitre.org/data/definitions/1188.html)
