---
id: ios-wkwebview-message-handlers
title: Unsafe WKWebView Message Handlers
category: ios-webview
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0376]
cwe: [CWE-749, CWE-79]
severity: high
last_verified: 2026-10-29
aliases: [js-bridge-abuse, unsafe-script-message-handler]
---

# Unsafe WKWebView Message Handlers

## Why It Is a Threat

`WKScriptMessageHandler` and `WKScriptMessageHandlerWithReply` bridges expose native functions to JavaScript. Handlers are registered in the page content world and in every frame by default. If the WebView loads untrusted or remote content, or the content is open to XSS, that script can post messages to call native functions, steal data, or perform privileged actions. Replies sent back with `evaluateJavaScript` can inject script or write secrets into the page DOM. The bridge is only as safe as the content loaded into it.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- List the registered handlers and the native actions they expose (MASTG-TEST-0376).
- Check whether handlers check the sender, which content world they use, and how they reply (MASTG-TEST-0377, MASTG-TEST-0379).
- Check whether the WebView loads untrusted or remote content, and test calling handlers from injected script.
- Source review by stack (`source-code-vapt`): iOS `userContentController.add(_:name:)` without `frameInfo.securityOrigin` checks; React Native `react-native-webview` `onMessage` without checking `event.nativeEvent.url`; Flutter `addJavaScriptChannel` or `flutter_inappwebview` handlers on untrusted pages.

## How to Fix

- Expose handlers only to trusted content. Check `message.frameInfo` (main frame, `securityOrigin`) and validate and authorize every message.
- Register handlers and app scripts in an isolated `WKContentWorld` instead of `.page`. Prefer reply handlers over writing results with `evaluateJavaScript`.
- Keep the bridge small, and remove handlers you no longer need (`removeScriptMessageHandler(forName:)`). Map to MASVS-PLATFORM-2. See `ios-wkwebview-untrusted-content`.

## References

- [MASTG-TEST-0376: References to Native Bridge APIs in WebViews](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0376/)
- [MASTG-TEST-0379: References to evaluateJavaScript Without Content World Isolation](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0379/)
- [Apple: WKContentWorld](https://developer.apple.com/documentation/webkit/wkcontentworld)
- [CWE-749: Exposed Dangerous Method or Function](https://cwe.mitre.org/data/definitions/749.html)
