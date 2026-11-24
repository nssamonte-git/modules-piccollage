---
id: iot-unauthorized-pairing
title: Unauthorized / Weak Pairing
category: iot-devices
platforms: [shared, android, ios, backend]
owasp_refs: [MASVS-NETWORK, IoT-I1:2018]
cwe: [CWE-287, CWE-1391]
severity: high
last_verified: 2026-10-29
aliases: [weak-pairing-codes]
---

# Unauthorized / Weak Pairing

## Why It Is a Threat

Devices that pair without authentication, use "Just Works" or legacy BLE pairing (no MITM protection), accept static, default, or guessable pairing codes, stay discoverable or in pairing mode indefinitely, or never expire pairings let an attacker pair their own device or app and gain control or data access — the entry point for most BLE/NFC device attacks.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on owner-authorized devices.

- Review the pairing/bonding method and code strength, discoverability window, and server-side ownership binding.
- Attempt to pair a test device without authorization and check whether MITM-protected pairing (LE Secure Connections with numeric comparison or passkey entry) is enforced.

## How to Fix

- Require authenticated pairing (LE Secure Connections with numeric comparison, passkey, or out-of-band), unique non-guessable codes with no universal defaults (ETSI EN 303 645 provision 5.1), user confirmation, a limited pairing window, and pairing expiry; bind devices to accounts server-side.
- Add an app-layer authenticated channel. Map to MASVS-NETWORK; see `proximity-wireless-interception`.

## References

- [NIST SP 800-121 Rev. 2: Guide to Bluetooth Security](https://csrc.nist.gov/pubs/sp/800/121/r2/upd1/final)
- [ETSI EN 303 645 V3.1.3: Cyber Security for Consumer Internet of Things](https://www.etsi.org/deliver/etsi_en/303600_303699/303645/03.01.03_60/en_303645v030103p.pdf)
- [CWE-1391: Use of Weak Credentials](https://cwe.mitre.org/data/definitions/1391.html)
- [OWASP Internet of Things Project (IoT Top 10 2018, I1 Weak, Guessable, or Hardcoded Passwords)](https://owasp.org/www-project-internet-of-things/)
