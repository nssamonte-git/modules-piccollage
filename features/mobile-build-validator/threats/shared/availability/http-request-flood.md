---
id: http-request-flood
title: HTTP / API Request Flood
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023, A06:2025]
cwe: [CWE-400, CWE-770]
severity: high
last_verified: 2026-10-29
aliases: [api-request-flood, layer-7-ddos]
---

# HTTP / API Request Flood

## Why It Is a Threat

Application-layer floods send high volumes of valid-looking HTTP/API requests to exhaust app and database capacity (L7 DDoS). They are harder to filter than volumetric attacks because requests look legitimate, and protocol-level abuse such as HTTP/2 Rapid Reset (CVE-2023-44487) multiplies the load per connection.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), against non-production with caps.

- Assess per-client and global rate limiting, caching, and autoscaling on hot endpoints; identify unauthenticated heavy endpoints.
- Confirm edge proxies and servers are patched against known HTTP/2 flood issues.

## How to Fix

- Enforce rate limits and quotas per client and endpoint (respond `429` with `Retry-After`); cache aggressively; use a CDN or WAF with L7 DDoS protection and bot mitigation.
- Keep proxies patched, shed load gracefully, and have a DDoS response plan with your provider. Map to API4:2023, ASVS 5.0 V2 Validation and Business Logic (2.4.1), and the infrastructure runbook.

## References

- [CISA: Understanding and Responding to Distributed Denial-of-Service Attacks](https://www.cisa.gov/resources-tools/resources/understanding-and-responding-distributed-denial-service-attacks)
- [MITRE ATT&CK T1499.002: Service Exhaustion Flood](https://attack.mitre.org/techniques/T1499/002/)
- [CISA: HTTP/2 Rapid Reset Vulnerability, CVE-2023-44487](https://www.cisa.gov/news-events/alerts/2023/10/10/http2-rapid-reset-vulnerability-cve-2023-44487)
- [OWASP Denial of Service Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Denial_of_Service_Cheat_Sheet.html)
