---
id: zip-slip
title: Zip Slip (Archive Path Traversal)
category: injection
platforms: [shared, web, backend]
owasp_refs: [A01:2025]
cwe: [CWE-22]
severity: high
last_verified: 2026-10-29
---

# Zip Slip (Archive Path Traversal)

## Why It Is a Threat

An archive (zip, tar, jar, 7z) contains entries with `../` or absolute paths, or symlinks pointing outside the target, so naive extraction writes files outside the target directory — overwriting config, binaries, or web-root files and often leading to code execution. Affects servers that import archives and mobile apps that unpack downloaded bundles. See also `path-traversal`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review extraction code for entry names joined directly to the destination path without a containment check.
- Where the app extracts uploaded or imported archives, supply an archive containing a benign entry with a traversal path toward an owner-approved safe location and confirm whether it is written outside the target directory.
- Source review by stack (`source-code-vapt`): Python `tarfile.extractall` without `filter="data"` (the default before Python 3.14) and `zipfile` extraction to paths built from member names.

## How to Fix

- Ignore user-provided path information where possible; otherwise, before writing each entry, resolve the canonical destination and verify it stays inside the extraction directory, rejecting traversal and absolute entries (ASVS 5.0 V5.3 File Storage, requirement 5.3.3).
- Reject archives containing symlinks unless explicitly required (ASVS 5.0 V5.2 requirement 5.2.5). Use a maintained extraction library or API option that enforces path checks, and extract with least privilege.

## References

- [CWE-22: Improper Limitation of a Pathname to a Restricted Directory ('Path Traversal')](https://cwe.mitre.org/data/definitions/22.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP Application Security Verification Standard (ASVS)](https://owasp.org/www-project-application-security-verification-standard/)
