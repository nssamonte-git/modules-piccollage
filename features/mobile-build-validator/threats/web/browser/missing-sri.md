---
id: missing-sri
title: Missing Subresource Integrity
category: web-browser
platforms: [web]
owasp_refs: [A08:2025, A02:2025]
cwe: [CWE-353, CWE-494]
severity: medium
last_verified: 2026-10-29
---

# Missing Subresource Integrity

## Why It Is a Threat

Third-party scripts and stylesheets loaded from CDNs without Subresource Integrity (SRI) hashes run whatever the CDN serves, so a compromised CDN or hijacked package silently injects malicious code into every page that loads it (`third-party-script-compromise`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory external `<script>` and `<link rel="stylesheet">` includes (and dynamically injected ones) and check for `integrity` plus `crossorigin`; flag unversioned "latest" URLs that cannot carry a stable hash.

## How to Fix

- Add SRI hashes (SHA-384 or stronger) and `crossorigin="anonymous"` to external scripts and styles; pin exact versions; self-host critical assets where SRI is impossible because the content changes.
- Combine with a strict CSP so only expected sources load.
- Map to ASVS 5.0 V3.6.1 External Resource Integrity and the Third Party JavaScript Management Cheat Sheet.

## References

- [MDN: Subresource Integrity](https://developer.mozilla.org/en-US/docs/Web/Security/Defenses/Subresource_Integrity)
- [W3C: Subresource Integrity](https://www.w3.org/TR/SRI/)
- [CWE-353: Missing Support for Integrity Check](https://cwe.mitre.org/data/definitions/353.html)
- [OWASP Third Party JavaScript Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Third_Party_Javascript_Management_Cheat_Sheet.html)
