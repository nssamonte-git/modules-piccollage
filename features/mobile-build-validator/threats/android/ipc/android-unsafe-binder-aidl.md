---
id: android-unsafe-binder-aidl
title: Unsafe Binder / AIDL IPC
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0365]
cwe: [CWE-862, CWE-749]
severity: medium
last_verified: 2026-10-29
aliases: [unsafe-binder-ipc, unsafe-aidl]
---

# Unsafe Binder / AIDL IPC

## Why It Is a Threat

Bound services that expose Binder, Messenger, or AIDL interfaces without verifying the caller let any app that can bind invoke privileged methods or read data. Binder interfaces are not declared in the manifest, so declarative permissions cannot protect individual methods; unvalidated parameters (parcelables, URIs, file descriptors) also reach injection or logic sinks.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review exported bound services (MASTG-TEST-0365) and their `android:permission`; check each AIDL method for caller checks and input validation; bind from an unprivileged app and invoke methods.

## How to Fix

- Keep services non-exported unless required; protect exported ones with a `signature`-level permission.
- In every sensitive method, verify the caller with `checkCallingPermission()`/`enforceCallingPermission()` or by checking `Binder.getCallingUid()` against expected packages and signatures. Avoid the `...OrSelf` variants for this, because they also pass on your own app's permissions.
- Validate all parameters and expose the fewest methods possible. Map to MASVS-PLATFORM.

## References

- [Android Developers: Security checklist, Binder and Messenger interfaces](https://developer.android.com/privacy-and-security/security-tips#binder-messenger)
- [Android Developers: Android Interface Definition Language (AIDL)](https://developer.android.com/develop/background-work/services/aidl)
- [MASTG-TEST-0365: Exported And Unprotected Services That Expose Sensitive Functionality](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0365/)
- [CWE-862: Missing Authorization](https://cwe.mitre.org/data/definitions/862.html)
