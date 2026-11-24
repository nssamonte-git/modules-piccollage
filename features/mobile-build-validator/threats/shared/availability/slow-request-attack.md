---
id: slow-request-attack
title: Slow-Request Attacks
category: availability
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-400, CWE-770]
severity: medium
last_verified: 2026-10-29
aliases: [slowloris, slow-read, slow-post]
---

# Slow-Request Attacks

## Why It Is a Threat

Attackers open many connections and send or read data very slowly (Slowloris, slow POST, slow read), holding server worker and connection slots with minimal bandwidth until capacity is exhausted. Thread-per-connection servers are most exposed.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review server and proxy timeouts for headers, body, and idle connections, minimum data rates, and per-client connection limits; check whether a reverse proxy buffers requests before they reach the app.

## How to Fix

- Set short header, body, and idle timeouts with minimum data-rate requirements; cap concurrent connections per client.
- Front the app with a buffering reverse proxy or CDN and apply the same limits to HTTP/2 streams. Map to ASVS 5.0 V13 Configuration (13.2.6) and the infrastructure runbook.

## References

- [CAPEC-469: HTTP DoS](https://capec.mitre.org/data/definitions/469.html)
- [MITRE ATT&CK T1499.002: Service Exhaustion Flood](https://attack.mitre.org/techniques/T1499/002/)
- [OWASP Denial of Service Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Denial_of_Service_Cheat_Sheet.html)
