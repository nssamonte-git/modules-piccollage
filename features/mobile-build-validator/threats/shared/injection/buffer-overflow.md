---
id: buffer-overflow
title: Buffer Overflow
category: injection
platforms: [shared, backend, android, ios]
owasp_refs: [A05:2025, MASVS-CODE]
cwe: [CWE-120, CWE-787, CWE-121, CWE-122]
severity: high
last_verified: 2026-10-29
---

# Buffer Overflow

## Why It Is a Threat

In native or unmanaged code (C/C++, JNI/NDK libraries, Objective-C and unsafe Swift interop), copying or writing more data than a stack or heap buffer holds corrupts adjacent memory. The result ranges from crashes to control-flow hijack and remote code execution. Out-of-bounds write and classic, stack-based, and heap-based overflows all sit in the 2025 CWE Top 25. Native parsers, codecs, and SDKs bundled into mobile and backend apps are the usual entry points.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory native code that handles untrusted input (media, protocols, file formats, JNI bridges); review for unbounded copies (`strcpy`, `sprintf`, `memcpy` with attacker-controlled lengths).
- Fuzz lengths and malformed structures on an authorized build with sanitizers (ASan, or HWASan on arm64 Android), and check native dependencies against known CVEs.
- Confirm a crash/overflow signal only; do not develop weaponized exploits beyond demonstrating the gap.

## How to Fix

- Prefer memory-safe languages for new parsing code; otherwise use bounds-checked APIs and validate every length and index before copying (ASVS 5.0 V1.4 Memory, String, and Unmanaged Code).
- Compile with stack protectors, `_FORTIFY_SOURCE`, PIE/ASLR, and non-executable memory; on mobile, enable platform hardening such as Arm MTE on Android and Xcode's Enhanced Security (memory tagging, typed allocators) on Apple platforms.
- Keep native dependencies patched and fuzz continuously in CI. See also `memory-corruption`.

## References

- [CWE-120: Buffer Copy without Checking Size of Input ('Classic Buffer Overflow')](https://cwe.mitre.org/data/definitions/120.html)
- [CWE-787: Out-of-bounds Write](https://cwe.mitre.org/data/definitions/787.html)
- [OWASP Community: Buffer Overflow](https://owasp.org/www-community/vulnerabilities/Buffer_Overflow)
- [OWASP MASTG-KNOW-0005: Memory Corruption Bugs (Android)](https://mas.owasp.org/MASTG/knowledge/android/MASVS-CODE/MASTG-KNOW-0005/)
- [2025 CWE Top 25 Most Dangerous Software Weaknesses](https://cwe.mitre.org/top25/archive/2025/2025_cwe_top25.html)
