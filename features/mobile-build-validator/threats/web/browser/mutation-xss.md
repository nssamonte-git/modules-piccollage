---
id: mutation-xss
title: Mutation XSS (mXSS)
category: web-browser
platforms: [web]
owasp_refs: [A05:2025, WSTG-CLNT-01]
cwe: [CWE-79]
severity: high
last_verified: 2026-10-29
aliases: [mxss]
---

# Mutation XSS (mXSS)

## Why It Is a Threat

HTML that a sanitizer judged safe is serialized and re-parsed by the browser, and the parser mutates it (namespace switches, malformed nesting, entity decoding) into executable markup. This bypasses sanitization and yields XSS in rich-text and HTML-sanitizing flows even when the sanitizer looked correct.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify where sanitized HTML is later concatenated, modified, or re-serialized before insertion, and check the sanitizer version against published bypass advisories.
- Test the sanitizer with known mXSS vector classes (namespace confusion, nesting) and confirm whether the serialized-then-reparsed output executes.

## How to Fix

- Use a well-maintained, mXSS-hardened sanitizer (for example DOMPurify) and keep it patched; sanitize as the last step and insert the result as-is, without string concatenation or another parse/serialize round trip.
- Prefer text or templated rendering over raw HTML; enforce Trusted Types so only sanitizer output reaches HTML sinks.
- Map to ASVS 5.0 V1.3.1 (HTML sanitization) and V3.2.2; see `dom-xss`, `xss`.

## References

- [OWASP Cross Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
- [OWASP DOM based XSS Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
