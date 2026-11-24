---
id: xss
title: Cross-Site Scripting (Reflected and Stored)
category: injection
platforms: [web]
owasp_refs: [A05:2025, WSTG-INPV-01, WSTG-INPV-02]
cwe: [CWE-79]
severity: high
last_verified: 2026-10-29
aliases: [reflected-xss, stored-xss, javascript-injection]
---

# Cross-Site Scripting (Reflected and Stored)

## Why It Is a Threat

Untrusted input is reflected (reflected XSS) or persisted and later rendered (stored XSS) into HTML/JS without encoding, executing attacker script in the victim's browser — session theft, account takeover, and actions on behalf of the user. The client-only variant is `dom-xss`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Reflected: inject a benign marker into parameters reflected in the response and confirm the execution context (HTML, attribute, JS, URL, CSS).
- Stored: submit a marker that is persisted and rendered to other users/admin views; confirm it executes there.
- Verify framework escaping is not bypassed via raw-HTML props.
- Source review by stack (`source-code-vapt`): Laravel `{!! $value !!}` and `new HtmlString($input)`; PHP `echo` of request values without `htmlspecialchars`; CodeIgniter views without `html_escape()` (3) or `esc()` (4); Django `mark_safe(`, `|safe`, `{% autoescape off %}`; Jinja2 `Environment()` without autoescape; React Native Web `dangerouslySetInnerHTML`; Rust Askama `|safe` and Maud `PreEscaped`.

## How to Fix

- Apply context-aware output encoding; rely on framework auto-escaping and avoid raw-HTML sinks.
- Sanitize rich text with a vetted allowlist (e.g. DOMPurify); deploy a strict, nonce/hash-based CSP as defense-in-depth; keep session cookies `HttpOnly`.
- Map to ASVS 5.0 V1.2.1 (context encoding), V1.3.1 (HTML sanitization), V3.2.2, and V3.4.3 (CSP), and the XSS Prevention Cheat Sheet.

## References

- [OWASP Cross Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
- [OWASP WSTG-INPV-01: Testing for Reflected Cross Site Scripting](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/01-Testing_for_Reflected_Cross_Site_Scripting/)
- [OWASP WSTG-INPV-02: Testing for Stored Cross Site Scripting](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/02-Testing_for_Stored_Cross_Site_Scripting/)
