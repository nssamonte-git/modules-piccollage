---
id: dom-clobbering
title: DOM Clobbering
category: web-browser
platforms: [web]
owasp_refs: [A05:2025, WSTG-CLNT-01]
cwe: [CWE-79]
severity: medium
last_verified: 2026-10-29
---

# DOM Clobbering

## Why It Is a Threat

Attacker-injected HTML with `id` or `name` attributes overrides JavaScript globals and DOM properties through named property access on `window` and `document`, corrupting app logic (script URLs, config flags, sanitizer checks). It is an HTML-only path to XSS or logic bypass when script injection itself is blocked.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Where limited HTML is allowed, review which globals or `document.*` properties the code reads without declaring them, then inject elements whose `id`/`name` collide with those names and observe whether logic changes.

## How to Fix

- Declare variables explicitly (`const`/`let`, no implicit globals), check types strictly (for example `instanceof` or `typeof`) before use, do not store app state on `window`/`document`, and isolate config in a module or namespace.
- Sanitize user HTML with a sanitizer configured to block clobbering of DOM APIs and to strip or namespace `id`/`name` values.
- Map to ASVS 5.0 V3.2.3 (DOM clobbering) and V1.3.1, and the DOM Clobbering Prevention Cheat Sheet.

## References

- [OWASP DOM Clobbering Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/DOM_Clobbering_Prevention_Cheat_Sheet.html)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
- [OWASP WSTG: Testing for DOM-Based Cross Site Scripting (WSTG-CLNT-01)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/01-DOM-based_Cross_Site_Scripting/)
