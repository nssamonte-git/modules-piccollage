---
id: ios-app-attest-bypass
title: App Attest / DeviceCheck Bypass or Overreliance
category: ios-integrity
platforms: [ios]
owasp_refs: [MASVS-RESILIENCE, M7:2024]
cwe: [CWE-602, CWE-654]
severity: medium
last_verified: 2026-10-29
aliases: [app-attest-overreliance, devicecheck-misuse, app-attest-bypass]
---

# App Attest / DeviceCheck Bypass or Overreliance

## Why It Is a Threat

App Attest (`DCAppAttestService`) proves that requests come from a genuine copy of your app on Apple hardware. DeviceCheck stores two bits per device. Both give false assurance if the app checks the result itself, if the server does not issue a one-time challenge, if the server skips the assertion counter check, or if the result is treated as full authorization. A hooked app on one compromised device can then replay assertions or serve them to many cloned clients. If the server rejects devices that cannot use App Attest (unsupported devices, most app extensions), it locks out real users.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Confirm that the server, not the client, validates the attestation and each assertion:
  - the certificate chain up to Apple's App Attest root;
  - the App ID (team ID plus bundle ID) and the environment;
  - a fresh server challenge;
  - a counter that always increases.
- Test whether old or replayed assertions are accepted, how unsupported devices are handled, and whether the result alone authorizes high-risk actions.

## How to Fix

- Verify attestation and assertions only on the server, against a one-time challenge. Store the public key and counter per key, and reject a counter that does not increase.
- Store the attestation receipt and use Apple's fraud-risk metric to spot one device serving many instances.
- Treat the result as one risk signal: keep real authorization on the server, roll out gradually, and handle unsupported devices with a fallback. Map to MASVS-RESILIENCE-1 and MASVS-RESILIENCE-2. See `android-integrity-bypass`.

## References

- [Apple: Validating apps that connect to your server](https://developer.apple.com/documentation/devicecheck/validating-apps-that-connect-to-your-server)
- [Apple: Establishing your app's integrity](https://developer.apple.com/documentation/devicecheck/establishing-your-app-s-integrity)
- [MASVS-RESILIENCE: Resilience Against Reverse Engineering and Tampering](https://mas.owasp.org/MASVS/11-MASVS-RESILIENCE/)
- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
