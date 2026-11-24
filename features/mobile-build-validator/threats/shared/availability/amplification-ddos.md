---
id: amplification-ddos
title: Amplification / Reflection DDoS
category: availability
platforms: [shared, backend]
owasp_refs: [A06:2025]
cwe: [CWE-406]
severity: high
last_verified: 2026-10-29
aliases: [dns-amplification, udp-amplification]
---

# Amplification / Reflection DDoS

## Why It Is a Threat

Attackers spoof the victim's source address and send small requests to connectionless UDP services (DNS, NTP, SSDP, CLDAP, memcached, and others) that answer with much larger responses, so third-party servers flood the victim. Your infrastructure can be the target, or an unwitting reflector that attacks others and consumes your own bandwidth.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory internet-facing UDP services you own and confirm none answer unauthenticated amplifying queries (open DNS recursion, legacy NTP status queries such as `monlist`, SSDP, CLDAP, memcached over UDP).
- Confirm with network owners that source-address anti-spoofing (BCP 38) is applied at the edge and that upstream DDoS mitigation covers reflected traffic.

## How to Fix

- Disable or firewall amplifiable UDP services that do not need public exposure; turn off open recursion and legacy NTP query modes; apply DNS response rate limiting.
- Enforce BCP 38 ingress/egress filtering and use an upstream DDoS scrubbing provider to absorb reflected floods. Map to the infrastructure runbook.

## References

- [CISA: UDP-Based Amplification Attacks](https://www.cisa.gov/news-events/alerts/2014/01/17/udp-based-amplification-attacks)
- [MITRE ATT&CK T1498.002: Reflection Amplification](https://attack.mitre.org/techniques/T1498/002/)
- [CWE-406: Insufficient Control of Network Message Volume (Network Amplification)](https://cwe.mitre.org/data/definitions/406.html)
- [RFC 2827 (BCP 38): Network Ingress Filtering](https://www.rfc-editor.org/rfc/rfc2827)
