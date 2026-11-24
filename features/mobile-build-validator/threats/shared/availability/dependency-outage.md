---
id: dependency-outage
title: Dependency or Cloud-Provider Outage
category: availability
platforms: [shared, web, backend]
owasp_refs: [A10:2025, A06:2025]
cwe: [CWE-1088, CWE-754, CWE-400]
severity: medium
last_verified: 2026-10-29
aliases: [cloud-provider-outage, third-party-outage]
---

# Dependency or Cloud-Provider Outage

## Why It Is a Threat

A critical third party (payment, auth, cloud region, SaaS API) fails or slows down and takes your service with it, because calls block without timeouts and there is no graceful degradation, failover, or isolation. Availability becomes hostage to external systems, and error paths may fail open.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map critical external dependencies and single points of failure; check that every remote call has a timeout.
- Run tabletop exercises or inject faults in non-production and observe degradation, and whether failures fail closed rather than skipping security checks.

## How to Fix

- Add timeouts, circuit breakers, and graceful degradation; isolate dependencies (bulkheads) so one cannot exhaust shared resources.
- Use multi-AZ or multi-region designs and queued retries for non-critical paths, and keep a tested contingency plan. Map to ASVS 5.0 V16 Security Logging and Error Handling (16.5.2, 16.5.3) and ASVS 5.0 V13 Configuration (13.1.3).

## References

- [AWS Well-Architected REL05-BP01: Implement graceful degradation](https://docs.aws.amazon.com/wellarchitected/latest/reliability-pillar/rel_mitigate_interaction_failure_graceful_degradation.html)
- [Microsoft Learn: Circuit Breaker Pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/circuit-breaker)
- [CWE-1088: Synchronous Access of Remote Resource without Timeout](https://cwe.mitre.org/data/definitions/1088.html)
- [OWASP Top 10:2025 A10 Mishandling of Exceptional Conditions](https://top10.owasp.org/2025/A10_2025-Mishandling_of_Exceptional_Conditions/)
