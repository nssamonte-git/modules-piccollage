---
id: ios-push-apns-security
title: Push / APNs Security Weaknesses
category: ios-auth
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, A08:2025]
cwe: [CWE-345, CWE-200]
severity: medium
last_verified: 2026-10-29
aliases: [push-notification-authentication-abuse, apns-device-token-leakage]
---

# Push / APNs Security Weaknesses

## Why It Is a Threat

Push weaknesses lead to spoofed actions, tracking, and mass phishing pushes:

- **Commands in payloads.** The app treats push payloads (including silent `content-available` pushes) as authenticated commands.
- **Token handling.** APNs device tokens are identifiers, not secrets. They change, and leaking or misusing them helps tracking.
- **Provider key exposure.** A stolen APNs token-signing key (`.p8`) lets an attacker sign pushes to any app topic in your developer team (a provider certificate is scoped to one app). Each push still needs a target device token, so the damage grows if your stored device tokens leak too.

Push content shown on the lock screen can also leak data (`ios-data-leakage-surfaces`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review whether push payloads trigger privileged actions without a server-verified fetch.
- Review how device tokens are stored, shared with SDKs, and tied to users.
- Review where the APNs signing key and certificates are stored and who can access them.
- Review how sensitive notification text and previews are.

## How to Fix

- Treat a push as a trigger only. After it arrives, fetch the data over the app's authenticated channel, and verify on the server any action the push implies.
- Keep secrets out of payloads, or end-to-end encrypt them and decrypt in a Notification Service Extension. Set `hiddenPreviewsBodyPlaceholder` for sensitive categories.
- Request the device token on each launch, bind it to the authenticated user on the server, and remove it on logout.
- Keep the APNs signing key in a secret store with restricted access, and revoke it if it may be compromised. Map to MASVS-PLATFORM-1.

## References

- [Apple: Establishing a token-based connection to APNs](https://developer.apple.com/documentation/usernotifications/establishing-a-token-based-connection-to-apns)
- [Apple: Modifying content in newly delivered notifications](https://developer.apple.com/documentation/usernotifications/modifying-content-in-newly-delivered-notifications)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
