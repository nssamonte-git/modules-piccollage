---
id: connection-pool-exhaustion
title: Connection / Thread / Queue Pool Exhaustion
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023, A10:2025, A06:2025]
cwe: [CWE-410, CWE-400, CWE-770]
severity: high
last_verified: 2026-10-29
aliases: [database-connection-exhaustion, thread-pool-exhaustion, queue-exhaustion, websocket-connection-exhaustion]
---

# Connection / Thread / Queue Pool Exhaustion

## Why It Is a Threat

Finite pools (DB connections, worker threads, job queues, WebSocket connections) are drained by slow, leaked, or excessive use, so the app stops serving even under modest load. Connections not released on error paths, or one slow dependency holding every worker, can cascade into total unavailability.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review pool sizes, acquisition timeouts, per-client connection caps, and whether resources are released in `finally`/error paths.
- In non-production, slow a dependency or open many long-lived connections and observe pool saturation and recovery.

## How to Fix

- Bound pools with acquisition timeouts and leak detection; always release resources on exceptions; cap concurrent WebSocket and long-lived connections per client.
- Isolate pools per dependency (bulkheads), apply backpressure and circuit breakers, and document limits and behavior at the limit. Map to API4:2023, ASVS 5.0 V13 Configuration (13.1.2, 13.2.6) and ASVS 5.0 V15 Secure Coding and Architecture (15.4.4).

## References

- [CWE-410: Insufficient Resource Pool](https://cwe.mitre.org/data/definitions/410.html)
- [OWASP Top 10:2025 A10 Mishandling of Exceptional Conditions](https://top10.owasp.org/2025/A10_2025-Mishandling_of_Exceptional_Conditions/)
- [Microsoft Learn: Bulkhead Pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/bulkhead)
- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
