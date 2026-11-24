---
id: biometric-fallback-abuse
title: Biometric Fallback Abuse
category: auth
platforms: [shared, ios, android]
owasp_refs: [MASVS-AUTH, A07:2025]
cwe: [CWE-288, CWE-602, CWE-287]
severity: medium
last_verified: 2026-10-29
---

# Biometric Fallback Abuse

## Why It Is a Threat

Biometric gates fall back to a weak device PIN or an app-side flag, or are enforced only by an event callback in the UI, so an attacker with the unlocked device or a hooked/tampered client bypasses the biometric check. Biometrics gate local access, not server authorization (`ios-keychain-accessibility`).

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Test fallback paths (device credential, cancel, tamper) and whether protected data/actions are still gated; confirm sensitive items are unlocked by a keystore/Keychain key that requires biometrics, not by a boolean result.
- Check whether keys survive a new biometric enrollment.

## How to Fix

- Android: use `BiometricPrompt` with a `CryptoObject` and a Keystore key that requires strong biometric auth (`setUserAuthenticationParameters`, `setInvalidatedByBiometricEnrollment(true)`); omit `DEVICE_CREDENTIAL` for sensitive actions. iOS: protect Keychain items with `SecAccessControl` `.biometryCurrentSet` rather than relying on `LAContext.evaluatePolicy` alone.
- Keep server-side authorization independent of local biometric success; treat biometrics as one factor alongside something the user has. Map to MASVS-AUTH-2 and ASVS 5.0 V6 Authentication (6.5.7).

## References

- [OWASP MASWE-0021: Fallback to Non-biometric Credentials Allowed for Sensitive Transactions](https://mas.owasp.org/MASWE/MASVS-AUTH/MASWE-0021/)
- [OWASP MASWE-0020: Local Authentication Can Be Bypassed](https://mas.owasp.org/MASWE/MASVS-AUTH/MASWE-0020/)
- [Android Developers: Show a biometric authentication dialog](https://developer.android.com/identity/sign-in/biometric-auth)
- [Apple Developer: Accessing Keychain Items with Face ID or Touch ID](https://developer.apple.com/documentation/localauthentication/accessing-keychain-items-with-face-id-or-touch-id)
