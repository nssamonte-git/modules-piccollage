---
id: ios-runtime-hooking
title: Runtime Hooking / Instrumentation
category: ios-integrity
platforms: [ios]
owasp_refs: [MASVS-RESILIENCE, MASTG-TEST-0354, M7:2024]
cwe: [CWE-602, CWE-489]
severity: medium
last_verified: 2026-10-29
aliases: [dynamic-instrumentation, method-swizzling-malicious, debugger-attachment-ios, frida-ios]
---

# Runtime Hooking / Instrumentation

## Why It Is a Threat

Instrumentation frameworks such as Frida (and objection, which is built on it) and malicious method swizzling hook and replace Objective-C and Swift functions at runtime. Attackers use them to bypass checks (authentication, certificate pinning, jailbreak detection), read memory, and change logic. An attached debugger can do the same. These techniques defeat client-side controls and extract secrets.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Show whether hooking or debugging can bypass client-side checks or read sensitive state. Collect evidence of the weakness; do not build a weapon.
- Review hook detection (MASTG-TEST-0354) and debugger detection (MASTG-TEST-0401).

## How to Fix

- Do not trust the client: enforce security on the server.
- As defense-in-depth, add hook and debugger detection, and ship release builds without `get-task-allow`.
- Keep keys in the Secure Enclave with user-authentication requirements, so hooks cannot extract the key material. They may still try to trigger operations, which the server must verify.
- Map to MASVS-RESILIENCE-4.

## References

- [MASTG-TEST-0354: Runtime Use of Hook Detection Techniques](https://mas.owasp.org/MASTG/tests/ios/MASVS-RESILIENCE/MASTG-TEST-0354/)
- [MASTG-TEST-0401: References to Debugging Detection APIs](https://mas.owasp.org/MASTG/tests/ios/MASVS-RESILIENCE/MASTG-TEST-0401/)
- [MASVS-RESILIENCE: Resilience Against Reverse Engineering and Tampering](https://mas.owasp.org/MASVS/11-MASVS-RESILIENCE/)
- [CWE-602: Client-Side Enforcement of Server-Side Security](https://cwe.mitre.org/data/definitions/602.html)
