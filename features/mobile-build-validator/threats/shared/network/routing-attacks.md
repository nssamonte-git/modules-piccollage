---
id: routing-attacks
title: Network-Routing and BGP Attacks
category: network
platforms: [shared, backend]
owasp_refs: [A07:2025, A04:2025]
cwe: [CWE-290, CWE-300]
severity: medium
last_verified: 2026-10-29
aliases: [bgp-routing-attacks, upstream-routing-attacks]
---

# Network-Routing and BGP Attacks

## Why It Is a Threat

Adversaries manipulate routing (BGP hijack/leak, upstream interception) to redirect traffic through attacker-controlled infrastructure, enabling interception, certificate-issuance abuse, and outages. Largely infrastructure-level but affects app trust assumptions.

## How to Find or Test

Authorized testing only (review, not live routing attacks; follow the `smx-ai-vapt` authorization gate).

- Review reliance on network-path or source-IP trust; assess exposure if traffic is rerouted (does TLS validation still protect it?); check whether your own prefixes have RPKI route origin authorizations.

## How to Fix

- Treat the network as untrusted; rely on TLS validation and pinning where appropriate; use CAA and Certificate Transparency monitoring to limit hijack-enabled issuance.
- Publish RPKI ROAs for owned prefixes and prefer providers that do route origin validation and route filtering (NIST SP 800-189). Map to ASVS 5.0 V12 Secure Communication.

## References

- [NIST SP 800-189: Resilient Interdomain Traffic Exchange: BGP Security and DDoS Mitigation](https://csrc.nist.gov/pubs/sp/800/189/final)
- [RFC 6811: BGP Prefix Origin Validation](https://www.rfc-editor.org/rfc/rfc6811)
- [CWE-300: Channel Accessible by Non-Endpoint](https://cwe.mitre.org/data/definitions/300.html)
