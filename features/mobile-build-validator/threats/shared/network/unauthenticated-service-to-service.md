---
id: unauthenticated-service-to-service
title: Unauthenticated Service-to-Service Traffic
category: network
platforms: [shared, backend]
owasp_refs: [A07:2025, A04:2025, A01:2025]
cwe: [CWE-306, CWE-319]
severity: high
last_verified: 2026-10-29
---

# Unauthenticated Service-to-Service Traffic

## Why It Is a Threat

Internal microservice/API traffic that assumes the network is trusted — no mutual auth, no encryption — lets any foothold inside the network call sensitive internal services, move laterally, and read data. Defeats perimeter-only security.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review internal service calls for authentication, authorization, and TLS; test whether internal endpoints are reachable and usable without credentials from a peer context.

## How to Fix

- Adopt zero trust: mutual TLS or signed short-lived workload tokens for per-service identity, authorization on every call, and encrypted service-to-service traffic with no cleartext fallback.
- Segment networks and use least-privilege service accounts (`service-account-compromise`); propagate the end user's identity where decisions depend on it. Map to ASVS 5.0 V12 Secure Communication (12.3.1, 12.3.3, 12.3.5) and V13 Configuration (13.2.1, 13.2.2).

## References

- [OWASP Microservices Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Microservices_Security_Cheat_Sheet.html)
- [NIST SP 800-207: Zero Trust Architecture](https://csrc.nist.gov/pubs/sp/800/207/final)
- [CWE-306: Missing Authentication for Critical Functionality](https://cwe.mitre.org/data/definitions/306.html)
