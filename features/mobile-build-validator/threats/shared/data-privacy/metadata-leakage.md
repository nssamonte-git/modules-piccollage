---
id: metadata-leakage
title: Metadata Leakage
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, A02:2025, WSTG-INFO-05]
cwe: [CWE-200, CWE-1230, CWE-497]
severity: low
last_verified: 2026-10-29
---

# Metadata Leakage

## Why It Is a Threat

Metadata reveals more than intended: EXIF/GPS location in uploaded images, document author/revision/tracked-change data, internal hostnames, paths, and component versions in headers and errors, source comments, timestamps, and sequential object ids that disclose volume or identity. It aids reconnaissance and can expose personal data such as a user's home location.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Upload and re-download test files and inspect them for EXIF and document metadata.
- Review response headers, error bodies, page source and comments, and ids for internal details.

## How to Fix

- Strip metadata from user-uploaded media and documents on ingest unless the user consents to keep it (ASVS 5.0 V14.2.8).
- Remove version and internal details from headers, errors, and served content (ASVS 5.0 V13.4.6 and V16.5.1); use opaque, non-enumerable identifiers.

## References

- [OWASP WSTG: Review Web Page Content for Information Leakage (WSTG-INFO-05)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/01-Information_Gathering/05-Review_Web_Page_Content_for_Information_Leakage)
- [CWE-1230: Exposure of Sensitive Information Through Metadata](https://cwe.mitre.org/data/definitions/1230.html)
- [CWE-497: Exposure of Sensitive System Information to an Unauthorized Control Sphere](https://cwe.mitre.org/data/definitions/497.html)
