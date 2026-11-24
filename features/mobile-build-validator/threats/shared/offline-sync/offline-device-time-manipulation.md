---
id: offline-device-time-manipulation
title: Manipulated Device Time
category: offline-sync
platforms: [shared, android, ios]
owasp_refs: [A06:2025]
cwe: [CWE-602, CWE-807]
severity: medium
last_verified: 2026-10-29
---

# Manipulated Device Time

## Why It Is a Threat

Offline logic that trusts the device clock can be gamed by changing it: extending expired trials/licenses/tokens, bypassing time-based limits or rate windows, or backdating records — because the client clock is attacker-controlled.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Change device time and exercise time-based features (trials, token expiry, limits) offline.
- Confirm whether the server re-validates time-dependent decisions and record timestamps on reconnect.

## How to Fix

- Do not trust the client clock for security or business decisions; enforce time limits at a trusted server layer (ASVS 5.0 V2.2.2 and V2.3.2); use server-issued, signed timestamps and expiry, and record server receipt time alongside client time.
- For local intervals use a monotonic clock (time since boot) rather than wall-clock time, and detect implausible time jumps. See `trial-subscription-abuse`.

## References

- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
- [CWE-807: Reliance on Untrusted Inputs in a Security Decision](https://cwe.mitre.org/data/definitions/807.html)
- [OWASP Top 10:2025 A06 Insecure Design](https://top10.owasp.org/2025/A06_2025-Insecure_Design/)
