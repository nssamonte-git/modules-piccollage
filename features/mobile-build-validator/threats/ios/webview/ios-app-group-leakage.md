---
id: ios-app-group-leakage
title: App Group / IPC Leakage
category: ios-webview
platforms: [ios]
owasp_refs: [MASVS-PLATFORM, MASVS-STORAGE, MASTG-TEST-0388]
cwe: [CWE-200, CWE-732]
severity: medium
last_verified: 2026-10-29
aliases: [app-group-cross-application-leakage, unsafe-ipc-ios]
---

# App Group / IPC Leakage

## Why It Is a Threat

App Group containers (shared `UserDefaults(suiteName:)`, files, and databases between an app, its extensions, and other apps from the same team) can leak sensitive data to every member of the group. Other IPC channels leak to any app that can read them: custom URL schemes, which any app can claim, and the general pasteboard. Membership that is too broad, or unprotected shared data, creates a cross-app data exposure path.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review App Group membership across the team's apps and extensions, and what sensitive data sits unprotected in shared containers (MASTG-TEST-0388).
- Check IPC channels (URL schemes, pasteboard) for sensitive data.

## How to Fix

- Limit App Group membership to the apps and extensions that need it.
- Keep secrets in the Keychain, not in shared `UserDefaults` or files. Encrypt sensitive shared data and apply strict Data Protection.
- Do not pass sensitive data through the pasteboard or URL schemes. Prefer Universal Links. Map to MASVS-PLATFORM-1 and MASVS-STORAGE-1. See `ios-data-leakage-surfaces` and `ios-deep-link-abuse`.

## References

- [MASTG-TEST-0388: References to Sensitive Data Stored Unprotected in Shared App Group Containers](https://mas.owasp.org/MASTG/tests/ios/MASVS-STORAGE/MASTG-TEST-0388/)
- [Apple Platform Security: App protection and app groups](https://support.apple.com/guide/security/app-protection-and-app-groups-sec1a976c067/web)
- [MASVS-PLATFORM: Platform Interaction](https://mas.owasp.org/MASVS/09-MASVS-PLATFORM/)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
