---
id: ios-malicious-keyboard
title: Malicious Keyboards / Extensions
category: ios-auth
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, MASVS-PRIVACY, MASTG-TEST-0389]
cwe: [CWE-200]
severity: medium
last_verified: 2026-10-29
aliases: [custom-keyboard-capture-ios]
---

# Malicious Keyboards / Extensions

## Why It Is a Threat

A third-party custom keyboard that the user has granted Full Access (`RequestsOpenAccess`) can reach the network and shared containers. It can capture what the user types into your app's fields, including account numbers, OTPs, and personal data, and send it off the device. iOS already switches to the system keyboard for secure text fields and phone-pad fields. Every other field is exposed unless the app blocks custom keyboards.

## How to Find or Test

Authorized testing only (resilience review; follow the `smx-ai-vapt` authorization gate).

- Check whether the app restricts custom keyboards app-wide (MASTG-TEST-0389) and which sensitive fields are not `isSecureTextEntry` or a phone-pad keyboard type.
- For keyboard extensions you ship, check whether Full Access is requested without need (MASTG-TEST-0390).
- Check that sensitive fields turn off autocorrection so their input is not cached (MASTG-TEST-0313).

## How to Fix

- For apps that handle sensitive input, reject custom keyboards: return `false` from `application(_:shouldAllowExtensionPointIdentifier:)` for `.keyboard`.
- Use `isSecureTextEntry` for secrets, and turn off autocorrection and spell checking on sensitive fields.
- Make typed data less valuable (tokenization, one-time codes), and avoid asking users to type long-lived secrets.
- If you ship a keyboard, do not request Full Access unless it is needed. Map to MASVS-PLATFORM-3 and MASVS-PRIVACY-1. See `android-input-capture`.

## References

- [MASTG-TEST-0389: References to the App-Wide Restriction of Custom Keyboards](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0389/)
- [MASTG-TEST-0390: Full Access Requested by a Custom Keyboard Extension](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0390/)
- [Apple: application(_:shouldAllowExtensionPointIdentifier:)](https://developer.apple.com/documentation/uikit/uiapplicationdelegate/application%28_:shouldallowextensionpointidentifier:%29)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
