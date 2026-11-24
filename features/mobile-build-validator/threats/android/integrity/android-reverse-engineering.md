---
id: android-reverse-engineering
title: APK Reverse Engineering
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0368, M7:2024]
cwe: [CWE-656]
severity: medium
last_verified: 2026-10-29
aliases: [apk-reverse-engineering]
---

# APK Reverse Engineering

## Why It Is a Threat

Attackers decompile the APK and its native libraries to understand logic, extract secrets and endpoints (`android-hardcoded-secrets`), find weaknesses, and prepare tampering or repackaging. Client code is fully visible to a determined attacker, so security through obscurity fails.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Decompile (`jadx`/`apktool`, plus a disassembler for `.so` files) and assess which sensitive logic, secrets, and endpoints are exposed, whether release code is obfuscated (MASTG-TEST-0368), and whether security depends on hidden client logic.

## How to Fix

- Assume the client is readable: keep secrets and trust decisions server-side.
- Enable R8 shrinking and obfuscation for release builds and strip debug symbols from native libraries to raise attacker effort; treat this as friction, not a control.
- Remove debug artifacts, verbose logging, and dead sensitive code. Map to MASVS-RESILIENCE and M7:2024.

## References

- [MASTG-TEST-0368: Insufficient Obfuscation of Security-Relevant Java/Kotlin Code](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0368/)
- [Android Developers: Enable app optimization with R8](https://developer.android.com/topic/performance/app-optimization/enable-app-optimization)
- [OWASP Mobile Top 10 2024 M7: Insufficient Binary Protection](https://owasp.org/www-project-mobile-top-10/2023-risks/m7-insufficient-binary-protection)
- [CWE-656: Reliance on Security Through Obscurity](https://cwe.mitre.org/data/definitions/656.html)
