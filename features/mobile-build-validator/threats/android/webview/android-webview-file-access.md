---
id: android-webview-file-access
title: WebView File-Access Abuse
category: android-webview
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-STORAGE, MASTG-TEST-0252, MASTG-TEST-0253]
cwe: [CWE-200, CWE-22]
severity: high
last_verified: 2026-10-29
aliases: [universal-file-access, webview-file-url-access]
---

# WebView File-Access Abuse

## Why It Is a Threat

WebView settings such as `setAllowFileAccess`, `setAllowFileAccessFromFileURLs`, and `setAllowUniversalAccessFromFileURLs` let `file://` content read local app files and reach other origins, and `setAllowContentAccess` (on by default) exposes content providers. Loaded or injected content can then exfiltrate private files, cookies, and app data. File access is still on by default for apps targeting Android 10 or lower.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review WebView file and content access settings (MASTG-TEST-0252) and whether `file://` or untrusted content is loaded; at runtime, test whether loaded content can read app-private files (MASTG-TEST-0253).
- Source review by stack (`source-code-vapt`): Android `setAllowFileAccessFromFileURLs(true)`, `setAllowUniversalAccessFromFileURLs(true)`; React Native `react-native-webview` `allowFileAccess` and `allowUniversalAccessFromFileURLs`; Flutter `flutter_inappwebview` `allowFileAccessFromFileURLs`.

## How to Fix

- Call `setAllowFileAccess(false)` explicitly, never enable the deprecated file-URL cross-access settings, and disable content access unless needed.
- Load local assets through `WebViewAssetLoader` over an `https` origin instead of `file://` URLs.
- Never combine file access with untrusted content. Map to MASVS-PLATFORM.

## References

- [Android Developers: WebViews, unsafe file inclusion](https://developer.android.com/privacy-and-security/risks/webview-unsafe-file-inclusion)
- [Android Developers: Load in-app content (WebViewAssetLoader)](https://developer.android.com/develop/ui/views/layout/webapps/load-local-content)
- [MASTG-TEST-0252: References to Local File Access in WebViews](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0252/)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
