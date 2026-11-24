---
id: android-webview-untrusted-content
title: WebView Loading Untrusted Content
category: android-webview
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-CODE, MASTG-TEST-0398, MASTG-TEST-0399]
cwe: [CWE-79, CWE-601]
severity: high
last_verified: 2026-10-29
aliases: [javascript-execution-untrusted, unsafe-url-redirects-webview, webview-cookie-token-leakage]
---

# WebView Loading Untrusted Content

## Why It Is a Threat

A WebView that loads untrusted or remote URLs with JavaScript enabled runs attacker-controlled script, can be redirected to phishing or malicious pages, and may leak cookies and tokens, because all WebViews in an app share one cookie store by default. Weak URL checks (validating the host but not the scheme, or partial host matches) make this easy. Combined with `android-webview-javascript-interface` the impact is high.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review which URLs the WebView loads and how `shouldOverrideUrlLoading` validates navigation (MASTG-TEST-0398), whether Safe Browsing is disabled (MASTG-TEST-0399), and whether JavaScript is enabled for untrusted content; check cookie and token sharing across WebViews.
- Source review by stack (`source-code-vapt`): Android `loadUrl(` with intent or deep-link values and permissive `shouldOverrideUrlLoading`; React Native `react-native-webview` `originWhitelist={['*']}` and `injectedJavaScript` built from data.

## How to Fix

- Restrict navigation to an allowlist in `shouldOverrideUrlLoading`, parsing the URI and matching the full scheme and host exactly; keep JavaScript off for untrusted content and leave Safe Browsing on.
- Separate sensitive sessions from other web content (for example with androidx.webkit profiles, each with its own cookie manager) and clear sensitive cookies on logout.
- Open arbitrary external URLs in Custom Tabs or the browser instead. Map to MASVS-PLATFORM and MASVS-CODE; see `xss`.

## References

- [Android Developers: WebViews, unsafe URI loading](https://developer.android.com/privacy-and-security/risks/unsafe-uri-loading)
- [MASTG-TEST-0398: References to WebViewClient URL Loading Handlers](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0398/)
- [MASTG-TEST-0399: SafeBrowsing Disabled](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0399/)
- [Android Developers: Overview of Android Custom Tabs](https://developer.android.com/develop/ui/views/layout/webapps/overview-of-android-custom-tabs)
- [CWE-79: Improper Neutralization of Input During Web Page Generation ('Cross-site Scripting')](https://cwe.mitre.org/data/definitions/79.html)
