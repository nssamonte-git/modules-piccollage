---
id: offline-sync-conflict
title: Synchronization Conflicts
category: offline-sync
platforms: [shared, android, ios, backend]
owasp_refs: [A06:2025]
cwe: [CWE-362, CWE-345]
severity: medium
last_verified: 2026-10-29
aliases: [synchronization-conflicts]
---

# Synchronization Conflicts

## Why It Is a Threat

When multiple devices edit data offline and sync, naive conflict resolution (last-write-wins, often keyed on client timestamps) can drop or overwrite changes, resurrect deleted/revoked records, or be gamed by an attacker to win conflicts and override server state — causing data loss and integrity/authorization errors.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with test accounts and devices.

- Create conflicting offline edits (including deletes and revocations) across devices and sync.
- Review the conflict-resolution logic and whether attacker-favorable outcomes are possible, for example by forging a later client timestamp.

## How to Fix

- Use explicit, server-authoritative conflict resolution (server-assigned versions or vector clocks, server validation and authorization of each change); never let a client overwrite security-relevant state by winning a conflict (ASVS 5.0 V2.3.3 and V8.3.1).
- Preserve revocations and deletes (tombstones); apply changes atomically and log overwritten values for recovery (ASVS 5.0 V15.4 Safe Concurrency).

## References

- [CWE-362: Concurrent Execution using Shared Resource with Improper Synchronization](https://cwe.mitre.org/data/definitions/362.html)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
- [OWASP Top 10:2025 A06 Insecure Design](https://top10.owasp.org/2025/A06_2025-Insecure_Design/)
