---
id: push-notification-interception
title: Push-Notification Interception
category: network
platforms: [shared, android, ios]
owasp_refs: [MASVS-STORAGE, MASVS-NETWORK, MASVS-PRIVACY]
cwe: [CWE-200, CWE-359]
severity: medium
last_verified: 2026-10-29
---

# Push-Notification Interception

## Why It Is a Threat

Sensitive content placed in push notifications transits third-party push services (APNs/FCM) and is shown on the lock screen, exposing it to the provider, device access, and shoulder-surfing. Misconfigured payloads can also leak tokens or PII.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inspect push payloads for sensitive data (PII, tokens, full messages); check lock-screen visibility and whether sensitive notifications require unlock.

## How to Fix

- Send only non-sensitive triggers in pushes and fetch sensitive content in-app after authentication; if content must ride in the push, encrypt it end to end and decrypt on device (iOS Notification Service Extension, app-side handling of FCM data messages).
- Hide sensitive content on the lock screen (Android `VISIBILITY_PRIVATE`/`VISIBILITY_SECRET` with a redacted public version; iOS hidden-preview placeholders). Map to MASVS-STORAGE-2 and MASVS-PRIVACY; see `sensitive-data-in-client-storage`.

## References

- [Android: Create a notification (lock screen visibility)](https://developer.android.com/develop/ui/views/notifications/build-notification)
- [Apple: UNNotificationServiceExtension](https://developer.apple.com/documentation/usernotifications/unnotificationserviceextension)
- [OWASP MASVS-STORAGE](https://mas.owasp.org/MASVS/05-MASVS-STORAGE/)
- [CWE-359: Exposure of Private Personal Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/359.html)
