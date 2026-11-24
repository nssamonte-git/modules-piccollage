---
id: unrestricted-file-download
title: Unrestricted File Download / Distribution
category: web-files
platforms: [web]
owasp_refs: [A01:2025, A02:2025]
cwe: [CWE-22, CWE-434]
severity: medium
last_verified: 2026-10-29
aliases: [content-disposition-attack, stored-malware-distribution]
---

# Unrestricted File Download / Distribution

## Why It Is a Threat

Download endpoints that lack authorization or path constraints let attackers retrieve arbitrary or other users' files (`path-traversal`, `idor-bola`) or use your trusted domain to host and distribute malware. Unsanitized filenames in `Content-Disposition` can also enable header injection or misleading downloads (spoofed extensions).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Test download endpoints for per-file authorization, path constraints, and filename/`Content-Disposition` handling; check whether user-uploaded content can be served as trusted downloads from the main domain.

## How to Fix

- Authorize every download and map opaque ids to files server-side (no user-supplied paths); set the download filename yourself and encode it per RFC 6266 (`filename*`).
- Scan untrusted files before serving them and serve user content from a separate origin.
- Map to ASVS 5.0 V5 File Handling (V5.3.2, V5.4.1 to V5.4.3) and V8 Authorization; see `malicious-file-upload`, `path-traversal`.

## References

- [OWASP WSTG: Testing Directory Traversal File Include (WSTG-ATHZ-01)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/05-Authorization/01-Directory_Traversal_File_Include/)
- [CWE-22: Improper Limitation of a Pathname to a Restricted Directory ('Path Traversal')](https://cwe.mitre.org/data/definitions/22.html)
- [RFC 6266: Use of the Content-Disposition Header Field in HTTP](https://www.rfc-editor.org/rfc/rfc6266.html)
- [OWASP File Upload Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html)
