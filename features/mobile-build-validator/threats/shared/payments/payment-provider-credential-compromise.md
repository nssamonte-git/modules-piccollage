---
id: payment-provider-credential-compromise
title: Payment-Provider Credential Compromise
category: payments
platforms: [shared, web, backend]
owasp_refs: [A02:2025, API2:2023]
cwe: [CWE-522, CWE-798]
severity: critical
last_verified: 2026-10-29
---

# Payment-Provider Credential Compromise

## Why It Is a Threat

Theft of payment-gateway secret API keys or webhook signing secrets lets an attacker create charges or refunds, read transaction and customer data, forge payment notifications, or redirect payouts: direct financial loss and a major breach. Keys leak via repos, logs, client code, CI, or environment dumps (`secrets-leakage`, `api-key-theft`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review where PSP secret keys and webhook signing secrets are stored, who can read them, their scope, and rotation; scan repos, build logs, mobile and web bundles, and logs for leaked keys.
- Confirm only publishable keys reach clients and that test and live keys are separated by environment.

## How to Fix

- Keep PSP secret keys server-side in a secrets manager with least-privilege access; use restricted or scoped keys and IP restrictions where the provider supports them; rotate keys and webhook secrets on a schedule and immediately after suspected exposure.
- Never expose secret keys to the client, and alert on anomalous API, refund, or payout activity. Map to API2:2023 and ASVS 5.0 V13 Configuration (13.3.1, 13.3.2, 13.3.4); see `webhook-security`, `improper-secrets-management`.

## References

- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-798: Use of Hard-coded Credentials](https://cwe.mitre.org/data/definitions/798.html)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
