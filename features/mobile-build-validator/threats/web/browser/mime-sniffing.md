---
id: mime-sniffing
title: MIME-Sniffing Attacks
category: web-browser
platforms: [web]
owasp_refs: [A02:2025, WSTG-CONF-14]
cwe: [CWE-430, CWE-79]
severity: medium
last_verified: 2026-10-29
aliases: [mime-type-confusion, content-type-sniffing]
---

# MIME-Sniffing Attacks

## Why It Is a Threat

When responses lack `X-Content-Type-Options: nosniff` or send a missing or wrong `Content-Type`, browsers guess (sniff) the type and may render user-uploaded or user-controlled content as HTML or run it as script, turning a benign-looking file or API response into stored XSS.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check that every response carries an accurate `Content-Type` (with `charset` for text types) and `X-Content-Type-Options: nosniff`.
- Upload or request content with a mismatched or missing type and check whether the browser renders it as markup or accepts it as a script or stylesheet.

## How to Fix

- Send an accurate `Content-Type` with charset and `X-Content-Type-Options: nosniff` on all responses; serve user files with safe types and `Content-Disposition: attachment` where they should not render.
- Host user uploads on a separate origin, and do not serve API or upload responses in a renderable context.
- Map to ASVS 5.0 V3.4.4 (`nosniff`), V4.1.1 (`Content-Type`), and V3.2.1; see `malicious-file-upload`.

## References

- [MDN: X-Content-Type-Options header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/X-Content-Type-Options)
- [OWASP WSTG: Test Other HTTP Security Header Misconfigurations (WSTG-CONF-14)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/14-Other_HTTP_Security_Header_Misconfigurations/)
- [OWASP HTTP Headers Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/HTTP_Headers_Cheat_Sheet.html)
- [CWE-430: Deployment of Wrong Handler](https://cwe.mitre.org/data/definitions/430.html)
