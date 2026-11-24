---
id: format-string
title: Format-String Attack
category: injection
platforms: [shared, backend, android, ios]
owasp_refs: [A05:2025, WSTG-INPV-13, MASVS-CODE]
cwe: [CWE-134]
severity: medium
last_verified: 2026-10-29
---

# Format-String Attack

## Why It Is a Threat

Untrusted input is used as a format string (e.g. `printf(userInput)`, `syslog`, `NSLog(userInput)`, `String(format:)`/`stringWithFormat:`), letting an attacker read memory via `%x`/`%s`/`%@` or write via `%n` in C-family code, leaking data or corrupting memory. In managed languages (Java `String.format`, Python `str.format` on templates) the impact is usually exceptions, denial of service, or attribute disclosure rather than memory writes.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Search code for formatting and logging calls whose format argument is not a constant; compiler and static-analysis warnings flag most cases.
- Submit format specifiers (`%x`, `%s`, `%@`, `{0}`) into inputs that reach formatting/logging functions; observe leakage, errors, or crashes on an authorized build.

## How to Fix

- Never pass untrusted input as the format string; use a constant format with the input as an argument (`printf("%s", input)`, `NSLog(@"%@", input)`) (ASVS 5.0 V1.3 Sanitization, requirement 1.3.10).
- Enable compiler format checks (`-Wformat -Wformat-security`, ideally `-Werror=format-security`) and treat them as build failures.

## References

- [OWASP WSTG v4.2 WSTG-INPV-13: Testing for Format String Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/13-Testing_for_Format_String_Injection/)
- [CWE-134: Use of Externally-Controlled Format String](https://cwe.mitre.org/data/definitions/134.html)
- [OWASP Community: Format string attack](https://owasp.org/www-community/attacks/Format_string_attack)
