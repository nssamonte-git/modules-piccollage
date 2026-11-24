---
id: android-deep-link-abuse
title: Deep-Link / App-Link Abuse
category: android-webview
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0393, MASTG-TEST-0394, M4:2024]
cwe: [CWE-939, CWE-20]
severity: high
last_verified: 2026-10-29
aliases: [custom-scheme-hijacking, deep-link-parameter-injection, unverified-app-links, deep-link-authorization-bypass]
---

# Deep-Link / App-Link Abuse

## Why It Is a Threat

Any app can register the same custom URL scheme, so links and the tokens they carry can be hijacked. Unverified App Links, and deep-link handlers that trust their parameters, let attackers inject data, load attacker URLs into WebViews, redirect flows, or invoke authenticated actions without proper authorization.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Enumerate intent filters and schemes; check that `http`/`https` links are verified App Links (MASTG-TEST-0393); send crafted deep links from another app and test parameter validation (MASTG-TEST-0394) and whether sensitive actions are reachable without authentication.
- Source review by stack (`source-code-vapt`): Android `intent.data` handling and missing `android:autoVerify`; Jetpack Compose `navDeepLink { uriPattern = ... }` arguments; React Native `Linking` and React Navigation `linking` params; Flutter `app_links` streams and `go_router` routes.

## How to Fix

- Use verified Android App Links (`autoVerify` plus a Digital Asset Links file) instead of custom schemes for sensitive flows; since Android 12, unverified web links open in the browser instead of the app.
- Validate every deep-link parameter against an allowlist, check authentication and authorization state before acting, and parse full scheme and host before loading any URL into a WebView.
- Do not perform privileged actions purely from a deep link. Map to MASVS-PLATFORM and M4:2024.

## References

- [Android Developers: Unsafe use of deep links](https://developer.android.com/privacy-and-security/risks/unsafe-use-of-deeplinks)
- [Android Developers: Verify App Links](https://developer.android.com/training/app-links/verify-applinks)
- [MASTG-TEST-0393: Use of Unverified App Links](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0393/)
- [MASTG-TEST-0394: Missing Input Validation in Custom URL Scheme Handlers](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0394/)
- [CWE-939: Improper Authorization in Handler for Custom URL Scheme](https://cwe.mitre.org/data/definitions/939.html)
