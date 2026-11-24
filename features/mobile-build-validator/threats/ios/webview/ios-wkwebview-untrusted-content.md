---
id: ios-wkwebview-untrusted-content
title: Unsafe WKWebView Content
category: ios-webview
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, MASVS-NETWORK, MASTG-TEST-0332]
cwe: [CWE-79, CWE-601]
severity: high
last_verified: 2026-10-29
aliases: [mixed-content-wkwebview, weak-navigation-policy, wkwebview-cookie-leakage]
---

# Unsafe WKWebView Content

## Why It Is a Threat

A WKWebView that loads untrusted, remote, or attacker-controlled URLs runs attacker-controlled script. Other weaknesses add to the risk:

- navigation that `decidePolicyFor` does not check can be redirected to malicious pages;
- the ATS exception `NSAllowsArbitraryLoadsInWebContent` allows cleartext and mixed content;
- a `didReceive` authentication-challenge handler that skips certificate checks allows interception;
- a shared persistent data store leaks cookies and tokens across web views.

Combined with message handlers, the impact is high. Legacy `UIWebView` is deprecated and rejected by the App Store.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trace where loaded URLs come from: deep links, server data, user input (MASTG-TEST-0332).
- Review navigation-policy handling, ATS web-content exceptions, and certificate-challenge handling (MASTG-TEST-0397).
- Check use of deprecated WebView APIs (MASTG-TEST-0331) and the cookie and data-store setup.
- Test redirects and running untrusted content.

## How to Fix

- Limit navigation to an allowlist in `decidePolicyFor`, or use App-Bound Domains (`limitsNavigationsToAppBoundDomains`).
- Do not load untrusted content in privileged web views, and turn off JavaScript where it is not needed (`WKWebpagePreferences.allowsContentJavaScript`).
- Keep ATS on for web content, and do not override certificate validation.
- Use a non-persistent `WKWebsiteDataStore` for sensitive sessions and clear sensitive cookies.
- Open arbitrary external URLs in `SFSafariViewController` or Safari. Map to MASVS-PLATFORM-2 and MASVS-NETWORK-1. See `xss`, `incorrect-certificate-validation`, and `ios-wkwebview-message-handlers`.

## References

- [MASTG-TEST-0332: Attacker-Controlled URI in WebViews](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0332/)
- [MASTG-TEST-0397: References to WKNavigationDelegate Bypassing Certificate Validation](https://mas.owasp.org/MASTG/tests/ios/MASVS-NETWORK/MASTG-TEST-0397/)
- [Apple: limitsNavigationsToAppBoundDomains](https://developer.apple.com/documentation/webkit/wkwebviewconfiguration/limitsnavigationstoappbounddomains)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
