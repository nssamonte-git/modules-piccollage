---
id: serverless-event-injection
title: Serverless Event Injection
category: backend-cloud
platforms: [backend]
owasp_refs: [A05:2025, A06:2025]
cwe: [CWE-20, CWE-94]
severity: high
last_verified: 2026-10-29
---

# Serverless Event Injection

## Why It Is a Threat

Functions receive event data from many sources (HTTP, queues, storage, email, IoT, other cloud services) and often trust it. Unvalidated event fields reach injection sinks (`sql-injection`, `os-command-injection`, `code-injection`) or business logic, and the function's execution role amplifies impact (`serverless-permission-abuse`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify all event sources a function trusts; send malformed or hostile fields and confirm validation happens before they reach sinks or downstream calls.

## How to Fix

- Treat every event field as untrusted; validate against a schema before use; parameterize sink calls; give each function a minimal role.
- Validate event source or signature where possible. Map to ASVS 5.0 V1 Encoding and Sanitization and V2.2 Input Validation; see `serverless-permission-abuse`.

## References

- [OWASP Serverless FaaS Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Serverless_FaaS_Security_Cheat_Sheet.html)
- [OWASP Serverless Top 10](https://owasp.org/www-project-serverless-top-10/)
- [AWS Lambda: Security in AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/lambda-security.html)
- [CWE-20: Improper Input Validation](https://cwe.mitre.org/data/definitions/20.html)
