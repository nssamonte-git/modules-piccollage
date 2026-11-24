---
id: iot-firmware-compromise
title: Firmware Compromise
category: iot-devices
platforms: [shared, backend]
owasp_refs: [A08:2025, IoT-I4:2018]
cwe: [CWE-494, CWE-347, CWE-1277]
severity: high
last_verified: 2026-10-29
---

# Firmware Compromise

## Why It Is a Threat

Unsigned or unverified firmware updates, missing rollback protection, or extractable firmware let an attacker install malicious or older vulnerable firmware (persistent control, data theft) or reverse-engineer secrets/keys baked into the device — compromising the device and any system that trusts it. Devices that cannot be updated stay vulnerable. Update channel: `malicious-software-updates`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), on owner-authorized hardware.

- Review firmware update signing and on-device verification, secure boot, anti-rollback, and update transport security.
- Assess whether firmware images can be obtained or extracted and whether they contain hard-coded secrets or outdated components.

## How to Fix

- Sign firmware and verify it on-device against a hardware root of trust; use secure boot and anti-rollback; deliver updates over an authenticated channel (ETSI EN 303 645 provisions 5.3 and 5.7; NIST SP 800-193).
- Do not embed shared secrets in firmware; encrypt firmware where confidentiality matters, protect debug interfaces, and keep components patched with a published support period. Map to A08:2025; see `signature-verification-failure`.

## References

- [NIST SP 800-193: Platform Firmware Resiliency Guidelines](https://csrc.nist.gov/pubs/sp/800/193/final)
- [ETSI EN 303 645 V3.1.3: Cyber Security for Consumer Internet of Things](https://www.etsi.org/deliver/etsi_en/303600_303699/303645/03.01.03_60/en_303645v030103p.pdf)
- [OWASP Internet of Things Project (IoT Top 10 2018, I4 Lack of Secure Update Mechanism)](https://owasp.org/www-project-internet-of-things/)
- [CWE-494: Download of Code Without Integrity Check](https://cwe.mitre.org/data/definitions/494.html)
