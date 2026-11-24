---
id: incorrect-certificate-validation
title: Incorrect Certificate Validation
category: crypto
platforms: [shared, web, backend, android, ios]
owasp_refs: [A07:2025, A04:2025, MASVS-NETWORK]
cwe: [CWE-295, CWE-297]
severity: high
last_verified: 2026-10-29
aliases: [trusting-all-certificates, disabled-hostname-verification, expired-certificate-handling]
---

# Incorrect Certificate Validation

## Why It Is a Threat

The client accepts invalid TLS certificates — trusting all certs, skipping hostname verification, ignoring expiry, or using a permissive trust manager — enabling attacker-in-the-middle interception of "encrypted" traffic. Common in mobile and internal service clients.

## How to Find or Test

Authorized testing only, on an owner-authorized environment (follow the `smx-ai-vapt` authorization gate).

- Present an untrusted/mismatched/expired certificate via a test proxy and confirm whether the client rejects it.
- Review code and config for disabled validation: accept-all `X509TrustManager`/`HostnameVerifier`, WebView `onReceivedSslError` that proceeds (`android-webview-tls-bypass`), user CAs trusted in release builds, iOS trust-evaluation delegates that accept any server trust, and flags such as `verify=False`, `rejectUnauthorized: false`, or `InsecureSkipVerify`.
- Source review by stack (`source-code-vapt`): Android empty `checkServerTrusted` or `HostnameVerifier` returning true; iOS `URLCredential(trust:)` without `SecTrustEvaluateWithError`; Flutter `badCertificateCallback` returning true; React Native native configs that disable TLS checks; Python `requests` or `httpx` with `verify=False`; Rust reqwest `danger_accept_invalid_certs(true)` and rustls `dangerous()` verifiers.

## How to Fix

- Use the platform's default, strict certificate and hostname validation (Android Network Security Configuration, iOS App Transport Security); never disable it for convenience or debug builds that ship.
- For internal services, trust only the specific internal CA and validate mTLS client certificates. Pin only for high-risk clients, with backup pins and a rotation plan. Map to ASVS 5.0 V12 Secure Communication (12.1.3, 12.3.2, 12.3.4) and MASVS-NETWORK-1.

## References

- [CWE-295: Improper Certificate Validation](https://cwe.mitre.org/data/definitions/295.html)
- [OWASP MASVS-NETWORK](https://mas.owasp.org/MASVS/08-MASVS-NETWORK/)
- [Android: Security with network protocols](https://developer.android.com/privacy-and-security/security-ssl)
- [OWASP Pinning Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Pinning_Cheat_Sheet.html)
