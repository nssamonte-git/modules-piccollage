---
id: db-credential-theft
title: Database Credential Theft
category: backend-database
platforms: [backend]
owasp_refs: [A02:2025, A07:2025]
cwe: [CWE-522, CWE-798]
severity: high
last_verified: 2026-10-29
---

# Database Credential Theft

## Why It Is a Threat

Database credentials hard-coded in source or config, leaked in logs, images, or env vars, or shared across services give an attacker direct read and write access to all data, bypassing application controls entirely. Related: `secrets-leakage`, `secrets-in-env-vars`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Search source, config, images, logs, and env for database credentials; check whether they are per-service, rotated, and stored in a secrets manager.

## How to Fix

- Store database credentials in a secrets manager and retrieve them at runtime; prefer short-lived or automatically rotated credentials (for example IAM database authentication); never commit or log them.
- Use a separate account per service and restrict database network access to application subnets. Map to ASVS 5.0 V13.3 Secret Management and V13.2 Backend Communication Configuration (13.2.1); see `improper-secrets-management`.

## References

- [OWASP Database Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Database_Security_Cheat_Sheet.html)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [Amazon RDS: IAM database authentication](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.html)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
