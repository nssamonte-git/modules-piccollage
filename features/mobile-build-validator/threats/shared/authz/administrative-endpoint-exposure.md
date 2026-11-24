---
id: administrative-endpoint-exposure
title: Administrative Endpoint Exposure
category: authz
platforms: [shared, web, backend]
owasp_refs: [A01:2025, A02:2025, WSTG-CONF-05]
cwe: [CWE-749, CWE-306]
severity: high
last_verified: 2026-10-29
---

# Administrative Endpoint Exposure

## Why It Is a Threat

Admin consoles, management APIs, actuator/debug endpoints, or internal dashboards are reachable from untrusted networks or by non-admin users. Exposure provides a direct, high-value path to data and configuration, often with weak or default authentication.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Enumerate admin/management paths (`/admin`, `/actuator`, `/_debug`, framework and middleware consoles, API docs); check reachability from the public network and as a low-privilege user.
- Verify authentication strength, admin-role checks, and network restrictions; check for default credentials and debug modes left on in production.

## How to Fix

- Require strong (phishing-resistant where possible) authentication and explicit admin authorization on every management endpoint; do not treat network location or a trusted host as the only control.
- Restrict to trusted networks or a separate management plane (VPN/allowlist); disable unused debug, actuator, and documentation endpoints in production. Map to ASVS 5.0 V8 Authorization (8.4.2) and V13 Configuration (13.4.2, 13.4.5).

## References

- [OWASP WSTG-CONF-05: Enumerate Infrastructure and Application Admin Interfaces](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/05-Enumerate_Infrastructure_and_Application_Admin_Interfaces/)
- [CWE-749: Exposed Dangerous Method or Function](https://cwe.mitre.org/data/definitions/749.html)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
