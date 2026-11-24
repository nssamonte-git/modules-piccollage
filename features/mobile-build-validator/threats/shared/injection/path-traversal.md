---
id: path-traversal
title: Path Traversal
category: injection
platforms: [shared, web, backend]
owasp_refs: [A01:2025, A02:2025, WSTG-ATHZ-01]
cwe: [CWE-22]
severity: high
last_verified: 2026-10-29
aliases: [directory-traversal, lfi]
---

# Path Traversal

## Why It Is a Threat

Untrusted input is used to build a filesystem path without constraint, letting an attacker use `../` sequences, absolute paths, or encoded variants to read or write files outside the intended directory — config, secrets, source, or arbitrary write leading to code execution. When the path feeds an include/require, it becomes local or remote file inclusion. CWE-22 is in the 2025 CWE Top 25 and is mapped to A01:2025 Broken Access Control.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- In file download/upload/include/template parameters, submit traversal sequences (URL-encoded, double-encoded, Unicode, backslash variants) toward a known-safe marker file and confirm out-of-directory access.
- Test both read and write paths, absolute-path input, and null-byte/encoding variants.
- Source review by stack (`source-code-vapt`): Android provider `openFile` with `uri.lastPathSegment`; React Native `@ReactMethod` and Flutter `MethodChannel` handlers taking file paths; Laravel `response()->download($path)` or `Storage::get($path)` with user paths; PHP `readfile` or `file_get_contents` with user paths; Python `os.path.join(base, user)` (an absolute input replaces `base`) and Flask `send_file(user_path)`; Rust `base.join(user_input)`.

## How to Fix

- Prefer opaque ids mapped server-side to paths over user-supplied filenames (ASVS 5.0 V5.3 File Storage, requirement 5.3.2).
- Where a name must be used, validate it against a strict allowlist, resolve the canonical path after decoding, and verify it stays within the allowlisted base directory; blocking `../` alone is not sufficient.
- Run file-serving code with least-privilege filesystem permissions.

## References

- [OWASP WSTG v4.2 WSTG-ATHZ-01: Testing Directory Traversal File Include](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/05-Authorization_Testing/01-Testing_Directory_Traversal_File_Include/)
- [CWE-22: Improper Limitation of a Pathname to a Restricted Directory ('Path Traversal')](https://cwe.mitre.org/data/definitions/22.html)
- [OWASP Community: Path Traversal](https://owasp.org/www-community/attacks/Path_Traversal)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
