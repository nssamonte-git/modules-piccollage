---
id: android-input-capture
title: Malicious Keyboard / Input Capture
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-PRIVACY, MASTG-TEST-0258]
cwe: [CWE-200, CWE-524]
severity: medium
last_verified: 2026-10-29
aliases: [malicious-keyboard-capture, ime-keylogging]
---

# Malicious Keyboard / Input Capture

## Why It Is a Threat

A malicious or insecure third-party input method (keyboard) can capture everything the user types, including passwords, card numbers, and messages entered into your app, and may send it off-device. Keyboards that learn from input can also cache and later suggest sensitive values.

## How to Find or Test

Authorized testing only (resilience review; follow the `smx-ai-vapt` authorization gate).

- Check that sensitive fields use password or no-suggestion input types and do not let the keyboard learn from them (MASTG-TEST-0258), and assess what high-risk input is exposed to third-party keyboards.

## How to Fix

- Use password input types (`textPassword`, `numberPassword`) for secrets and set `IME_FLAG_NO_PERSONALIZED_LEARNING` on sensitive fields so the keyboard does not store typing history.
- For the most sensitive inputs (PINs), consider an in-app keypad; avoid blocking system keyboards broadly. Managed (enterprise) deployments can restrict allowed keyboards through device policy.
- Limit what users type, for example by tokenizing card entry through the payment provider. Map to MASVS-PLATFORM and MASVS-PRIVACY.

## References

- [MASTG-TEST-0258: References to Keyboard Caching Attributes in UI Elements](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0258/)
- [Android Developers: EditorInfo (IME_FLAG_NO_PERSONALIZED_LEARNING)](https://developer.android.com/reference/android/view/inputmethod/EditorInfo)
- [CWE-524: Use of Cache Containing Sensitive Information](https://cwe.mitre.org/data/definitions/524.html)
