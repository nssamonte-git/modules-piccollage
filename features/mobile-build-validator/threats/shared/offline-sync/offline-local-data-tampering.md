---
id: offline-local-data-tampering
title: Local Database Tampering
category: offline-sync
platforms: [shared, android, ios]
owasp_refs: [MASVS-STORAGE, A08:2025]
cwe: [CWE-602, CWE-345]
severity: high
last_verified: 2026-10-29
---

# Local Database Tampering

## Why It Is a Threat

The local offline store (SQLite/Room/Core Data/files) is modifiable by the user/attacker on a rooted/jailbroken device or through backup restore, so changes to balances, entitlements, prices, or flags sync up to the server if the server trusts client data — turning client tampering into server-side fraud.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on an owner-authorized device.

- Modify local records (balances/roles/prices) on a test account and sync.
- Confirm whether the server validates and recomputes values or trusts the client copy, and whether the app detects tampered local data.

## How to Fix

- Treat synced client data as untrusted: validate and recompute authoritative values at a trusted server layer (ASVS 5.0 V2.2.2 and V8.3.1); never accept client-supplied money/role/entitlement as truth.
- Sign or MAC critical local data with a Keystore/Keychain-held key and encrypt it at rest; add anti-tampering checks as defense in depth (MASVS-RESILIENCE-2). See `request-tampering`.

## References

- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [OWASP MASVS-RESILIENCE](https://mas.owasp.org/MASVS/11-MASVS-RESILIENCE/)
