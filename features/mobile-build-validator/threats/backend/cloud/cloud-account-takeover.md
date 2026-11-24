---
id: cloud-account-takeover
title: Cloud-Account Takeover
category: backend-cloud
platforms: [backend]
owasp_refs: [A07:2025, A01:2025]
cwe: [CWE-287, CWE-308, CWE-269]
severity: critical
last_verified: 2026-10-29
---

# Cloud-Account Takeover

## Why It Is a Threat

An attacker gains control of cloud console or API access (phished or leaked root or admin credentials, no MFA, exposed long-lived access keys, stolen sessions or tokens) and owns the entire environment: data, infrastructure, and the ability to destroy or ransom it. This is the highest-impact cloud outcome (MITRE ATT&CK T1078.004 Valid Accounts: Cloud Accounts). Related: `oauth-device-code-phishing`, `cloud-audit-logging-gaps`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate). Posture review.

- Review root, super-admin, and global-admin protection (phishing-resistant MFA, no routine use, no root access keys), long-lived keys in code or CI, and break-glass account controls.

## How to Fix

- Enforce phishing-resistant MFA (FIDO2 security keys or passkeys) on all privileged identities; do not use root or super-admin for daily work; use federated SSO and short-lived credentials; find and rotate leaked keys.
- Keep a small number of monitored emergency-access accounts, and alert on anomalous console and API activity. Map to A07:2025; see `over-permissive-iam`.

## References

- [AWS IAM: Root user best practices for your AWS account](https://docs.aws.amazon.com/IAM/latest/UserGuide/root-user-best-practices.html)
- [AWS IAM: Security best practices in IAM](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html)
- [Microsoft Entra: Manage emergency access admin accounts](https://learn.microsoft.com/en-us/entra/identity/role-based-access-control/security-emergency-access)
- [Google Cloud: Super administrator account best practices](https://cloud.google.com/resource-manager/docs/super-admin-best-practices)
- [MITRE ATT&CK T1078.004: Valid Accounts: Cloud Accounts](https://attack.mitre.org/techniques/T1078/004/)
