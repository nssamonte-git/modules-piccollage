---
id: exposed-admin-consoles
title: Exposed Admin Consoles and Dashboards
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-306, CWE-749, CWE-284]
severity: high
last_verified: 2026-10-29
aliases: [exposed-kubernetes-dashboards, public-container-registries]
---

# Exposed Admin Consoles and Dashboards

## Why It Is a Threat

Infrastructure management surfaces (Kubernetes dashboard and API server, kubelet and container-runtime APIs, container registries, CI consoles, DB admin tools, monitoring UIs) are reachable from the internet, often with anonymous or weak authentication, giving direct control of the platform. App-route variant: `administrative-endpoint-exposure`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Enumerate internet-reachable management interfaces and registries; check for anonymous access, default or single-factor logins, and public network exposure.
- Source review by stack (`source-code-vapt`): Laravel Telescope, Horizon, Debugbar, or Ignition reachable in production without a gate.

## How to Fix

- Keep management surfaces on private networks reachable only through VPN, bastion, or identity-aware proxy, with SSO and MFA; disable anonymous access.
- Make registries and dashboards private by default and review exposure continuously. Map to A02:2025 and ASVS 5.0 V8.4 (layered protection for administrative interfaces) and V13.4 (monitoring endpoints not exposed).

## References

- [OWASP WSTG: Enumerate Infrastructure and Application Admin Interfaces](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/05-Enumerate_Infrastructure_and_Application_Admin_Interfaces)
- [CWE-306: Missing Authentication for Critical Function](https://cwe.mitre.org/data/definitions/306.html)
- [Kubernetes: Security Checklist](https://kubernetes.io/docs/concepts/security/security-checklist/)
- [CISA AA23-278A: Top Ten Cybersecurity Misconfigurations](https://www.cisa.gov/news-events/cybersecurity-advisories/aa23-278a)
