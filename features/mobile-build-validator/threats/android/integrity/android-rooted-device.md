---
id: android-rooted-device
title: Rooted-Device Compromise
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0325, M7:2024]
cwe: [CWE-250]
severity: medium
last_verified: 2026-10-29
aliases: [rooted-device-compromise]
---

# Rooted-Device Compromise

## Why It Is a Threat

On a rooted device the OS sandbox is weakened: privileged processes can read app storage, hook the runtime, and bypass local protections, so any device-local secret or control is at risk. Devices that no longer receive security updates carry similar risk. This matters most for high-value apps such as banking, payments, and health.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Assess which device-local protections the app relies on and how it behaves on a rooted device; review root detection (MASTG-TEST-0325) and whether high-risk actions are gated on server-side device signals.

## How to Fix

- Do not rely on device-local secrecy; keep secrets server-side and use hardware-backed Keystore keys that require user authentication.
- Use Play Integrity device labels on the server to adjust risk for sensitive actions. On Android 13+, `MEETS_STRONG_INTEGRITY` also requires a security update within the last year; on older versions check the SDK level as well. Treat local root signals as defense-in-depth, not the sole control.
- Map to MASVS-RESILIENCE and M7:2024.

## References

- [MASTG-TEST-0325: Runtime Use of Root Detection Techniques](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0325/)
- [Android Developers: Play Integrity integrity verdicts](https://developer.android.com/google/play/integrity/verdicts)
- [OWASP Mobile Top 10 2024 M7: Insufficient Binary Protection](https://owasp.org/www-project-mobile-top-10/2023-risks/m7-insufficient-binary-protection)
- [CWE-250: Execution with Unnecessary Privileges](https://cwe.mitre.org/data/definitions/250.html)
