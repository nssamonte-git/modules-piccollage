---
id: retry-storm
title: Retry Storms / Cascading Failure
category: availability
platforms: [shared, web, backend]
owasp_refs: [A10:2025, A06:2025]
cwe: [CWE-400]
severity: medium
last_verified: 2026-10-29
aliases: [retry-amplification, cascading-failure]
---

# Retry Storms / Cascading Failure

## Why It Is a Threat

When a dependency slows or fails, aggressive client and service retries multiply load, pushing the struggling system further down and cascading the outage across services. Retries at several layers compound each other, turning a partial failure into a self-inflicted DoS.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review retry policies across services and clients for backoff, jitter, attempt caps, and retries at multiple layers.
- In non-production, simulate a slow dependency and measure request amplification.

## How to Fix

- Use exponential backoff with jitter, cap attempts, retry at one layer only, and honor `Retry-After`; add circuit breakers, timeouts, and load shedding.
- Make operations idempotent so safe retries do not duplicate work (`race-condition`). Map to ASVS 5.0 V16 Security Logging and Error Handling (16.5.2) and the infrastructure runbook.

## References

- [Microsoft Learn: Retry Storm Antipattern](https://learn.microsoft.com/en-us/azure/architecture/antipatterns/retry-storm/)
- [AWS Well-Architected REL05-BP03: Control and limit retry calls](https://docs.aws.amazon.com/wellarchitected/latest/reliability-pillar/rel_mitigate_interaction_failure_limit_retries.html)
- [Microsoft Learn: Circuit Breaker Pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/circuit-breaker)
