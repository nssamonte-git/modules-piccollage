---
id: ios-background-task-leakage
title: Background-Task Data Leakage
category: ios-auth
platforms: [ios]
owasp_refs: [MASVS-STORAGE, MASVS-PRIVACY, MASTG-TEST-0290]
cwe: [CWE-200, CWE-532]
severity: low
last_verified: 2026-10-29
---

# Background-Task Data Leakage

## Why It Is a Threat

Background work (`BGTaskScheduler` refresh/processing tasks, background fetch, silent pushes, location or audio modes) can write sensitive data to caches, files, or logs. It can also keep secrets in memory while the device is locked. When the app moves to the background, the system captures a snapshot of the screen for the app switcher, and an unredacted snapshot leaks data while the app is not in use. To make background access work, developers often weaken file and Keychain protection classes.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review what each background task reads, writes, and logs (`os_log`/`Logger`, `print`), and the Data Protection or Keychain class of what it touches while the device is locked.
- Background the app on sensitive screens and check whether the app-switcher snapshot is redacted (MASTG-TEST-0290).

## How to Fix

- Keep sensitive work and storage in the background to a minimum, and never log secrets or PII.
- Hide sensitive UI before the snapshot is taken: in `sceneWillResignActive`/`sceneDidEnterBackground`, or when SwiftUI `scenePhase` leaves `.active`.
- Choose the strictest protection class that still lets the task work: `.complete` for files not needed while locked, `.completeUnlessOpen` for files that must be written while locked, and `AfterFirstUnlockThisDeviceOnly` only for Keychain items that background refresh really needs.
- Clear sensitive in-memory data on backgrounding. Map to MASVS-STORAGE-2. See `ios-data-leakage-surfaces` and `sensitive-data-in-logs`.

## References

- [MASTG-TEST-0290: Runtime Verification of Sensitive Content Exposure in Screenshots During App Backgrounding](https://mas.owasp.org/MASTG/tests/ios/MASVS-PLATFORM/MASTG-TEST-0290/)
- [Apple Platform Security: Data Protection classes](https://support.apple.com/guide/security/data-protection-classes-secb010e978a/web)
- [MASVS-STORAGE: Storage](https://mas.owasp.org/MASVS/05-MASVS-STORAGE/)
- [CWE-532: Insertion of Sensitive Information into Log File](https://cwe.mitre.org/data/definitions/532.html)
