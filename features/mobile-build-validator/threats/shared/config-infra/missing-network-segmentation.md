---
id: missing-network-segmentation
title: Missing Network Segmentation
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-653, CWE-923]
severity: medium
last_verified: 2026-10-29
---

# Missing Network Segmentation

## Why It Is a Threat

A flat network lets any compromised host reach databases, internal services, and management planes, enabling broad lateral movement from a single foothold. Compounds `unauthenticated-service-to-service`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review network topology, security groups, and Kubernetes network policies; check that app, data, and management tiers are isolated and that east-west and egress traffic is restricted.

## How to Fix

- Segment into tiers or zones with default-deny east-west and egress rules; isolate data and management planes; authenticate every service call rather than trusting network location (zero trust).
- Use private subnets, microsegmentation, and network policies, and allowlist outbound destinations. Map to A02:2025 and ASVS 5.0 V13.2 Backend Communication Configuration.

## References

- [OWASP Network Segmentation Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Network_Segmentation_Cheat_Sheet.html)
- [NIST SP 800-207: Zero Trust Architecture](https://csrc.nist.gov/pubs/sp/800/207/final)
- [CISA: Layering Network Security Through Segmentation](https://www.cisa.gov/resources-tools/resources/layering-network-security-through-segmentation-infographic)
- [Kubernetes: Network Policies](https://kubernetes.io/docs/concepts/services-networking/network-policies/)
