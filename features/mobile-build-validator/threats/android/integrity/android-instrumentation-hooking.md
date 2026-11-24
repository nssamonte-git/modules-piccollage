---
id: android-instrumentation-hooking
title: Instrumentation / Hooking
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0341, M7:2024]
cwe: [CWE-602, CWE-489]
severity: medium
last_verified: 2026-10-29
aliases: [frida-hooking, runtime-method-replacement, debugger-attachment]
---

# Instrumentation / Hooking

## Why It Is a Threat

Frameworks like Frida and Xposed-style modules hook and replace methods at runtime to bypass client checks (authentication gates, certificate pinning, root detection), read memory, and change logic. An attached debugger, which is easy when a release build is `debuggable`, does the same. Attackers use this to defeat client-side controls and extract secrets.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Check the release manifest for `android:debuggable="true"` (MASTG-TEST-0226).
- Demonstrate whether hooking or debugging can bypass client-side checks or expose sensitive state, and whether the app detects instrumentation (MASTG-TEST-0341). Collect evidence of the weakness; do not weaponize it.

## How to Fix

- Do not trust the client: enforce authorization, limits, and business rules on the server. Keep release builds non-debuggable.
- As defense-in-depth, add hook, debugger, and instrumentation detection and feed the result to server-side risk decisions (for example alongside Play Integrity verdicts) rather than acting only locally.
- Keep keys in the Android Keystore with user authentication required so hooked code cannot use them silently. Map to MASVS-RESILIENCE and M7:2024.

## References

- [MASTG-TEST-0341: Runtime Use of Hook Detection Techniques](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0341/)
- [MASTG-TEST-0226: Debuggable Flag Enabled in the AndroidManifest](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0226/)
- [OWASP Mobile Top 10 2024 M7: Insufficient Binary Protection](https://owasp.org/www-project-mobile-top-10/2023-risks/m7-insufficient-binary-protection)
- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
