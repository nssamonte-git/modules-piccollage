---
id: polyglot-files
title: Polyglot Files
category: web-files
platforms: [web]
owasp_refs: [A06:2025, A05:2025, WSTG-BUSL-09]
cwe: [CWE-434, CWE-430]
severity: medium
last_verified: 2026-10-29
---

# Polyglot Files

## Why It Is a Threat

A polyglot is valid as several types at once (e.g. a file that is both a valid GIF and valid HTML or JavaScript). It passes a type or magic-byte check as an image but is interpreted as script or HTML when served or referenced in another context, bypassing upload filters to achieve stored XSS, CSP bypass via same-origin script, or content smuggling.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Upload polyglot samples (image plus HTML/JS) that pass type validation; check the served `Content-Type`, `nosniff`, and origin, and whether the file can be loaded as a script or rendered as a document.

## How to Fix

- Validate extension and content together, then re-encode or transcode uploads (rasterize images) so only the intended format survives.
- Serve user files from a separate origin with an accurate `Content-Type`, `nosniff`, `Content-Disposition: attachment`, and a restrictive CSP (`sandbox`).
- Do not rely on extension or MIME alone (`mime-sniffing`). Map to ASVS 5.0 V5.2.2, V3.2.1, and V3.4.4; see `malicious-file-upload`.

## References

- [OWASP File Upload Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html)
- [OWASP WSTG: Test Upload of Malicious Files (WSTG-BUSL-09)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/09-Upload_of_Malicious_Files/)
- [CWE-434: Unrestricted Upload of File with Dangerous Type](https://cwe.mitre.org/data/definitions/434.html)
