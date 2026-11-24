---
id: svg-script-execution
title: SVG Script Execution
category: web-files
platforms: [web]
owasp_refs: [A05:2025, WSTG-BUSL-09]
cwe: [CWE-79, CWE-611]
severity: high
last_verified: 2026-10-29
---

# SVG Script Execution

## Why It Is a Threat

SVG is XML that can contain `<script>`, event handlers, `<foreignObject>` HTML, and external references. SVGs shown through `<img>` do not run script, but a user-uploaded SVG opened directly, embedded via `<object>`/`<iframe>`, or inlined into the page on the app origin executes as stored XSS. Server-side SVG processing can also expose XXE. A common bypass of image-upload assumptions.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Upload an SVG containing a benign script/event handler and confirm whether it executes when opened directly or embedded; check whether SVGs are served inline on the app origin and with which `Content-Type`, CSP, and `Content-Disposition`.

## How to Fix

- Sanitize SVGs to an allowlist of drawing elements and attributes (no scripts, handlers, `foreignObject`, or external references) or rasterize on upload.
- Serve user files from a separate origin with `Content-Disposition: attachment`, `nosniff`, and a restrictive CSP; disable external entities in any XML parser that handles SVG.
- Map to ASVS 5.0 V1.3.4 (SVG sanitization), V1.5.1 (XXE), and V3.2.1; see `xss`, `malicious-file-upload`.

## References

- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
- [OWASP WSTG: Test Upload of Malicious Files (WSTG-BUSL-09)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/09-Upload_of_Malicious_Files/)
- [OWASP XML External Entity Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html)
- [CWE-611: Improper Restriction of XML External Entity Reference](https://cwe.mitre.org/data/definitions/611.html)
