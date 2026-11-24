---
id: service-account-compromise
title: Service-Account Compromise
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025, A01:2025]
cwe: [CWE-522, CWE-269]
severity: high
last_verified: 2026-10-29
---

# Service-Account Compromise

## Why It Is a Threat

Machine and service accounts (CI, integrations, daemons) often hold broad, long-lived privileges and static keys with weak monitoring. Compromise yields powerful, low-visibility access and lateral movement. Related: `config-infra` over-permissive IAM and `confused-deputy`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory service accounts and their scopes; check for shared or static secrets (keys in repos, CI variables, images), missing rotation, excessive privileges, unused accounts, and absence of usage anomaly alerts.

## How to Fix

- Give each workload its own identity with least privilege; replace static keys with short-lived credentials (workload identity federation, OIDC from CI, managed identities, certificates).
- Store any remaining secrets in a secrets manager with rotation; monitor and alert on unusual service-account use; disable unused accounts. Map to ASVS 5.0 V13 Configuration (13.2.1, 13.2.2, 13.3).

## References

- [Google Cloud: Best practices for using service accounts securely](https://cloud.google.com/iam/docs/best-practices-service-accounts)
- [GitHub Docs: OpenID Connect (security hardening for deployments)](https://docs.github.com/en/actions/security-for-github-actions/security-hardening-your-deployments/about-security-hardening-with-openid-connect)
- [MITRE ATT&CK T1078.004: Valid Accounts: Cloud Accounts](https://attack.mitre.org/techniques/T1078/004/)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
