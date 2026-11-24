---
id: iot-device-impersonation
title: Device Impersonation
category: iot-devices
platforms: [shared, android, ios, backend]
owasp_refs: [MASVS-NETWORK, A07:2025]
cwe: [CWE-290, CWE-345]
severity: high
last_verified: 2026-10-29
---

# Device Impersonation

## Why It Is a Threat

An attacker clones a device's identifier (MAC/serial/advertised name, BLE address) or spoofs a peripheral/gateway, so the app or backend trusts a rogue device — feeding false data, intercepting commands, or gaining access granted to the real device. Shared fleet-wide credentials or keys extracted from one unit let an attacker impersonate every device.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on owner-authorized devices.

- Check whether device identity is verified cryptographically or only by spoofable identifiers, and whether credentials are unique per device.
- With a test device, present a copied identifier and observe whether the app or backend accepts it.

## How to Fix

- Authenticate devices with unique per-device cryptographic credentials or certificates held in hardware-backed storage (mutual TLS or an equivalent challenge-response), not MAC/name; verify identity server-side and bind devices to accounts (ASVS 5.0 V12.1.3 mTLS validation; ETSI EN 303 645 provisions 5.4 and 5.5).
- Revoke credentials of compromised devices and detect duplicate or anomalous device identities. Map to MASVS-NETWORK.

## References

- [NIST IR 8259A: IoT Device Cybersecurity Capability Core Baseline (device identification)](https://csrc.nist.gov/pubs/ir/8259/a/final)
- [ETSI EN 303 645 V3.1.3: Cyber Security for Consumer Internet of Things](https://www.etsi.org/deliver/etsi_en/303600_303699/303645/03.01.03_60/en_303645v030103p.pdf)
- [CWE-290: Authentication Bypass by Spoofing](https://cwe.mitre.org/data/definitions/290.html)
- [Android Developers: Insecure machine-to-machine communication setup](https://developer.android.com/privacy-and-security/risks/insecure-machine-to-machine)
