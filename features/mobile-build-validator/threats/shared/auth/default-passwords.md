---
id: default-passwords
title: Default Passwords
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHN-02, A07:2025, A02:2025]
cwe: [CWE-1392, CWE-1393, CWE-798]
severity: high
last_verified: 2026-10-29
---

# Default Passwords

## Why It Is a Threat

Shipped or seeded accounts retain vendor or default credentials (admin/admin, sample users, demo API keys), giving attackers trivial access. The infrastructure/service instance is tracked under `config-infra` (default-credentials).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Attempt documented default credentials on application accounts, admin panels, and seeded demo users; check seed/migration data and installers for fixed passwords or shared initial secrets.

## How to Fix

- Remove or disable default accounts before production; generate unique, random initial secrets per install or user that expire quickly and must be changed on first use.
- Never commit default credentials; prefer setup flows that force the operator to create the first admin credential. Map to ASVS 5.0 V6 Authentication (6.3.2, 6.4.1) and see `config-infra` default-credentials.

## References

- [OWASP WSTG-ATHN-02: Testing for Default Credentials](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/02-Default_Credentials)
- [CWE-1392: Use of Default Credentials](https://cwe.mitre.org/data/definitions/1392.html)
- [CISA Secure by Design Alert: Eliminating Default Passwords](https://www.cisa.gov/resources-tools/resources/secure-design-alert-how-manufacturers-can-protect-customers-eliminating-default-passwords)
- [MITRE ATT&CK T1078.001: Valid Accounts: Default Accounts](https://attack.mitre.org/techniques/T1078/001/)
