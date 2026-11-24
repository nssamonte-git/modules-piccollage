---
id: iot-unencrypted-comms
title: Unencrypted Device Communication
category: iot-devices
platforms: [shared, android, ios, backend]
owasp_refs: [MASVS-NETWORK, A04:2025, IoT-I7:2018]
cwe: [CWE-319]
severity: high
last_verified: 2026-10-29
---

# Unencrypted Device Communication

## Why It Is a Threat

Device links (BLE, custom RF, local TCP, MQTT, Wi-Fi P2P) that send data/commands in cleartext or rely only on weak link-layer security let a nearby or on-path attacker read and tamper with traffic (`proximity-wireless-interception`, `attacker-in-the-middle`) — leaking data and forging commands.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Capture device traffic and check for cleartext data/commands.
- Check whether app-layer encryption and authentication are applied beyond link security, including for device-to-cloud protocols such as MQTT.

## How to Fix

- Apply an authenticated, encrypted app-layer channel over the device link (do not rely on BLE link security alone); use TLS for device-to-cloud traffic and modern crypto with per-session keys (ETSI EN 303 645 provision 5.5; ASVS 5.0 V12.3).
- Authenticate both endpoints. Map to MASVS-NETWORK; see `unencrypted-data-in-transit`.

## References

- [ETSI EN 303 645 V3.1.3: Cyber Security for Consumer Internet of Things](https://www.etsi.org/deliver/etsi_en/303600_303699/303645/03.01.03_60/en_303645v030103p.pdf)
- [Android Developers: Insecure machine-to-machine communication setup](https://developer.android.com/privacy-and-security/risks/insecure-machine-to-machine)
- [CWE-319: Cleartext Transmission of Sensitive Information](https://cwe.mitre.org/data/definitions/319.html)
- [OWASP Internet of Things Project (IoT Top 10 2018, I7 Insecure Data Transfer and Storage)](https://owasp.org/www-project-internet-of-things/)
