---
id: null-byte-injection
title: Null-Byte Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025]
cwe: [CWE-158, CWE-626]
severity: medium
last_verified: 2026-10-29
---

# Null-Byte Injection

## Why It Is a Threat

A null byte (`%00`) in input truncates strings when a managed language hands them to lower-level C APIs (file paths, native libraries), letting an attacker bypass extension/suffix checks or smuggle values past validation — e.g. `shell.php%00.jpg` passing an image filter. Most current runtimes (modern PHP, Java, Python, and Node.js) reject null bytes in file-path APIs, so the risk now sits mainly in legacy stacks, native code, and custom parsers.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Append `%00` and other encodings of the null byte to file names, paths, and validated parameters; observe whether downstream parsing truncates and bypasses checks.

## How to Fix

- Reject null bytes and control characters in input; decode to canonical form once and validate after decoding, not before (ASVS 5.0 V1.1 Encoding and Sanitization Architecture, requirement 1.1.1).
- Use safe, length-aware string and file APIs, keep runtimes current, and build file paths from server-generated names (ASVS 5.0 V5.3 requirement 5.3.2).

## References

- [OWASP Community: Embedding Null Code](https://owasp.org/www-community/attacks/Embedding_Null_Code)
- [CWE-158: Improper Neutralization of Null Byte or NUL Character](https://cwe.mitre.org/data/definitions/158.html)
- [CWE-626: Null Byte Interaction Error (Poison Null Byte)](https://cwe.mitre.org/data/definitions/626.html)
