---
id: malicious-document-image-parsing
title: Malicious Document / Image Parsing
category: injection
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025, A02:2025]
cwe: [CWE-20, CWE-1284]
severity: medium
last_verified: 2026-10-29
aliases: [untrusted-media-parsing]
---

# Malicious Document / Image Parsing

## Why It Is a Threat

Parsing untrusted documents and images (PDF, Office, JPEG/PNG/GIF/WebP, SVG, fonts) exercises complex native parsers that have a long history of memory-safety and logic bugs, enabling crashes, code execution, XXE (Office/SVG), script in SVG, or SSRF through embedded URLs. Bugs in shared media libraries have been exploited in the wild (e.g. the 2023 libwebp heap overflow, CVE-2023-4863), and they affect servers that build thumbnails and mobile clients that render media without user interaction.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory the parsing, thumbnail, and preview libraries (ImageMagick, libvips, PDF and Office converters, platform image decoders) and check their versions against known CVEs.
- Submit malformed and edge-case files (oversized pixel dimensions, truncated structures) to parsing pipelines; fuzz under sanitizers on an authorized build and observe crashes or out-of-bounds behaviour.
- Test SVG/Office inputs for script and external-entity behaviour (`xxe`).

## How to Fix

- Keep media and document libraries patched and track them in an SBOM (ASVS 5.0 V15.2 Security Architecture and Dependencies).
- Isolate parsing in sandboxed, resource-limited, low-privilege workers; restrict converter features (e.g. an ImageMagick security policy), disable scripting and external entities, and sanitize SVG (ASVS 5.0 V1.3 requirement 1.3.4, V1.5 requirement 1.5.1).
- Validate type by content, reject oversized pixel dimensions, and re-encode images before storing or serving them (ASVS 5.0 V5.2 File Upload and Content).

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [OWASP File Upload Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html)
- [CWE-20: Improper Input Validation](https://cwe.mitre.org/data/definitions/20.html)
- [CWE-1284: Improper Validation of Specified Quantity in Input](https://cwe.mitre.org/data/definitions/1284.html)
