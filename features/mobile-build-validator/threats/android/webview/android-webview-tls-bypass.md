---
id: android-webview-tls-bypass
title: WebView TLS / Mixed-Content Bypass
category: android-webview
platforms: [android]
owasp_refs: [MASVS-NETWORK, MASTG-TEST-0284, M5:2024]
cwe: [CWE-295, CWE-319]
severity: high
last_verified: 2026-10-29
aliases: [webview-certificate-error-bypass, mixed-content-webview]
---

# WebView TLS / Mixed-Content Bypass

## Why It Is a Threat

Overriding `onReceivedSslError` to call `handler.proceed()` makes the WebView accept invalid certificates, and `MIXED_CONTENT_ALWAYS_ALLOW` (or compatibility mode) loads HTTP resources into HTTPS pages. Both enable attacker-in-the-middle interception and script injection in WebView traffic (`incorrect-certificate-validation`, `attacker-in-the-middle`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review `onReceivedSslError` handling (MASTG-TEST-0284) and `setMixedContentMode`; present an invalid certificate through a test proxy and confirm whether the WebView proceeds.

## How to Fix

- Never call `handler.proceed()` on SSL errors; cancel the load and show an error.
- Keep `MIXED_CONTENT_NEVER_ALLOW` (the default for apps targeting Android 5.0+) and do not lower it.
- Disable cleartext traffic in the Network Security Configuration, and confirm in testing that any pinning claim actually covers WebView traffic. Map to MASVS-NETWORK and M5:2024.

## References

- [MASTG-TEST-0284: Incorrect SSL Error Handling in WebViews](https://mas.owasp.org/MASTG/tests/android/MASVS-NETWORK/MASTG-TEST-0284/)
- [Android Developers: WebSettings (setMixedContentMode)](https://developer.android.com/reference/android/webkit/WebSettings)
- [OWASP Mobile Top 10 2024 M5: Insecure Communication](https://owasp.org/www-project-mobile-top-10/2023-risks/m5-insecure-communication)
- [CWE-295: Improper Certificate Validation](https://cwe.mitre.org/data/definitions/295.html)
