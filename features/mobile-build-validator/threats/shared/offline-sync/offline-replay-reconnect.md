---
id: offline-replay-reconnect
title: Replay After Reconnection / Duplicate Submission
category: offline-sync
platforms: [shared, android, ios, backend]
owasp_refs: [A06:2025]
cwe: [CWE-294, CWE-837]
severity: high
last_verified: 2026-10-29
aliases: [replay-after-reconnection, duplicate-transaction-submission]
---

# Replay After Reconnection / Duplicate Submission

## Why It Is a Threat

Queued offline operations are replayed on reconnect; without idempotency an attacker (or a buggy retry) resubmits the same transaction multiple times — double-spending, duplicate orders/payments, or repeated side effects. Queued requests stored on the device can also be edited before they sync. Online cousins: `replay-attack`, `race-condition`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with a test account.

- Queue an operation offline, resend it on reconnect, and confirm whether the server deduplicates.
- Check whether a modified queued request is accepted and whether stale operations are rejected.

## How to Fix

- Attach a unique, client-generated idempotency key to each operation and enforce single execution server-side; reject stale or duplicate submissions using server-side state, not client timestamps.
- Wrap side effects in transactions and locks so an operation fully succeeds or rolls back (ASVS 5.0 V2.3.3 and V2.3.4); re-authorize queued operations on sync. See `race-condition`.

## References

- [CWE-837: Improper Enforcement of a Single, Unique Action](https://cwe.mitre.org/data/definitions/837.html)
- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
- [OWASP Transaction Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Transaction_Authorization_Cheat_Sheet.html)
