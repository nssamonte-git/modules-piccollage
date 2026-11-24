---
id: open-ports-firewall
title: Unnecessary Open Ports / Over-Permissive Firewall
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025]
cwe: [CWE-1327, CWE-284]
severity: high
last_verified: 2026-10-29
aliases: [unnecessary-open-ports, over-permissive-firewall-rules]
---

# Unnecessary Open Ports / Over-Permissive Firewall

## Why It Is a Threat

Services bound to public interfaces, or firewall and security-group rules that allow any source (`0.0.0.0/0`, `::/0`), expose admin ports (SSH, RDP), databases, caches, and internal services to the internet, widening the attack surface for exploitation and brute force.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory listening ports and ingress rules for IPv4 and IPv6; flag public exposure of management, database, and internal ports and any-source rules; compare an external scan of in-scope ranges with the intended exposure.

## How to Fix

- Default-deny ingress; expose only required ports to required sources; bind internal services to private interfaces or localhost.
- Reach admin interfaces through a bastion, VPN, or identity-aware proxy, and review security groups continuously with policy-as-code. Map to A02:2025 and ASVS 5.0 V13 Configuration.

## References

- [CWE-1327: Binding to an Unrestricted IP Address](https://cwe.mitre.org/data/definitions/1327.html)
- [CISA AA23-278A: Top Ten Cybersecurity Misconfigurations](https://www.cisa.gov/news-events/cybersecurity-advisories/aa23-278a)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
