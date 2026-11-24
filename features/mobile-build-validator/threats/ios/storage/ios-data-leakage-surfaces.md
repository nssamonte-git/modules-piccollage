---
id: ios-data-leakage-surfaces
title: iOS Data-Leakage Surfaces
category: ios-storage
platforms: [ios]
owasp_refs: [MASVS-STORAGE, MASVS-PLATFORM, MASTG-TEST-0277, MASTG-TEST-0290]
cwe: [CWE-200, CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [pasteboard-leakage, universal-clipboard-leakage, screenshot-leakage-ios, app-switcher-snapshot-leakage, notification-content-leakage-ios, screen-recording-leakage]
---

# iOS Data-Leakage Surfaces

## Why It Is a Threat

Sensitive data can leak through iOS system surfaces even without a storage bug:

- the general pasteboard, which other apps can read and Universal Clipboard can carry to other devices;
- the app-switcher snapshot;
- user screenshots;
- notification content on the lock screen;
- screen recording, mirroring, and AirPlay.

This card is the iOS-specific case of `sensitive-data-in-client-storage`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- After sensitive flows, check:
  - the general pasteboard: what is placed on it and whether it expires or stays on the device (MASTG-TEST-0276 to MASTG-TEST-0280);
  - the app-switcher snapshot (MASTG-TEST-0290);
  - lock-screen notification previews;
  - what is shown during screen recording or mirroring.
- Source review by stack (`source-code-vapt`): SwiftUI views without `.privacySensitive()` or a blur when `scenePhase` leaves `.active`; Flutter screens without a privacy overlay on iOS.

## How to Fix

- Hide sensitive UI before the system takes the snapshot: in `sceneWillResignActive`, or when SwiftUI `scenePhase` leaves `.active`.
- Avoid the general pasteboard for secrets. If copying is needed, use `setItems(_:options:)` with `.localOnly` and a short `.expirationDate`, and clear the pasteboard after use.
- Keep secrets out of notifications. Use `hiddenPreviewsBodyPlaceholder` for sensitive categories.
- Detect capture with `UITraitCollection.sceneCaptureState` (iOS 17+; `UIScreen.isCaptured` is deprecated) and hide high-risk content. Screenshots cannot be blocked; `userDidTakeScreenshotNotification` only reports them afterwards. Map to MASVS-STORAGE-2 and MASVS-PLATFORM-3.

## References

- [MASTG-TEST-0277: Sensitive Data in the iOS General Pasteboard at Runtime](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0277/)
- [MASTG-TEST-0290: Runtime Verification of Sensitive Content Exposure in Screenshots During App Backgrounding](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0290/)
- [Apple: sceneCaptureState](https://developer.apple.com/documentation/uikit/uitraitcollection/scenecapturestate)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
