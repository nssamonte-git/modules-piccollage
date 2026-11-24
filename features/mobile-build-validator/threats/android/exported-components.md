---
id: android-exported-components
title: Improperly Exported Android Components
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0364, M8:2024]
cwe: [CWE-926]
severity: high
last_verified: 2026-10-29
---

# Improperly Exported Android Components

## Why It Is a Threat

Activities, services, broadcast receivers, and content providers that are exported are reachable by every other app on the device. Without permission checks they let an unprivileged app invoke privileged actions, read or write provider data (including SQL injection or path traversal in providers), or drive the app into unintended states. Export defaults have differed by component type and API level (providers were exported by default on API 16 and lower; a component with an intent filter was exported implicitly before Android 12), so components are often exposed by accident.

## How to Find or Test

Authorized testing only, on an owner-provided build and authorized test device (follow the `smx-ai-vapt` authorization gate).

- Decompile the APK (`jadx`/`apktool`) and review the merged `AndroidManifest.xml` for components with `android:exported="true"`, or with intent filters and no explicit value, that have no `android:permission`. Include receivers registered at runtime without `RECEIVER_NOT_EXPORTED`.
- Enumerate and invoke exported components from an unprivileged test app (`drozer`, `adb shell am`/`content`); probe content providers for SQL injection and path traversal.
- Send crafted intent extras and deep-link parameters to exported entry points and look for unvalidated actions, auth bypass, or intent redirection.

## How to Fix

- Set `android:exported` explicitly on every component (mandatory for components with intent filters when targeting Android 12+) and use `false` unless other apps must reach it. For required exports, enforce a `signature`-level permission. Register runtime receivers with `RECEIVER_NOT_EXPORTED` unless export is needed (an export flag is mandatory when targeting Android 14+).
- Treat all incoming intent data as untrusted: validate and authorize it in the component, parameterize content-provider queries, and confine file paths.
- Keep sensitive data out of implicit intents. Map to MASVS-PLATFORM and M8:2024; see `android-intent-redirection` and `android-contentprovider-vulnerabilities`.

## References

- [Android Developers: android:exported](https://developer.android.com/privacy-and-security/risks/android-exported)
- [Android Developers: Permission-based access control to exported components](https://developer.android.com/privacy-and-security/risks/access-control-to-exported-components)
- [MASTG-TEST-0364: Exported And Unprotected Activities That Expose Sensitive Functionality](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0364/)
- [OWASP Mobile Top 10 2024 M8: Security Misconfiguration](https://owasp.org/www-project-mobile-top-10/2023-risks/m8-security-misconfiguration)
- [CWE-926: Improper Export of Android Application Components](https://cwe.mitre.org/data/definitions/926.html)
