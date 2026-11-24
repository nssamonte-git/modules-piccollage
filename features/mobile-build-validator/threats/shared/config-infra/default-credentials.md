---
id: default-credentials
title: Default Credentials (Infrastructure)
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A07:2025]
cwe: [CWE-1392, CWE-798]
severity: high
last_verified: 2026-10-29
---

# Default Credentials (Infrastructure)

## Why It Is a Threat

Infrastructure and service components (databases, admin panels, message brokers, network devices, dashboards, CI servers) ship with well-known default credentials or no password at all. Left unchanged, they give attackers trivial authenticated access and are routinely exploited at scale. Application-account variant: `default-passwords`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory services and check them against vendor documentation for default or blank accounts; review provisioning templates and images for unchanged vendor defaults.
- Validate default logins only against in-scope systems, within agreed lockout limits.

## How to Fix

- Remove or disable default accounts; set unique, strong per-instance credentials at provisioning and keep them in a secrets manager; prefer products that force a credential change on first use.
- Add configuration checks that fail builds or deployments on known defaults. Map to A02:2025 and ASVS 5.0 V13.2 (no default service credentials) and V6.3 (no default user accounts).

## References

- [CWE-1392: Use of Default Credentials](https://cwe.mitre.org/data/definitions/1392.html)
- [CISA Secure by Design Alert: Eliminating Default Passwords](https://www.cisa.gov/resources-tools/resources/secure-design-alert-how-manufacturers-can-protect-customers-eliminating-default-passwords)
- [OWASP WSTG: Testing for Default Credentials](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/02-Default_Credentials)
- [MITRE ATT&CK T1078.001: Valid Accounts: Default Accounts](https://attack.mitre.org/techniques/T1078/001/)
