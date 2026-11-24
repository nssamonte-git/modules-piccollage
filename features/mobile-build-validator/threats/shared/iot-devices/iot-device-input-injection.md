---
id: iot-device-input-injection
title: Command Injection Through Device Input
category: iot-devices
platforms: [shared, android, ios, backend]
owasp_refs: [A05:2025]
cwe: [CWE-77, CWE-20]
severity: high
last_verified: 2026-10-29
---

# Command Injection Through Device Input

## Why It Is a Threat

Data received from a device/peripheral (telemetry, device names, BLE characteristics, MQTT payloads, firmware-reported fields) is trusted and passed to a sink — shell, query, parser, template, or command interpreter — so a malicious or spoofed device injects commands (`os-command-injection`, `sql-injection`) into the app, gateway, or backend that processes its input.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trace device and peripheral input to sinks (commands, queries, parsers, dashboards that render device data).
- From a controllable test device, send boundary and benign injection test values and confirm they are rejected or neutralized.

## How to Fix

- Treat device input as untrusted: validate against an allowlist and expected structure (ASVS 5.0 V2.2; ETSI EN 303 645 provision 5.13), parameterize queries and avoid shells (ASVS 5.0 V1.2), and bound parsing; authenticate the device source.
- Isolate device-input processing with least privilege. See `os-command-injection`, `sql-injection`.

## References

- [OWASP Top 10:2025 A05 Injection](https://top10.owasp.org/2025/A05_2025-Injection/)
- [OWASP Input Validation Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html)
- [CWE-77: Improper Neutralization of Special Elements used in a Command](https://cwe.mitre.org/data/definitions/77.html)
- [ETSI EN 303 645 V3.1.3: Cyber Security for Consumer Internet of Things](https://www.etsi.org/deliver/etsi_en/303600_303699/303645/03.01.03_60/en_303645v030103p.pdf)
