---
id: dns-attacks
title: DNS Spoofing, Poisoning, and Hijacking
category: network
platforms: [shared, web, backend]
owasp_refs: [A07:2025, A02:2025]
cwe: [CWE-350]
severity: high
last_verified: 2026-10-29
aliases: [dns-spoofing, dns-poisoning, dns-hijacking]
---

# DNS Spoofing, Poisoning, and Hijacking

## Why It Is a Threat

An attacker forges DNS responses, poisons resolver caches, or hijacks a domain/registrar/resolver to redirect clients to attacker-controlled servers — enabling interception, phishing, and certificate issuance abuse. Dangling records are covered by `dns-misconfig-subdomain-takeover`.

## How to Find or Test

Authorized testing only (configuration review, not third-party attacks; follow the `smx-ai-vapt` authorization gate).

- Review use of DNSSEC, resolver trust, registrar/account protections (MFA, registry lock), CAA records, and whether the app trusts hostnames or reverse DNS without TLS validation as a backstop.

## How to Fix

- Rely on TLS certificate validation as the trust anchor (DNS is not trustworthy) and never use DNS or reverse-DNS results for authentication.
- Sign zones with DNSSEC and use validating or encrypted (DoH/DoT) resolvers; lock registrar/DNS accounts with MFA and registry lock; publish CAA records and monitor Certificate Transparency for unexpected issuance. Map to ASVS 5.0 V12 Secure Communication (12.2.1, 12.2.2).

## References

- [CAPEC-142: DNS Cache Poisoning](https://capec.mitre.org/data/definitions/142.html)
- [NIST SP 800-81 Rev. 3: Secure Domain Name System (DNS) Deployment Guide](https://csrc.nist.gov/pubs/sp/800/81/r3/final)
- [RFC 8659: DNS Certification Authority Authorization (CAA) Resource Record](https://www.rfc-editor.org/rfc/rfc8659)
- [CWE-350: Reliance on Reverse DNS Resolution for a Security-Critical Action](https://cwe.mitre.org/data/definitions/350.html)
