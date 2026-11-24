---
id: tabnabbing
title: Tabnabbing / Reverse Tabnabbing
category: web-browser
platforms: [web]
owasp_refs: [A06:2025, WSTG-CLNT-14]
cwe: [CWE-1022]
severity: medium
last_verified: 2026-10-29
aliases: [reverse-tabnabbing]
---

# Tabnabbing / Reverse Tabnabbing

## Why It Is a Threat

A page opened from your site that keeps a `window.opener` reference can navigate your original tab to a phishing page while the user is looking at the new tab (reverse tabnabbing). Current browsers imply `noopener` for `target="_blank"` links, so the remaining exposure is `window.open()` without `noopener`, explicit `rel="opener"`, and older browsers or embedded webviews.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Find user-controllable or external links and `window.open()` calls; check for `rel="noopener"`, the `noopener` window feature, or `rel="opener"`, and confirm whether `window.opener` is non-null in the opened page.

## How to Fix

- Add `rel="noopener noreferrer"` to external and user-supplied links, and pass `noopener` to `window.open()`; never set `rel="opener"` for untrusted destinations.
- Send `Cross-Origin-Opener-Policy: same-origin` (or `same-origin-allow-popups`) so cross-origin pages cannot keep a handle to yours.
- Map to ASVS 5.0 V3.4.8 (COOP) and the HTML5 Security Cheat Sheet.

## References

- [OWASP WSTG: Testing for Reverse Tabnabbing (WSTG-CLNT-14)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/14-Reverse_Tabnabbing/)
- [CWE-1022: Use of Web Link to Untrusted Target with window.opener Access](https://cwe.mitre.org/data/definitions/1022.html)
- [MDN: rel="noopener"](https://developer.mozilla.org/en-US/docs/Web/HTML/Reference/Attributes/rel/noopener)
- [OWASP: Reverse Tabnabbing](https://owasp.org/www-community/attacks/Reverse_Tabnabbing)
