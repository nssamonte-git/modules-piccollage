---
id: css-injection
title: CSS Injection
category: injection
platforms: [web]
owasp_refs: [A05:2025, WSTG-CLNT-05]
cwe: [CWE-79, CWE-1021]
severity: medium
last_verified: 2026-10-29
---

# CSS Injection

## Why It Is a Threat

Attacker-controlled CSS (injected styles, themes, style attributes) can exfiltrate page data such as CSRF tokens or attribute values through attribute selectors that trigger background or font requests, perform UI redress, or hide and overlay content to trick users, all without executing JavaScript.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify sinks that accept user-controlled styles (themes, `style` attributes, `<style>` blocks, CSS variables, CSS-in-JS values).
- Test attribute-selector exfiltration patterns against an owner-approved collector; confirm style application and any outbound request, and note whether CSP `style-src`/`img-src`/`font-src` would block it.

## How to Fix

- Do not allow user-controlled CSS; where styling input is needed, allowlist properties and values, and encode untrusted data for the CSS context.
- Isolate untrusted content (sandboxed iframe or separate origin) and restrict `style-src`, `img-src`, and `font-src` with CSP to limit exfiltration.
- Map to ASVS 5.0 V1.3.5 (sanitize user CSS) and V1.2.1 (CSS-context encoding).

## References

- [OWASP WSTG: Testing for CSS Injection (WSTG-CLNT-05)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/05-CSS_Injection/)
- [OWASP Cross Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
