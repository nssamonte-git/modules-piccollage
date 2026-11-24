---
id: clickjacking
title: Clickjacking / UI Redressing
category: web-browser
platforms: [web]
owasp_refs: [A06:2025, A02:2025, WSTG-CLNT-09]
cwe: [CWE-1021]
severity: medium
last_verified: 2026-10-29
aliases: [ui-redressing, frame-overlay]
---

# Clickjacking / UI Redressing

## Why It Is a Threat

An attacker frames your site invisibly over their page and tricks the victim into clicking sensitive controls (confirm payment, change settings, grant OAuth consent). The click lands on your app with the victim's session if the browser sends the session cookie into the cross-site frame.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Load sensitive pages in an `<iframe>` from another origin and check whether they render; inspect every response for `Content-Security-Policy: frame-ancestors` and `X-Frame-Options`.
- Check the session cookie's `SameSite` value: `Lax` or `Strict` cookies are not sent in cross-site frames, which limits impact.

## How to Fix

- Send `Content-Security-Policy: frame-ancestors 'none'` (or `'self'` / an explicit allowlist) on every response; keep `X-Frame-Options: DENY` or `SAMEORIGIN` only as a fallback for old browsers, since CSP takes precedence.
- Set session cookies `SameSite=Lax` or `Strict`, and require re-authentication or explicit confirmation for high-risk actions.
- Map to ASVS 5.0 V3 Web Frontend Security (V3.4.6 frame-ancestors, V3.3.2 SameSite) and the Clickjacking Defense Cheat Sheet.

## References

- [OWASP Clickjacking Defense Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Clickjacking_Defense_Cheat_Sheet.html)
- [OWASP WSTG: Testing for Clickjacking (WSTG-CLNT-09)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/09-Clickjacking/)
- [CWE-1021: Improper Restriction of Rendered UI Layers or Frames](https://cwe.mitre.org/data/definitions/1021.html)
- [MDN: CSP frame-ancestors directive](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Content-Security-Policy/frame-ancestors)
