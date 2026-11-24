---
id: html-injection
title: HTML Injection
category: injection
platforms: [web]
owasp_refs: [A05:2025, WSTG-CLNT-03]
cwe: [CWE-80]
severity: medium
last_verified: 2026-10-29
aliases: [content-spoofing]
---

# HTML Injection

## Why It Is a Threat

Untrusted HTML is rendered without encoding. Even without script execution, an attacker can inject markup for content spoofing, fake login forms, and phishing or UI redress within a trusted page, or use unclosed (dangling) markup to leak nearby page content such as tokens to an external URL.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inject benign HTML tags (e.g. a marker `<b>`/`<form>`) into reflected or stored fields and observe whether they render as markup rather than text.

## How to Fix

- Encode output as text by default (`textContent`, framework auto-escaping); where HTML is required, sanitize against a strict allowlist.
- Treat as the same root cause as XSS and fix both together; a strict CSP (`form-action`, `img-src`) limits spoofed forms and dangling-markup leaks.
- Map to ASVS 5.0 V1.2.1, V1.3.1, and V3.2.2.

## References

- [OWASP WSTG: Testing for HTML Injection (WSTG-CLNT-03)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/03-HTML_Injection/)
- [CWE-80: Improper Neutralization of Script-Related HTML Tags in a Web Page (Basic XSS)](https://cwe.mitre.org/data/definitions/80.html)
- [OWASP: Content Spoofing](https://owasp.org/www-community/attacks/Content_Spoofing)
