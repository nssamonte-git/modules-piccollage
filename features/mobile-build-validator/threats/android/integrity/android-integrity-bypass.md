---
id: android-integrity-bypass
title: Integrity / Play-Integrity Bypass
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-RESILIENCE, M7:2024]
cwe: [CWE-602, CWE-654]
severity: medium
last_verified: 2026-10-29
aliases: [integrity-check-bypass, play-integrity-bypass, emulator-based-abuse, play-integrity-overreliance]
---

# Integrity / Play-Integrity Bypass

## Why It Is a Threat

Client-side integrity, root, and emulator checks can be hooked out (`android-instrumentation-hooking`). Even Play Integrity gives false assurance if the verdict is evaluated on the device, is not bound to the specific request, or is the only gate; treating an unavailable verdict as a pass has the same effect (`fail-open-security-controls`). Emulator farms and modified devices automate abuse at scale.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check that integrity tokens are decrypted and verified on the backend, that `requestPackageName` and the request binding (`requestHash` for standard requests, `nonce` for classic requests) are compared with what the server expects, and how missing, stale, or failed verdicts are handled.
- Check whether local root and emulator checks can be bypassed with hooking or an emulator (MASTG-TEST-0351).

## How to Fix

- Send the token to your backend, decode it there, and check request binding, package name, app recognition, and device-integrity labels before acting. Standard requests get Google's replay protection; for classic requests use a server-generated, single-use nonce.
- Decide on the server with tiered responses (step-up, limits, deny) and a defined policy for when verdicts are unavailable. Treat client checks as defense-in-depth, never the sole gate.
- Enforce real security server-side. Map to MASVS-RESILIENCE and M7:2024.

## References

- [Android Developers: Make a standard Play Integrity API request](https://developer.android.com/google/play/integrity/standard)
- [Android Developers: Play Integrity integrity verdicts](https://developer.android.com/google/play/integrity/verdicts)
- [MASTG-TEST-0351: Runtime Use of Emulator Detection Techniques](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0351/)
- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
