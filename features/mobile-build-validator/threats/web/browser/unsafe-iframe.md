---
id: unsafe-iframe
title: Unsafe Iframe Use / Sandbox Omission
category: web-browser
platforms: [web]
owasp_refs: [A02:2025, WSTG-CLNT-09]
cwe: [CWE-829, CWE-1021]
severity: medium
last_verified: 2026-10-29
aliases: [sandbox-attribute-bypass, missing-iframe-sandbox]
---

# Unsafe Iframe Use / Sandbox Omission

## Why It Is a Threat

Embedding untrusted content in an iframe without `sandbox`, or with `allow-scripts` and `allow-same-origin` together on same-origin content (which lets the frame remove its own sandbox), lets the framed content run script that can reach the parent, navigate the top window, open popups, or use powerful features, enabling XSS, redirects, and data theft.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review iframes that embed third-party or user content for `sandbox` tokens, the `allow` (Permissions Policy) attribute, and whether the content is same-origin with the app; test whether the frame can reach the parent or navigate the top window.

## How to Fix

- Sandbox untrusted iframes with the fewest `allow-*` tokens; never combine `allow-scripts` with `allow-same-origin` for content served from your own origin; avoid `allow-top-navigation`.
- Serve user content from a separate, sandboxed origin; grant only needed features via `allow`; restrict embeddable sources with CSP `frame-src`.
- Map to ASVS 5.0 V3.5.4 (separate hostnames) and V3.4.3, and the HTML5 Security Cheat Sheet.

## References

- [OWASP HTML5 Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/HTML5_Security_Cheat_Sheet.html)
- [MDN: The iframe element](https://developer.mozilla.org/en-US/docs/Web/HTML/Reference/Elements/iframe)
- [CWE-829: Inclusion of Functionality from Untrusted Control Sphere](https://cwe.mitre.org/data/definitions/829.html)
