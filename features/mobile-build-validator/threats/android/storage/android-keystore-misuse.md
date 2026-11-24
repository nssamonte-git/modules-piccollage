---
id: android-keystore-misuse
title: Improper Android Keystore Use
category: android-storage
platforms: [android]
owasp_refs: [MASVS-CRYPTO, MASVS-AUTH, MASTG-TEST-0328, M10:2024]
cwe: [CWE-922, CWE-522]
severity: high
last_verified: 2026-10-29
aliases: [weak-key-accessibility-android, weak-key-authentication-android, deprecated-encryptedsharedprefs]
---

# Improper Android Keystore Use

## Why It Is a Threat

Sensitive keys are kept outside the Android Keystore, or configured with weak access controls: usable without user authentication, valid for long periods after one unlock, not hardware-backed, used for several purposes, or not invalidated when biometric enrollment changes. Malware or someone holding the device can then use the keys. Jetpack deprecated `androidx.security:security-crypto` (including `EncryptedSharedPreferences`) in 2025 in favor of platform APIs and direct Keystore use, so teams must configure Keystore keys themselves.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Review `KeyGenParameterSpec`: hardware backing (TEE or StrongBox), `setUserAuthenticationRequired` and authentication validity, `setInvalidatedByBiometricEnrollment` (MASTG-TEST-0328), key purposes, and long-validity biometric keys (MASTG-TEST-0330); check for deprecated crypto libraries.

## How to Fix

- Generate non-exportable keys in the Keystore, using StrongBox where it is needed and available (fall back on `StrongBoxUnavailableException`). Use one purpose per key.
- Require user authentication for sensitive keys with per-use or short validity, bind biometric prompts to a `CryptoObject`, and invalidate keys on enrollment change. Use key attestation when the server must trust the key, and keep server authorization independent of local authentication.
- Migrate off `security-crypto` to Keystore-backed encryption. Map to MASVS-CRYPTO and M10:2024.

## References

- [Android Developers: Android Keystore system](https://developer.android.com/privacy-and-security/keystore)
- [Android Developers: Jetpack Security release notes (security-crypto deprecation)](https://developer.android.com/jetpack/androidx/releases/security)
- [MASTG-TEST-0328: References to APIs Detecting Biometric Enrollment Changes](https://mas.owasp.org/MASTG/tests/android/MASVS-AUTH/MASTG-TEST-0328/)
- [CWE-922: Insecure Storage of Sensitive Information](https://cwe.mitre.org/data/definitions/922.html)
