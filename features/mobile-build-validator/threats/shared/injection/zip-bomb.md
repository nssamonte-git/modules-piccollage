---
id: zip-bomb
title: Decompression Bomb (Zip Bomb)
category: injection
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-409, CWE-400]
severity: medium
last_verified: 2026-10-29
aliases: [decompression-bomb, archive-bomb]
---

# Decompression Bomb (Zip Bomb)

## Why It Is a Threat

A small archive or compressed payload expands to an enormous size on decompression, exhausting CPU, memory, or disk and causing denial of service. Applies to zip/gzip/tar uploads, nested archives, container formats that are zip underneath (DOCX, XLSX, ODT, APK), and compressed request bodies (`Content-Encoding: gzip`/`br`). Image "pixel floods" are a close relative. Also tracked under `availability`.

## How to Find or Test

Authorized testing only, with owner approval and resource caps (follow the `smx-ai-vapt` authorization gate).

- Submit a bounded decompression-bomb sample to archive and compressed-input handlers and observe resource growth against configured limits — do not exhaust production resources.

## How to Fix

- Before and during extraction, enforce limits on total uncompressed size, file count, compression ratio, and nesting depth; stream with hard caps rather than trusting sizes declared in archive headers (ASVS 5.0 V5.2 File Upload and Content, requirement 5.2.3).
- Apply the same caps to decompressed HTTP request bodies, and process untrusted decompression in isolated, resource-limited workers.

## References

- [CWE-409: Improper Handling of Highly Compressed Data (Data Amplification)](https://cwe.mitre.org/data/definitions/409.html)
- [OWASP File Upload Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html)
- [CWE-400: Uncontrolled Resource Consumption](https://cwe.mitre.org/data/definitions/400.html)
