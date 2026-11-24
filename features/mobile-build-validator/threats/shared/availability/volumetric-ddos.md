---
id: volumetric-ddos
title: Volumetric (Network/Transport) DDoS
category: availability
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-400]
severity: high
last_verified: 2026-10-29
aliases: [network-layer-ddos, transport-layer-ddos, syn-flood, bandwidth-exhaustion]
---

# Volumetric (Network/Transport) DDoS

## Why It Is a Threat

Attackers flood the network or transport layer (UDP and packet floods, SYN floods, link saturation), often from botnets, to exhaust bandwidth and connection state so the service is unreachable regardless of application correctness.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate); do not run real floods against production.

- Review upstream DDoS protection or scrubbing, SYN-cookie and connection-state defenses, and bandwidth headroom; tabletop the response with your provider.

## How to Fix

- Use a DDoS-mitigation or CDN provider with always-on or on-demand scrubbing; enable SYN cookies and connection limits on exposed hosts.
- Provision bandwidth headroom and keep a tested incident runbook with provider and ISP contacts. Map to the deployment infrastructure runbook.

## References

- [CISA: Understanding and Responding to Distributed Denial-of-Service Attacks](https://www.cisa.gov/resources-tools/resources/understanding-and-responding-distributed-denial-service-attacks)
- [MITRE ATT&CK T1498.001: Direct Network Flood](https://attack.mitre.org/techniques/T1498/001/)
- [RFC 4987: TCP SYN Flooding Attacks and Common Mitigations](https://www.rfc-editor.org/rfc/rfc4987)
