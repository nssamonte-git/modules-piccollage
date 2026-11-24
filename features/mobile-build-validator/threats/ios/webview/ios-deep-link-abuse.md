---
id: ios-deep-link-abuse
title: Deep-Link / Universal-Link Abuse
category: ios-webview
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0370, MASTG-TEST-0395, M4:2024]
cwe: [CWE-939, CWE-20]
severity: high
last_verified: 2026-10-29
aliases: [custom-url-scheme-hijacking, universal-link-misconfiguration, deep-link-parameter-injection, deep-link-authorization-bypass, associated-domain-takeover]
---

# Deep-Link / Universal-Link Abuse

## Why It Is a Threat

Deep links give other apps and websites a way into the app:

- **Custom URL schemes have no owner.** Any app can register the same scheme, and iOS does not define which app wins. A malicious app can therefore catch links meant for yours, including OAuth redirects and tokens.
- **Universal Links fail open.** If the app is not installed or the link is not claimed, the system opens the link in the browser, which can leak tokens in the URL.
- **Handlers trust parameters.** Handlers that act on link parameters let attackers inject data, redirect flows, or trigger authenticated actions without authorization.
- **Lapsed domains.** If an associated domain lapses or is taken over, an attacker controls the `apple-app-site-association` file.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- List URL schemes (`CFBundleURLTypes`) and associated domains.
- Send crafted links and check:
  - input validation in scheme handlers (MASTG-TEST-0370) and Universal Link handlers (MASTG-TEST-0395);
  - checks on where a link came from (MASTG-TEST-0371);
  - whether sensitive actions run without authentication or confirmation.
- Fetch the `apple-app-site-association` file, which Apple serves through its CDN, and check its paths and domain ownership.
- Source review by stack (`source-code-vapt`): iOS `onOpenURL`, `application(_:open:options:)`, `scene(_:openURLContexts:)`; React Native `Linking.getInitialURL` and React Navigation `linking` params; Flutter `app_links` or `uni_links` streams and `go_router` query parameters.

## How to Fix

- Use Universal Links instead of custom schemes for sensitive flows, with a correctly hosted `apple-app-site-association` file on domains you control.
- Use `ASWebAuthenticationSession` with PKCE for OAuth. See `missing-state-nonce-pkce`.
- Treat every link parameter as untrusted. Validate it, require an authenticated session and user confirmation for state-changing actions, and authorize them on the server.
- Never put tokens in links. Keep associated domains registered. Map to MASVS-PLATFORM-1. See `dns-misconfig-subdomain-takeover`.

## References

- [MASTG-TEST-0370: Missing Input Validation in Custom URL Scheme Handlers](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0370/)
- [MASTG-TEST-0395: Missing Input Validation in Universal Link Handlers](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0395/)
- [Apple: Supporting associated domains](https://developer.apple.com/documentation/xcode/supporting-associated-domains)
- [CWE-939: Improper Authorization in Handler for Custom URL Scheme](https://cwe.mitre.org/data/definitions/939.html)
