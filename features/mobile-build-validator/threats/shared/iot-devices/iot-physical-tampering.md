---
id: iot-physical-tampering
title: Physical Tampering
category: iot-devices
platforms: [shared, backend]
owasp_refs: [A08:2025, IoT-I10:2018]
cwe: [CWE-1263, CWE-1191]
severity: medium
last_verified: 2026-10-29
---

# Physical Tampering

## Why It Is a Threat

With physical access, attackers extract secrets/keys, dump firmware or flash memory, attach to debug ports (JTAG/SWD/UART), or modify hardware — recovering credentials shared across devices and cloning/compromising units. Relevant where devices are deployed in untrusted locations.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on owner-authorized hardware.

- Review exposure of debug ports, external storage, and key storage, plus enclosure tamper resistance.
- Assess what a device-in-hand attacker can extract and whether one unit's secrets work on others.

## How to Fix

- Use hardware-backed key storage (secure element/TPM) with per-device keys; disable or lock debug interfaces in production (ETSI EN 303 645 provisions 5.4 and 5.6); encrypt local storage; add tamper detection and response.
- Never share one key across the fleet, and support revoking a tampered unit. Map to A08:2025; see `iot-firmware-compromise`.

## References

- [CWE-1191: On-Chip Debug and Test Interface With Improper Access Control](https://cwe.mitre.org/data/definitions/1191.html)
- [ETSI EN 303 645 V3.1.3: Cyber Security for Consumer Internet of Things](https://www.etsi.org/deliver/etsi_en/303600_303699/303645/03.01.03_60/en_303645v030103p.pdf)
- [OWASP Internet of Things Project (IoT Top 10 2018, I10 Lack of Physical Hardening)](https://owasp.org/www-project-internet-of-things/)
- [CWE-1263: Improper Physical Access Control](https://cwe.mitre.org/data/definitions/1263.html)
