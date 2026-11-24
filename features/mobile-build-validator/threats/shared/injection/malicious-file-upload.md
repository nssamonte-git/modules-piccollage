---
id: malicious-file-upload
title: Malicious File Upload
category: injection
platforms: [shared, web, backend]
owasp_refs: [A06:2025, A02:2025, WSTG-BUSL-09]
cwe: [CWE-434]
severity: high
last_verified: 2026-10-29
---

# Malicious File Upload

## Why It Is a Threat

Unrestricted upload lets an attacker store executable or dangerous content (web shells, HTML/SVG with script, polyglots, macro documents, malware) that is later served or processed, leading to code execution, stored XSS, malware distribution, or SSRF. CWE-434 is in the 2025 CWE Top 25. Closely tied to `path-traversal`, `zip-slip`, and media parsing.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Upload benign test files with unexpected types/extensions/MIME, double extensions, case variants, oversized content, and active content (SVG/HTML); check whether they are stored in a served path, executed, or rendered inline, and how they are processed.
- Source review by stack (`source-code-vapt`): Laravel `getClientOriginalName()` in storage paths and uploads on the `public` disk; PHP `move_uploaded_file` to web-served paths; Django `FileField` `upload_to` built from client names.

## How to Fix

- Allowlist extensions and validate type by content (magic bytes, re-encoding for images), not by the client-supplied `Content-Type` (ASVS 5.0 V5.2 File Upload and Content).
- Store outside the web root, or in object storage, under randomized server-generated names, and ensure uploaded files can never execute as server-side code (ASVS 5.0 V5.3 File Storage).
- Serve through a controlled handler with correct `Content-Type`, `Content-Disposition: attachment` where inline rendering is not needed, and `X-Content-Type-Options: nosniff`, ideally from a separate domain (ASVS 5.0 V5.4 File Download).
- Enforce size and per-user quota limits, and scan content with antivirus/CDR before others can access it.

## References

- [OWASP File Upload Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html)
- [OWASP WSTG v4.2 WSTG-BUSL-09: Test Upload of Malicious Files](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/10-Business_Logic_Testing/09-Test_Upload_of_Malicious_Files/)
- [CWE-434: Unrestricted Upload of File with Dangerous Type](https://cwe.mitre.org/data/definitions/434.html)
- [OWASP Top 10:2025 A06 Insecure Design](https://top10.owasp.org/2025/A06_2025-Insecure_Design/)
