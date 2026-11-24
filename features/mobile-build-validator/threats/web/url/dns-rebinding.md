---
id: dns-rebinding
title: DNS Rebinding
category: web-url
platforms: [web]
owasp_refs: [A01:2025]
cwe: [CWE-350, CWE-918]
severity: medium
last_verified: 2026-10-29
---

# DNS Rebinding

## Why It Is a Threat

An attacker's domain first resolves to the attacker's server, then (after a short TTL) rebinds to an internal or loopback address, so the victim's browser makes "same-origin" requests to internal services (routers, local dev servers, admin panels, cloud metadata) that the attacker could not reach directly. The same trick defeats server-side SSRF filters that validate a hostname's IP once and then resolve it again when connecting.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- For local and internal web services, test whether requests with an unexpected `Host` header are accepted and whether they rely only on network position, without authentication.
- For server-side fetchers, check whether the validated IP is the one actually connected to.

## How to Fix

- Validate the `Host` header against an allowlist and require authentication on local and internal services; do not trust network position; prefer HTTPS with valid hostnames.
- Use resolvers that drop private addresses from public DNS answers; for server-side fetches, resolve once, validate the IP, and connect to that address (`ssrf`).
- Map to ASVS 5.0 V1.3.6 (SSRF) and V13.2.1 (authenticated backend communication); see `ssrf`, `unauthenticated-service-to-service`.

## References

- [CAPEC-275: DNS Rebinding](https://capec.mitre.org/data/definitions/275.html)
- [CWE-350: Reliance on Reverse DNS Resolution for a Security-Critical Action](https://cwe.mitre.org/data/definitions/350.html)
- [OWASP Server Side Request Forgery Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html)
