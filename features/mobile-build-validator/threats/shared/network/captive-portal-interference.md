---
id: captive-portal-interference
title: Captive-Portal Interference
category: network
platforms: [shared, android, ios, web]
owasp_refs: [A04:2025, MASVS-NETWORK]
cwe: [CWE-300]
severity: low
last_verified: 2026-10-29
---

# Captive-Portal Interference

## Why It Is a Threat

Captive portals (hotel/airport Wi-Fi) intercept and redirect early traffic and can inject content or present fake TLS, creating an attacker-in-the-middle opportunity if the app degrades security to "work" on such networks.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Test app behavior on a captive-portal network; confirm it does not disable TLS validation, fall back to cleartext, accept injected content, or leak data before the portal is cleared.

## How to Fix

- Never relax certificate validation for captive portals; leave portal sign-in to the operating system's captive-portal flow and keep it isolated from authenticated app traffic.
- Defer sensitive requests until a validated TLS connection succeeds; treat redirects and TLS failures as "no connectivity", not as a reason to retry insecurely. Map to MASVS-NETWORK-1 and ASVS 5.0 V12 (12.2.1); see `attacker-in-the-middle`.

## References

- [RFC 8952: Captive Portal Architecture](https://www.rfc-editor.org/rfc/rfc8952)
- [OWASP MASVS-NETWORK](https://mas.owasp.org/MASVS/08-MASVS-NETWORK/)
- [CWE-300: Channel Accessible by Non-Endpoint](https://cwe.mitre.org/data/definitions/300.html)
