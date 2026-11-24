---
id: resource-exhaustion
title: Compute / Memory / Disk Exhaustion
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023, A06:2025]
cwe: [CWE-400, CWE-770]
severity: high
last_verified: 2026-10-29
aliases: [cpu-exhaustion, memory-exhaustion, disk-exhaustion, file-descriptor-exhaustion]
---

# Compute / Memory / Disk Exhaustion

## Why It Is a Threat

Attacker input drives unbounded consumption of CPU, memory, disk, or file descriptors: huge payloads, unbounded allocations or array sizes, log or upload floods filling disk, or leaked descriptors. The service crashes or freezes. Includes ReDoS (`redos`) and decompression bombs (`zip-bomb`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify endpoints that allocate or compute in proportion to input; test bounded large or complex inputs against limits and observe resource growth.
- Source review by stack (`source-code-vapt`): Rust `unwrap()` or indexing on request data, `Vec::with_capacity(user_len)`, axum `DefaultBodyLimit::disable()`, and `bincode` without size limits.

## How to Fix

- Limit request body size, string lengths, array element counts, and processing time; cap upload, log, and temp-file usage with quotas and cleanup.
- Use streaming, timeouts, and container or process limits on memory, CPU, and file descriptors; isolate heavy work in limited workers. Map to API4:2023 and ASVS 5.0 V15 Secure Coding and Architecture (15.2.2).

## References

- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-400: Uncontrolled Resource Consumption](https://cwe.mitre.org/data/definitions/400.html)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
- [OWASP Denial of Service Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Denial_of_Service_Cheat_Sheet.html)
