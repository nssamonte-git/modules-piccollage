---
id: memory-corruption
title: Memory Corruption (OOB Access, Use-After-Free, Type Confusion)
category: injection
platforms: [shared, backend, android, ios]
owasp_refs: [A05:2025, MASVS-CODE]
cwe: [CWE-787, CWE-416, CWE-125, CWE-415, CWE-476, CWE-843]
severity: high
last_verified: 2026-11-18
aliases: [out-of-bounds-read, out-of-bounds-write, use-after-free, double-free, type-confusion, untrusted-native-library-input]
---

# Memory Corruption (OOB Access, Use-After-Free, Type Confusion)

## Why It Is a Threat

Out-of-bounds reads/writes, use-after-free, double-free, type confusion, and NULL-pointer dereferences in native code let an attacker leak memory, crash the process, or hijack control flow for code execution. Out-of-bounds write, use-after-free, out-of-bounds read, and NULL-pointer dereference are all in the 2025 CWE Top 25. Type confusion, where an object is used as an incompatible type, is a frequent exploited bug class in browser JavaScript engines, WebViews, and OS kernels. Often triggered by untrusted input to native libraries (codecs, parsers, JNI/NDK code, C/C++ SDKs) embedded in mobile and backend apps.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Fuzz native input paths under sanitizers on an authorized build (ASan/UBSan/MSan; HWASan on arm64 Android; Address Sanitizer and memory-tagging diagnostics in Xcode); review native-library provenance and known CVEs.
- Confirm a crash/corruption signal; do not weaponize beyond demonstrating the gap.
- Source review by stack (`source-code-vapt`): Rust `unsafe` blocks: `slice::from_raw_parts`, `transmute`, `set_len`, `get_unchecked`, and FFI pointers from input.

## How to Fix

- Prefer memory-safe languages (Rust, Swift, Kotlin/Java, Go) for new code that parses untrusted input; in C/C++, validate lengths, object lifetimes, and object types before casts (no unchecked downcasts or unions), and clear pointers after free (ASVS 5.0 V1.4 Memory, String, and Unmanaged Code).
- Enable hardening: stack protectors, `_FORTIFY_SOURCE`, PIE/ASLR, and hardware memory tagging where available (Arm MTE on supported Android devices; Xcode's Enhanced Security capability with hardware memory tagging and typed allocators on Apple platforms). Keep sanitizer and fuzzing jobs in CI.
- Patch or replace vulnerable native dependencies and SDKs, and keep embedded browser engines and WebViews updated, since type-confusion fixes ship there. See also `buffer-overflow`, `integer-overflow`.

## References

- [CWE-787: Out-of-bounds Write](https://cwe.mitre.org/data/definitions/787.html)
- [CWE-416: Use After Free](https://cwe.mitre.org/data/definitions/416.html)
- [Android Open Source Project: Memory safety](https://source.android.com/docs/security/test/memory-safety)
- [Apple Developer: Enabling enhanced security for your app](https://developer.apple.com/documentation/xcode/enabling-enhanced-security-for-your-app)
- [CISA: The Case for Memory Safe Roadmaps](https://www.cisa.gov/resources-tools/resources/case-memory-safe-roadmaps)
