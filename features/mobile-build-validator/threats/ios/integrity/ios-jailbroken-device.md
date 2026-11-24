---
id: ios-jailbroken-device
title: Jailbroken-Device Compromise
category: ios-integrity
platforms: [ios]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0240, MASTG-TEST-0241]
cwe: [CWE-602]
severity: medium
last_verified: 2026-10-29
aliases: [jailbroken-device-compromise]
---

# Jailbroken-Device Compromise

## Why It Is a Threat

On a jailbroken device, the iOS sandbox and code-signing enforcement are weakened. Tools can read app containers and unlocked Keychain items, hook the runtime, and bypass local checks. Any secret or control that lives only on the device is at risk. This matters most for high-value apps (finance, health, enterprise).

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Find out which protections the app relies on that only exist on the device.
- Check the app's jailbreak detection, both in code (MASTG-TEST-0240) and at runtime (MASTG-TEST-0241), and what it does when detection fires for high-risk actions.

## How to Fix

- Do not rely on secrets staying hidden on the device. Keep secrets and trust decisions on the server, and use Secure Enclave keys that require user authentication.
- Use jailbreak detection and App Attest results to raise risk or step up authentication for sensitive actions. Treat them as defense-in-depth, never as the only control, because local detection can be bypassed.
- Map to MASVS-RESILIENCE-1.

## References

- [MASTG-TEST-0240: Jailbreak Detection in Code](https://mas.owasp.org/MASTG/tests/ios/MASVS-RESILIENCE/MASTG-TEST-0240/)
- [MASTG-TEST-0241: Runtime Use of Jailbreak Detection Techniques](https://mas.owasp.org/MASTG/tests/ios/MASVS-RESILIENCE/MASTG-TEST-0241/)
- [MASVS-RESILIENCE: Resilience Against Reverse Engineering and Tampering](https://mas.owasp.org/MASVS/11-MASVS-RESILIENCE/)
- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
