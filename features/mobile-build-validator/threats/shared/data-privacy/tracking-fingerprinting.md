---
id: tracking-fingerprinting
title: Location Tracking and Device Fingerprinting
category: data-privacy
platforms: [shared, web, android, ios]
owasp_refs: [A06:2025, MASVS-PRIVACY]
cwe: [CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [location-tracking, device-fingerprinting]
---

# Location Tracking and Device Fingerprinting

## Why It Is a Threat

Persistent or precise location collection, or device fingerprinting (hardware, browser, and OS signals, non-resettable identifiers), tracks users across sessions and apps without meaningful consent, enabling surveillance and de-anonymization and breaching platform privacy rules. Apple prohibits fingerprinting for tracking, and both mobile platforms restrict non-resettable identifiers.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify location and identifier collection and fingerprinting signals; check consent gating, precision (precise versus approximate), background access, and frequency.
- Review SDKs for covert fingerprinting and for use of hardware identifiers.

## How to Fix

- Collect location only when needed, at the minimum precision (approximate where possible), in the foreground, with consent (MASVS-PRIVACY-1 and MASVS-PRIVACY-2).
- Avoid fingerprinting; use resettable, scoped identifiers instead of hardware ids; request iOS App Tracking Transparency permission before cross-app tracking and declare required-reason APIs in the privacy manifest.

## References

- [OWASP MASVS-PRIVACY](https://mas.owasp.org/MASVS/12-MASVS-PRIVACY/)
- [Apple Developer: App Tracking Transparency](https://developer.apple.com/documentation/apptrackingtransparency)
- [Android Developers: Best practices for unique identifiers](https://developer.android.com/identity/user-data-ids)
- [Android Developers: Request location permissions](https://developer.android.com/develop/sensors-and-location/location/permissions)
