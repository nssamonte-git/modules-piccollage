---
id: android-dynamic-code-loading
title: Unsafe Dynamic Code Loading
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-CODE, MASVS-RESILIENCE, A08:2025]
cwe: [CWE-494, CWE-829]
severity: high
last_verified: 2026-10-29
---

# Unsafe Dynamic Code Loading

## Why It Is a Threat

Loading code at runtime (`DexClassLoader`/`PathClassLoader`, `System.load` of downloaded `.so` files, plugins fetched from a server) lets anyone who can replace that code, such as a compromised server, an attacker-in-the-middle, or another app that can write the file location, run arbitrary code with your app's identity and permissions (`remote-code-execution`). Many forms of loading code from outside the APK also violate Google Play policy.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify dynamic loading APIs and trace where the loaded code comes from (network, external or shared storage, writable directories); check whether integrity is verified before loading and whether the source or path can be influenced.

## How to Fix

- Avoid dynamic code loading; ship the code in the APK (or use Play Feature Delivery).
- If it is unavoidable, load only from app-internal storage, fetch over TLS from your own infrastructure, verify a signature or a securely stored hash before loading, and mark the files read-only (required when targeting Android 14+). Never load code from external or shared storage.
- Map to MASVS-CODE, MASVS-RESILIENCE, and A08:2025.

## References

- [Android Developers: Dynamic Code Loading](https://developer.android.com/privacy-and-security/risks/dynamic-code-loading)
- [Android Developers: Behavior changes for apps targeting Android 14](https://developer.android.com/about/versions/14/behavior-changes-14)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [CWE-494: Download of Code Without Integrity Check](https://cwe.mitre.org/data/definitions/494.html)
