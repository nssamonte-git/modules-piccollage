---
id: ios-reverse-engineering
title: IPA Reverse Engineering
category: ios-integrity
platforms: [ios]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0391, M7:2024]
cwe: [CWE-656]
severity: medium
last_verified: 2026-10-29
aliases: [ipa-reverse-engineering]
---

# IPA Reverse Engineering

## Why It Is a Threat

App Store binaries are encrypted, but they are easy to decrypt on a jailbroken device. Attackers then analyze the binary to understand its logic, pull out secrets and endpoints (`ios-hardcoded-secrets`), find weaknesses, and prepare tampering. Client code, symbols, and strings can all be recovered, so security through obscurity fails.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inspect the decrypted binary (`otool`, `nm`, `strings`, `class-dump` for Objective-C metadata, Swift demangling, a disassembler or decompiler).
- Find out which sensitive logic, secrets, or endpoints are exposed. Check whether debug symbols remain (MASTG-TEST-0219), whether security-relevant code is obfuscated (MASTG-TEST-0391), and whether security depends on hidden client logic.

## How to Fix

- Assume anyone can read the client. Keep secrets and trust decisions on the server. Obfuscation raises the attacker's effort but gives no assurance.
- Strip symbols from release builds, remove debug logging and dead sensitive code, and obfuscate the logic most likely to be targeted.
- Map to MASVS-RESILIENCE-3.

## References

- [MASTG-TEST-0391: Insufficient Obfuscation of Security-Relevant Native Code](https://mas.owasp.org/MASTG/tests/ios/MASVS-RESILIENCE/MASTG-TEST-0391/)
- [MASTG-TEST-0219: Testing for Debugging Symbols](https://mas.owasp.org/MASTG/tests/ios/MASVS-RESILIENCE/MASTG-TEST-0219/)
- [OWASP Mobile Top 10 2024 M7: Insufficient Binary Protection](https://owasp.org/www-project-mobile-top-10/2023-risks/m7-insufficient-binary-protection)
- [CWE-656: Reliance on Security Through Obscurity](https://cwe.mitre.org/data/definitions/656.html)
