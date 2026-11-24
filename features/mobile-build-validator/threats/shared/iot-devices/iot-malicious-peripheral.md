---
id: iot-malicious-peripheral
title: Malicious Peripheral
category: iot-devices
platforms: [shared, android, ios, backend]
owasp_refs: [MASVS-PLATFORM, MASVS-CODE]
cwe: [CWE-20, CWE-940]
severity: medium
last_verified: 2026-10-29
---

# Malicious Peripheral

## Why It Is a Threat

A rogue peripheral (BLE/USB/NFC tag/accessory) sends crafted or malformed data to the app, exploiting parsing bugs, injecting commands (`iot-device-input-injection`), or abusing trust granted to connected devices — turning the device channel into an attack surface. Apps often assume any device that connects or advertises the expected service is genuine.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on owner-authorized devices.

- Connect a controllable test peripheral and send malformed or out-of-range payloads; assess input validation and parser robustness on the app and backend side.
- Check whether the app verifies which device it is talking to before acting on its data.

## How to Fix

- Treat all peripheral input as untrusted: validate, bound, and fuzz-test parsers (MASVS-CODE-4); least-privilege what a connected device can trigger; authenticate the device and the channel before trusting data.
- Isolate peripheral parsing. Map to MASVS-PLATFORM; see `malicious-document-image-parsing`.

## References

- [Android Developers: Insecure machine-to-machine communication setup](https://developer.android.com/privacy-and-security/risks/insecure-machine-to-machine)
- [OWASP MASVS-CODE](https://mas.owasp.org/MASVS/10-MASVS-CODE/)
- [CWE-20: Improper Input Validation](https://cwe.mitre.org/data/definitions/20.html)
- [CWE-940: Improper Verification of Source of a Communication Channel](https://cwe.mitre.org/data/definitions/940.html)
