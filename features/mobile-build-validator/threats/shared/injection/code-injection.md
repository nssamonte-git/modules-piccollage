---
id: code-injection
title: Code Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-11]
cwe: [CWE-94, CWE-95]
severity: critical
last_verified: 2026-10-29
---

# Code Injection

## Why It Is a Threat

Untrusted input is evaluated as application code (e.g. `eval`, `exec`, `Function()`, `vm` scripts, dynamic `require`/`import` or class loading, PHP `include` of user-chosen paths), letting an attacker execute arbitrary code in the application runtime — typically full compromise. CWE-94 is in the 2025 CWE Top 25.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Find dynamic-evaluation sinks fed by user input (source review and SAST are the most reliable); confirm evaluation with a benign marker (e.g. a controlled arithmetic result or echoed string), not a weaponized payload.
- Escalation to full RCE requires explicit written approval.
- Source review by stack (`source-code-vapt`): Laravel and PHP `eval(`, `create_function(`, `assert(` with a string (PHP 7), and variable functions from input; Python `eval(`, `exec(`, `compile(`; React Native `eval(` and `new Function(`.

## How to Fix

- Remove dynamic code evaluation of untrusted input; use data-driven logic, safe parsers (e.g. JSON instead of `eval`), or explicit dispatch maps instead (ASVS 5.0 V1.3 Sanitization).
- If evaluation is unavoidable, restrict input to a strict allowlist and run it in an isolated, least-privilege sandbox; language-level "sandboxes" such as Node's `vm` are not a security boundary.

## References

- [CWE-94: Improper Control of Generation of Code ('Code Injection')](https://cwe.mitre.org/data/definitions/94.html)
- [CWE-95: Improper Neutralization of Directives in Dynamically Evaluated Code ('Eval Injection')](https://cwe.mitre.org/data/definitions/95.html)
- [OWASP WSTG v4.2 WSTG-INPV-11: Testing for Code Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/11-Testing_for_Code_Injection/)
- [OWASP Top 10:2025 A05 Injection](https://top10.owasp.org/2025/A05_2025-Injection/)
