---
id: verbose-errors
title: Verbose Errors / Directory Listing
category: config-infra
platforms: [shared, web, backend]
owasp_refs: [A02:2025, A10:2025, WSTG-ERRH-01]
cwe: [CWE-209, CWE-548]
severity: medium
last_verified: 2026-10-29
aliases: [directory-listing, information-disclosure-errors]
---

# Verbose Errors / Directory Listing

## Why It Is a Threat

Detailed error messages (stack traces, SQL, framework and version strings, file paths) and directory listings hand attackers reconnaissance (internal structure, technologies, and sometimes secrets), accelerating exploitation.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trigger errors with malformed input and unexpected methods, and inspect responses for stack traces or internal detail; check whether web and app servers list directory contents or expose version banners.

## How to Fix

- Return generic error messages through a last-resort error handler and log details server-side only; disable directory listing.
- Strip detailed version banners. Map to A02:2025, A10:2025, and ASVS 5.0 V16.5 Error Handling and V13.4 Unintended Information Leakage.

## References

- [OWASP WSTG: Testing for Improper Error Handling](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/08-Error_Handling/01-Improper_Error_Handling)
- [OWASP Error Handling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Error_Handling_Cheat_Sheet.html)
- [CWE-209: Generation of Error Message Containing Sensitive Information](https://cwe.mitre.org/data/definitions/209.html)
- [CWE-548: Exposure of Information Through Directory Listing](https://cwe.mitre.org/data/definitions/548.html)
