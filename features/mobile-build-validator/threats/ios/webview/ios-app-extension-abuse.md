---
id: ios-app-extension-abuse
title: App Extension Abuse
category: ios-webview
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, M4:2024]
cwe: [CWE-20, CWE-200]
severity: medium
last_verified: 2026-10-29
aliases: [share-extension-input-manipulation, document-provider-abuse, app-extension-data-leakage]
---

# App Extension Abuse

## Why It Is a Threat

App extensions take input from other apps and share data with their containing app. Examples are share, action, File Provider (formerly document provider), widget, notification service, and keyboard extensions. If an extension trusts that input, or leaks sensitive data through its storage, the App Group, or handoff to the containing app, a malicious host app can inject data or harvest information across the trust boundary. Each extension is a separate binary with its own entitlements.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review each extension's `NSExtensionActivationRule`: a `TRUEPREDICATE` or overly broad rule accepts anything.
- Review how received items are validated, and what data crosses between the extension and the containing app (App Group containers, shared Keychain, pasteboard, files).
- Review the extension's own entitlements.

## How to Fix

- Treat all extension input as untrusted. Restrict activation rules to the types you need, and validate and authorize content before acting on it.
- Share as little data as possible between the extension and the containing app, and protect what is shared. Keep App Group data scoped (`ios-app-group-leakage`).
- Give each extension only the entitlements it needs. Require the containing app's authenticated session for sensitive actions. Map to MASVS-PLATFORM-1.

## References

- [Apple Platform Security: Supporting extensions](https://support.apple.com/guide/security/supporting-extensions-secabd3504cd/web)
- [MASVS-PLATFORM: Platform Interaction](https://mas.owasp.org/MASVS/09-MASVS-PLATFORM/)
- [OWASP Mobile Top 10 2024 M4: Insufficient Input/Output Validation](https://owasp.org/www-project-mobile-top-10/2023-risks/m4-insufficient-input-output-validation)
- [CWE-20: Improper Input Validation](https://cwe.mitre.org/data/definitions/20.html)
