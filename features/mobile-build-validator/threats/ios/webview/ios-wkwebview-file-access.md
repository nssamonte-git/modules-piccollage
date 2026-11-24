---
id: ios-wkwebview-file-access
title: Unsafe WKWebView File URL Handling
category: ios-webview
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, MASVS-STORAGE, MASTG-TEST-0333]
cwe: [CWE-200, CWE-552]
severity: medium
last_verified: 2026-10-29
aliases: [unsafe-file-url-handling]
---

# Unsafe WKWebView File URL Handling

## Why It Is a Threat

Calling `loadFileURL(_:allowingReadAccessTo:)` with a read-access directory that is too broad (such as the Documents folder or the whole container) lets loaded or injected web content read private app files through `file://`. Turning on the undocumented `allowFileAccessFromFileURLs` or `allowUniversalAccessFromFileURLs` preferences makes this worse: local pages can then read other files or send them to remote origins.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the `allowingReadAccessTo` scope (MASTG-TEST-0333) and any key-value setting of the file-origin preferences (MASTG-TEST-0335, MASTG-TEST-0336).
- Check whether untrusted or user-supplied content is loaded with file access, and test whether that content can read app-private files.

## How to Fix

- Grant read access to the smallest directory possible: a dedicated folder of bundled assets, not the container or Documents.
- Never turn on the file-origin preferences.
- Do not load untrusted content with file access. Serve local assets through a `WKURLSchemeHandler` with a constrained resource set.
- Map to MASVS-PLATFORM-2.

## References

- [MASTG-TEST-0333: Overly Broad File Read Access in WebViews](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0333/)
- [MASTG-TEST-0335: WebView File Origin Access Relaxed by Configuration](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0335/)
- [Apple: loadFileURL(_:allowingReadAccessTo:)](https://developer.apple.com/documentation/webkit/wkwebview/loadfileurl%28_:allowingreadaccessto:%29)
- [CWE-552: Files or Directories Accessible to External Parties](https://cwe.mitre.org/data/definitions/552.html)
