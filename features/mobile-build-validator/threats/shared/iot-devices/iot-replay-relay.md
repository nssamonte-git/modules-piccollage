---
id: iot-replay-relay
title: Replay and Relay Attacks
category: iot-devices
platforms: [shared, android, ios, backend]
owasp_refs: [MASVS-NETWORK]
cwe: [CWE-294]
severity: high
last_verified: 2026-10-29
aliases: [iot-replay-attack, relay-attack]
---

# Replay and Relay Attacks

## Why It Is a Threat

Captured device commands/auth exchanges are replayed to repeat actions (unlock, pay), and relay attacks forward a legitimate exchange over distance to defeat proximity assumptions (e.g. keyless entry, BLE phone-as-key, contactless payment) — without breaking crypto. Signal strength (RSSI) is not proof of proximity. Network-layer cousin: `replay-attack`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on owner-authorized devices.

- Capture and resend device exchanges in a test setup; check whether nonces, counters, or timestamps prevent replay.
- Assess whether proximity-sensitive actions rely only on connection or RSSI, or use timing, distance bounding, or user presence.

## How to Fix

- Use per-exchange challenge-response with fresh nonces or monotonic counters, short validity, and single use; protect messages with authenticated encryption.
- For proximity-sensitive actions add distance bounding or secure ranging (e.g. UWB) and timing checks, plus explicit user presence. Bind transactions to context. Map to MASVS-NETWORK; see `proximity-wireless-interception`.

## References

- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
- [Android Developers: Insecure machine-to-machine communication setup](https://developer.android.com/privacy-and-security/risks/insecure-machine-to-machine)
- [OWASP MASVS-NETWORK](https://mas.owasp.org/MASVS/08-MASVS-NETWORK/)
