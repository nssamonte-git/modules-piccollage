---
id: android-sms-interception
title: SMS / OTP Interception
category: android-integrity
platforms: [android]
owasp_refs: [MASVS-AUTH, MASVS-PLATFORM, M3:2024, A07:2025]
cwe: [CWE-1390, CWE-319]
severity: high
last_verified: 2026-10-29
aliases: [sms-otp-interception]
---

# SMS / OTP Interception

## Why It Is a Threat

Malware with SMS permissions, an abused default-SMS role, or notification-listener or accessibility access reads incoming SMS OTPs, defeating SMS-based MFA and account recovery on the device itself. Android 15 redacts detected OTPs from notifications for untrusted notification listeners, but older devices and accessibility-based malware remain exposed (complements `otp-interception`, `sim-swapping`).

## How to Find or Test

Authorized testing only (resilience review; follow the `smx-ai-vapt` authorization gate).

- Assess reliance on SMS OTP for login, recovery, and transaction approval; check whether the app requests broad SMS permissions instead of using the SMS Retriever API; review OTP display in notifications and OTP lifetime.

## How to Fix

- Prefer phishing-resistant factors (passkeys) over SMS. If SMS stays, use the SMS Retriever API, which needs no SMS permission and only delivers messages that carry your app's identifying string.
- Bind each OTP to the session and action, expire it quickly with attempt limits, and keep account recovery and payee changes behind stronger factors.
- Map to MASVS-AUTH, M3:2024, and A07:2025; see `otp-interception`.

## References

- [Android Developers: Request SMS verification in an Android app](https://developer.android.com/identity/sms-retriever)
- [Android Developers: Android 15 behavior changes for all apps (OTP redaction)](https://developer.android.com/about/versions/15/behavior-changes-all)
- [Android Developers: About passkeys](https://developer.android.com/identity/passkeys)
- [OWASP Mobile Top 10 2024 M3: Insecure Authentication/Authorization](https://owasp.org/www-project-mobile-top-10/2023-risks/m3-insecure-authentication-authorization)
- [CWE-1390: Weak Authentication](https://cwe.mitre.org/data/definitions/1390.html)
