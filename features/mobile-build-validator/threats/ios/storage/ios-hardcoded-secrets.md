---
id: ios-hardcoded-secrets
title: Hard-Coded Secrets in IPA Binaries
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-CRYPTO, MASVS-STORAGE, MASTG-TEST-0213, M1:2024]
cwe: [CWE-798, CWE-321]
severity: high
last_verified: 2026-10-29
aliases: [secrets-in-ipa, hardcoded-keys-ios]
---

# Hard-Coded Secrets in IPA Binaries

## Why It Is a Threat

API keys, credentials, and cryptographic keys built into the IPA (binary strings, Info.plist, `.xcconfig`-generated constants, bundled JSON or plist resources, third-party SDK configs) can be pulled out by unpacking the app. A client binary is not a secret store. Related: `secrets-leakage`, `hardcoded-encryption-keys`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Unpack the IPA and inspect the binary (`strings`, a disassembler), Info.plist, and bundled resources for keys, secrets, and endpoints.
- Look specifically for hardcoded cryptographic keys in code (MASTG-TEST-0213) and in files (MASTG-TEST-0214).
- Confirm the scope of any key you find without abusing it.
- Source review by stack (`source-code-vapt`): iOS keys in Swift constants, `Info.plist`, or `.xcconfig`; React Native values in the JS bundle (`react-native-config`, `EXPO_PUBLIC_*`); Flutter `flutter_dotenv` assets and `--dart-define` values.

## How to Fix

- Keep secrets on the server. Have the app fetch short-lived, narrowly scoped tokens at runtime after user or App Attest verification. Never embed long-lived keys or credentials in the client.
- Generate device-local keys at runtime in the Keychain or Secure Enclave instead of shipping them.
- Rotate any secret that has shipped, and use restricted, public-by-design keys for third-party SDKs. Map to MASVS-CRYPTO-2 and M1:2024.

## References

- [MASTG-TEST-0213: Use of Hardcoded Cryptographic Keys in Code](https://mas.owasp.org/MASTG/tests/ios/MASVS-CRYPTO/MASTG-TEST-0213/)
- [MASTG-TEST-0214: Hardcoded Cryptographic Keys in Files](https://mas.owasp.org/MASTG/tests/ios/MASVS-CRYPTO/MASTG-TEST-0214/)
- [OWASP Mobile Top 10 2024 M1: Improper Credential Usage](https://owasp.org/www-project-mobile-top-10/2023-risks/m1-improper-credential-usage)
- [CWE-798: Use of Hard-coded Credentials](https://cwe.mitre.org/data/definitions/798.html)
