---
id: traffic-analysis
title: Traffic and Metadata Analysis
category: network
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-PRIVACY]
cwe: [CWE-200]
severity: low
last_verified: 2026-10-29
aliases: [metadata-analysis]
---

# Traffic and Metadata Analysis

## Why It Is a Threat

Even with TLS, observable traffic metadata — endpoints contacted, timing, sizes, and frequency — can reveal sensitive behavior (which features/records a user accesses, health/finance activity), enabling profiling and inference without breaking encryption.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review whether request sizes/timing/endpoints distinguish sensitive actions; assess SNI/DNS exposure and predictable patterns.

## How to Fix

- Reduce distinguishing signals (padding, batching, constant-size responses) for highly sensitive flows; enable Encrypted Client Hello (RFC 9849) and encrypted DNS (DoH/DoT) where supported.
- Minimize sensitive metadata in URLs, hostnames, and SNI. Map to ASVS 5.0 V12 Secure Communication (12.1.5) and V14 (14.2.1); see `metadata-leakage`.

## References

- [RFC 9849: TLS Encrypted Client Hello](https://www.rfc-editor.org/rfc/rfc9849)
- [RFC 9076: DNS Privacy Considerations](https://www.rfc-editor.org/rfc/rfc9076)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
