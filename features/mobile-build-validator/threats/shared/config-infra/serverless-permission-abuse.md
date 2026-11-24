---
id: serverless-permission-abuse
title: Serverless Permission Abuse
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-269, CWE-732]
severity: medium
last_verified: 2026-10-29
---

# Serverless Permission Abuse

## Why It Is a Threat

Functions (AWS Lambda, Google Cloud Run functions, Azure Functions) with over-broad execution roles, roles shared across functions, or injectable event data let an attacker who influences one function abuse its permissions to reach other cloud resources. Untrusted event sources and secrets in environment variables compound this.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review each function's execution role for least privilege and sharing; check which event sources can invoke it and whether event payloads are validated; check for plaintext secrets in function configuration.

## How to Fix

- Give each function a minimal, dedicated role and restrict who can invoke or update it; validate event input as untrusted.
- Fetch secrets from a secrets manager at runtime instead of plain environment variables; restrict outbound network access. Map to A02:2025, ASVS 5.0 V13.3 Secret Management, and the cloud-iam-review reference.

## References

- [AWS: Defining Lambda function permissions with an execution role](https://docs.aws.amazon.com/lambda/latest/dg/lambda-intro-execution-role.html)
- [OWASP Serverless FaaS Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Serverless_FaaS_Security_Cheat_Sheet.html)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
