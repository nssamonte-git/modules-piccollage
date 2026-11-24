---
id: proximity-wireless-interception
title: Bluetooth and NFC Interception / Relay
category: network
platforms: [shared, android, ios]
owasp_refs: [MASVS-NETWORK]
cwe: [CWE-300, CWE-294]
severity: medium
last_verified: 2026-10-29
aliases: [bluetooth-interception, nfc-relay]
---

# Bluetooth and NFC Interception / Relay

## Why It Is a Threat

Short-range radios can be attacked: Bluetooth pairing/traffic interception (legacy or "Just Works" pairing, which gives no MITM protection) and NFC relay attacks (forwarding NFC exchanges over distance to defeat proximity assumptions, e.g. payments/access). Enables eavesdropping, spoofing, and unauthorized transactions.

## How to Find or Test

Authorized testing only, on owner-authorized devices (follow the `smx-ai-vapt` authorization gate).

- Review BLE pairing method and link encryption, GATT characteristic permissions, and whether app-layer data is additionally encrypted/authenticated; assess NFC flows for relay resistance (timing/distance bounding, transaction binding).

## How to Fix

- Use LE Secure Connections with an authenticated pairing method (numeric comparison or passkey) where MITM matters, require encryption on sensitive characteristics, and add an app-layer encrypted/authenticated channel; do not rely on link security alone.
- For NFC, bind transactions to nonces and tight timing and require user presence/confirmation; do not trust proximity as authentication. Map to MASVS-NETWORK.

## References

- [NIST SP 800-121 Rev. 2: Guide to Bluetooth Security](https://csrc.nist.gov/pubs/sp/800/121/r2/upd1/final)
- [OWASP MASVS-NETWORK](https://mas.owasp.org/MASVS/08-MASVS-NETWORK/)
- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
