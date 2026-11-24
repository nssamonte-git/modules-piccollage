---
id: android-fileprovider-misconfig
title: FileProvider Misconfiguration
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASVS-STORAGE, MASTG-TEST-0357]
cwe: [CWE-732, CWE-200]
severity: medium
last_verified: 2026-10-29
aliases: [uri-permission-leakage]
---

# FileProvider Misconfiguration

## Why It Is a Threat

Over-broad `FileProvider` paths (`<root-path>`, `path="."`, the whole files directory, or `<external-path>` for sensitive data), or leaked content URIs with write, persistable, or prefix grants, let other apps read or overwrite files they should not reach. This exposes private app data and, where writable files feed code or configuration, can lead to code execution.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the FileProvider paths XML for over-broad roots (MASTG-TEST-0357); check how content URIs are shared, which grant flags are used, and whether received intents carrying grant flags are forwarded (`android-intent-redirection`).

## How to Fix

- Never use `<root-path>`; share only narrow subdirectories that contain nothing sensitive, and avoid `<external-path>` for sensitive files.
- Grant the minimum per-URI access (read only when that is enough), revoke grants when done, and avoid persistable or prefix grants for sensitive files.
- Validate incoming content URIs so they cannot point at your own private files. Map to MASVS-PLATFORM and MASVS-STORAGE.

## References

- [Android Developers: Improperly exposed directories to FileProvider](https://developer.android.com/privacy-and-security/risks/file-providers)
- [MASTG-TEST-0357: References to Oversharing of File-Based Content Providers](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0357/)
- [Android Developers: Setting up file sharing](https://developer.android.com/training/secure-file-sharing/setup-sharing)
- [CWE-732: Incorrect Permission Assignment for Critical Resource](https://cwe.mitre.org/data/definitions/732.html)
