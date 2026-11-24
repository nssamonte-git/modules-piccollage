---
id: android-hardcoded-secrets
title: Hard-Coded Secrets in APKs
category: android-storage
platforms: [android]
owasp_refs: [MASVS-CRYPTO, MASVS-STORAGE, MASTG-TEST-0212, M1:2024]
cwe: [CWE-798, CWE-321]
severity: high
last_verified: 2026-10-29
aliases: [secrets-in-apk, hardcoded-keys-android]
---

# Hard-Coded Secrets in APKs

## Why It Is a Threat

API keys, credentials, and cryptographic keys compiled into the APK (code, resources, `BuildConfig`, assets, native libraries) are easily extracted by decompiling the app; client binaries are not a secret store. Related: `secrets-leakage`, `hardcoded-encryption-keys`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Decompile the APK (`jadx`/`apktool`) and search code, resources, assets, and native libraries for keys, credentials, and endpoints, including hardcoded cryptographic keys (MASTG-TEST-0212); check whether found keys are live and what they grant.
- Source review by stack (`source-code-vapt`): Android `BuildConfig` fields and `strings.xml`; React Native JS bundle values (`react-native-config`, `EXPO_PUBLIC_*`); Flutter `flutter_dotenv` assets and `--dart-define` values.

## How to Fix

- Keep secrets server-side and have the app obtain short-lived, scoped tokens at runtime; never embed long-lived credentials or private keys in the client.
- Generate device-local keys inside the Android Keystore instead of shipping them. Restrict any key that must ship (for example by app signature and API scope), rotate exposed keys, and add secret scanning to CI.
- Obfuscation is not protection. Map to MASVS-CRYPTO and M1:2024.

## References

- [Android Developers: Hardcoded cryptographic secrets](https://developer.android.com/privacy-and-security/risks/hardcoded-cryptographic-secrets)
- [MASTG-TEST-0212: Use of Hardcoded Cryptographic Keys in Code](https://mas.owasp.org/MASTG/tests/android/MASVS-CRYPTO/MASTG-TEST-0212/)
- [OWASP Mobile Top 10 2024 M1: Improper Credential Usage](https://owasp.org/www-project-mobile-top-10/2023-risks/m1-improper-credential-usage)
- [CWE-798: Use of Hard-coded Credentials](https://cwe.mitre.org/data/definitions/798.html)
