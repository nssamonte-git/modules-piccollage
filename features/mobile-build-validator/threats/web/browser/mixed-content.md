---
id: mixed-content
title: Mixed-Content Loading
category: web-browser
platforms: [web]
owasp_refs: [A04:2025, WSTG-CRYP-03]
cwe: [CWE-319]
severity: low
last_verified: 2026-10-29
---

# Mixed-Content Loading

## Why It Is a Threat

An HTTPS page references sub-resources (scripts, styles, images, XHR/fetch, form targets) over HTTP, so a network attacker can read or tamper with them. Current browsers block "blockable" mixed content such as scripts and auto-upgrade "upgradable" media to HTTPS, so the remaining risk is in clients that do not apply these rules, insecure form actions and links (browsers may only warn), and content that breaks or leaks when it cannot be upgraded.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Audit pages for `http://` sub-resource and form-action references and for mixed-content console warnings; check whether HSTS is set.

## How to Fix

- Reference all sub-resources and form targets over HTTPS; add `Content-Security-Policy: upgrade-insecure-requests` as a safety net (the older `block-all-mixed-content` directive is deprecated).
- Send HSTS with a long `max-age` (and `includeSubDomains`, preload where possible).
- Map to ASVS 5.0 V12.2.1 and V3.4.1 (HSTS); see `attacker-in-the-middle`.

## References

- [MDN: Mixed content](https://developer.mozilla.org/en-US/docs/Web/Security/Mixed_content)
- [W3C: Mixed Content](https://www.w3.org/TR/mixed-content/)
- [OWASP WSTG: Testing for Sensitive Information Sent via Unencrypted Channels (WSTG-CRYP-03)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/09-Weak_Cryptography/03-Sensitive_Information_Sent_via_Unencrypted_Channels/)
- [CWE-319: Cleartext Transmission of Sensitive Information](https://cwe.mitre.org/data/definitions/319.html)
