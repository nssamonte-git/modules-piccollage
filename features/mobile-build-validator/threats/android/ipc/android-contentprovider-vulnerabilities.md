---
id: android-contentprovider-vulnerabilities
title: ContentProvider Vulnerabilities
category: android-ipc
platforms: [android]
owasp_refs: [MASVS-PLATFORM, MASTG-TEST-0355, MASTG-TEST-0339, M4:2024]
cwe: [CWE-926, CWE-89, CWE-22]
severity: high
last_verified: 2026-10-29
aliases: [contentprovider-data-leakage, contentprovider-sql-injection, contentprovider-path-traversal]
---

# ContentProvider Vulnerabilities

## Why It Is a Threat

Exported or weakly protected ContentProviders let other apps read or write data, inject SQL through `selection`, `projection`, or `sortOrder`, or traverse paths through `openFile` to read arbitrary app files. The result is a direct cross-app data breach. Providers in apps targeting API 16 or lower are exported by default.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Review provider declarations (`exported`, `readPermission`/`writePermission`, `grantUriPermissions`, path permissions) for unauthorized database access (MASTG-TEST-0355).
- Query, insert, and update from an unprivileged app (`drozer`, `adb shell content`); test query arguments for SQL injection (MASTG-TEST-0339) and `openFile` paths for traversal.

## How to Fix

- Keep providers non-exported unless required. For exported providers enforce `signature`-level read and write permissions and grant per-URI access only.
- Use parameterized `selectionArgs`, and validate projection and sort columns against an allowlist (for example a strict `SQLiteQueryBuilder` with a projection map) (`sql-injection`). Canonicalize and confine `openFile` paths (`path-traversal`).
- Validate the caller and all inputs. Map to MASVS-PLATFORM and M4:2024.

## References

- [Android Developers: SQL injection](https://developer.android.com/privacy-and-security/risks/sql-injection)
- [Android Developers: Path traversal](https://developer.android.com/privacy-and-security/risks/path-traversal)
- [MASTG-TEST-0355: References to Unauthorized Database Access through Content Providers](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0355/)
- [MASTG-TEST-0339: SQL Injection in Content Providers](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0339/)
- [CWE-926: Improper Export of Android Application Components](https://cwe.mitre.org/data/definitions/926.html)
