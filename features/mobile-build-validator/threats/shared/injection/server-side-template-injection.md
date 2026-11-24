---
id: server-side-template-injection
title: Server-Side Template Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-18]
cwe: [CWE-1336, CWE-94]
severity: high
last_verified: 2026-10-29
aliases: [ssti]
---

# Server-Side Template Injection

## Why It Is a Threat

Untrusted input is concatenated into a server-side template (Jinja2, Twig, Freemarker, Velocity, Thymeleaf, Handlebars, ERB) instead of being passed as data, letting an attacker access template internals and frequently escalate to remote code execution via engine gadgets. Common in user-editable email, notification, and report templates.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit an engine math probe (e.g. `{{7*7}}` per syntax) and observe evaluation; fingerprint the engine by differential syntax and error messages.
- Confirm template evaluation only; escalate to RCE gadgets solely with owner approval, preferring a benign marker over system commands.
- Source review by stack (`source-code-vapt`): Flask `render_template_string(` with user data; Jinja2 templates compiled from user input.

## How to Fix

- Never build templates from user input; load fixed templates and pass user data only as bound variables (ASVS 5.0 V1.3 Sanitization, requirement 1.3.7).
- Where users must author templates, use a logic-less engine or the engine's sandboxed mode with an allowlist, render in an isolated low-privilege worker, and keep the engine patched; sandboxes have a history of escapes, so do not rely on them alone.
- Keep auto-escaping on for output encoding.

## References

- [OWASP WSTG v4.2 WSTG-INPV-18: Testing for Server-side Template Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/18-Testing_for_Server-side_Template_Injection/)
- [CWE-1336: Improper Neutralization of Special Elements Used in a Template Engine](https://cwe.mitre.org/data/definitions/1336.html)
- [CWE-94: Improper Control of Generation of Code ('Code Injection')](https://cwe.mitre.org/data/definitions/94.html)
- [OWASP Top 10:2025 A05 Injection](https://top10.owasp.org/2025/A05_2025-Injection/)
