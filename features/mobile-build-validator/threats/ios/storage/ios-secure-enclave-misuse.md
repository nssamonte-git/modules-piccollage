---
id: ios-secure-enclave-misuse
title: Weak Secure Enclave / Key Usage
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-CRYPTO, MASVS-AUTH, M10:2024]
cwe: [CWE-522, CWE-922]
severity: medium
last_verified: 2026-10-29
---

# Weak Secure Enclave / Key Usage

## Why It Is a Threat

Sensitive keys (device binding, signing, key agreement) are sometimes generated as software keys instead of in the Secure Enclave. Others are created without access control: no `SecAccessControl` requiring biometrics or the passcode, or no `ThisDeviceOnly` accessibility. Such keys can be used without the user present, or extracted and moved to another device, which undermines the protection they are meant to give.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review key generation:
  - Secure Enclave use (`kSecAttrTokenIDSecureEnclave` or CryptoKit `SecureEnclave.P256`);
  - the `SecAccessControl` flags (`.privateKeyUsage` plus `.biometryCurrentSet` or `.userPresence`);
  - `ThisDeviceOnly` accessibility.
- Check whether the server ties the key to the user and verifies its signatures.

## How to Fix

- Generate sensitive asymmetric keys in the Secure Enclave. It supports only NIST P-256 keys, and the private key never leaves it.
- Protect the keys with `SecAccessControl` requiring user presence or `.biometryCurrentSet`, so a biometric enrollment change invalidates them. Use `ThisDeviceOnly` accessibility.
- For symmetric data keys, encrypt them with a Secure Enclave key or store them in the Keychain with strict access control.
- Keep server authorization independent of local authentication. Map to MASVS-CRYPTO-2 and MASVS-AUTH-2. See `ios-keychain-accessibility` and `ios-localauthentication-misuse`.

## References

- [Apple: Protecting keys with the Secure Enclave](https://developer.apple.com/documentation/security/protecting-keys-with-the-secure-enclave)
- [Apple Platform Security: The Secure Enclave](https://support.apple.com/guide/security/the-secure-enclave-sec59b0b31ff/web)
- [MASVS-CRYPTO: Cryptography](https://mas.owasp.org/MASVS/06-MASVS-CRYPTO/)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
