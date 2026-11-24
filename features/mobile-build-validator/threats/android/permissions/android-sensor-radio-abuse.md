---
id: android-sensor-radio-abuse
title: Sensor / Radio Permission Abuse
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PRIVACY, MASVS-PLATFORM, M6:2024]
cwe: [CWE-359, CWE-200]
severity: medium
last_verified: 2026-10-29
aliases: [bluetooth-permission-abuse, nfc-intent-interception, location-permission-abuse, background-sensor-collection]
---

# Sensor / Radio Permission Abuse

## Why It Is a Threat

Bluetooth, Wi-Fi scanning, NFC, location, and motion-sensor access are abused to track users, collect data in the background, or intercept NFC intents. These are privacy violations and fingerprinting vectors, often pulled in by SDKs beyond the app's stated purpose. Related: `proximity-wireless-interception`, `tracking-fingerprinting`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review Bluetooth, Wi-Fi, NFC, location, and sensor permissions and background access against need; check NFC intent filters and what SDKs collect.

## How to Fix

- Request these only when needed and with the least access: approximate location, foreground-only by default, and background location requested separately and only with a clear user-facing reason. Use `BLUETOOTH_SCAN`/`NEARBY_WIFI_DEVICES` with `neverForLocation` when the app does not derive location.
- Treat NFC tag and intent data as untrusted input. Audit SDKs for covert collection.
- Map to MASVS-PRIVACY and M6:2024; see `tracking-fingerprinting`.

## References

- [Android Developers: Request location permissions](https://developer.android.com/develop/sensors-and-location/location/permissions)
- [Android Developers: Bluetooth permissions](https://developer.android.com/develop/connectivity/bluetooth/bt-permissions)
- [OWASP Mobile Top 10 2024 M6: Inadequate Privacy Controls](https://owasp.org/www-project-mobile-top-10/2023-risks/m6-inadequate-privacy-controls)
- [CWE-359: Exposure of Private Personal Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/359.html)
