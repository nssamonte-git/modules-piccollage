---
id: android-webview-javascript-interface
title: Unsafe WebView JavaScript Interface
category: android-webview
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0334]
cwe: [CWE-749, CWE-79]
severity: high
last_verified: 2026-10-29
aliases: [addjavascriptinterface-abuse, unsafe-javascript-interface]
---

# Unsafe WebView JavaScript Interface

## Why It Is a Threat

`addJavascriptInterface` exposes native methods to JavaScript in every frame of the WebView, including iframes, with no way to check the calling origin. If the WebView loads untrusted or remote content, or is XSS-able, that script can call the bridge to reach native capabilities: data theft and, for apps targeting API 16 or lower, code execution. Message-channel bridges without origin checks carry the same risk. The bridge is only as safe as the content loaded.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review `addJavascriptInterface` and message-channel usage, the exposed methods (MASTG-TEST-0334), and whether the WebView ever loads untrusted, remote, or mixed content.
- Source review by stack (`source-code-vapt`): Android `addJavascriptInterface(` on pages that are not fully trusted, including WebViews created in Jetpack Compose `AndroidView`; Flutter `webview_flutter` `JavaScriptMode.unrestricted` with `addJavaScriptChannel`.

## How to Fix

- Prefer `WebViewCompat.addWebMessageListener` with a strict allowed-origin list over `addJavascriptInterface`.
- If a JavaScript interface is needed, use it only for trusted content, remove it (`removeJavascriptInterface`) before loading anything else, expose minimal `@JavascriptInterface` methods, and validate their arguments.
- Keep JavaScript disabled when it is not needed. Map to MASVS-PLATFORM.

## References

- [Android Developers: WebView native bridges](https://developer.android.com/privacy-and-security/risks/insecure-webview-native-bridges)
- [MASTG-TEST-0334: Native Code Exposed Through WebViews](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0334/)
- [CWE-749: Exposed Dangerous Method or Function](https://cwe.mitre.org/data/definitions/749.html)
