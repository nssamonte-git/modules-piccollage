---
id: replay-attack
title: Replay Attack
category: network
platforms: [shared, web, backend, android, ios]
owasp_refs: [A07:2025, A06:2025]
cwe: [CWE-294]
severity: high
last_verified: 2026-10-29
aliases: [session-replay, request-replay]
---

# Replay Attack

## Why It Is a Threat

An attacker captures a valid request, token, or message and resends it to repeat an action or authenticate — e.g. replaying a payment, an auth token, or a signed message. Includes session replay and replay of financial operations (`business-logic`); see also `refresh-token-replay`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Capture and resend authenticated/state-changing requests, signed webhooks, and tokens; confirm whether nonces, timestamps, expiry, or idempotency prevent the replay.
- Check whether TLS 1.3 0-RTT (early data) is enabled for non-idempotent requests.

## How to Fix

- Use per-request nonces/timestamps with a short validity window and server-side single-use enforcement; give tokens short lifetimes and unique ids, and bind them to sender/context.
- Apply idempotency keys for sensitive operations; accept 0-RTT only for safe, idempotent requests (or disable it). Map to ASVS 5.0 V9 Self-contained Tokens (9.2.1), V4 (4.1.5), and V2 Validation and Business Logic (2.3.x).

## References

- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
- [CAPEC-60: Reusing Session IDs (aka Session Replay)](https://capec.mitre.org/data/definitions/60.html)
- [RFC 8470: Using Early Data in HTTP](https://www.rfc-editor.org/rfc/rfc8470)
