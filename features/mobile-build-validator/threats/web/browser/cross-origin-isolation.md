---
id: cross-origin-isolation
title: Cross-Origin Isolation Failures
category: web-browser
platforms: [web]
owasp_refs: [A02:2025]
cwe: [CWE-668]
severity: low
last_verified: 2026-10-29
aliases: [missing-coop-corp]
---

# Cross-Origin Isolation Failures

## Why It Is a Threat

Without cross-origin policies (COOP, COEP, CORP) and request isolation, other sites can keep a window handle to your pages (cross-window leaks, XS-Leaks), embed your authenticated resources, and pull them into their own process where speculative-execution (Spectre-class) side channels can read them. Sensitive in-memory or cross-origin data is exposed even without a classic injection bug.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check document responses for `Cross-Origin-Opener-Policy`, and sensitive resources for `Cross-Origin-Resource-Policy`; if the app needs `SharedArrayBuffer` or high-resolution timers, check `Cross-Origin-Embedder-Policy` and `window.crossOriginIsolated`.
- Check whether authenticated resources can be loaded or embedded from another site (no `Sec-Fetch-*` validation or CORP).

## How to Fix

- Send `Cross-Origin-Opener-Policy: same-origin` (or `same-origin-allow-popups` where popups such as OAuth are required) on all HTML documents.
- Set `Cross-Origin-Resource-Policy: same-origin` or `same-site` on sensitive resources, and reject cross-site requests to authenticated resources by validating `Sec-Fetch-Site`/`Sec-Fetch-Dest`.
- Enable full isolation (`COEP: require-corp` or `credentialless`) only where needed, after checking embeds. Combine with CSP and `nosniff`. Map to ASVS 5.0 V3 Web Frontend Security (V3.4.8 COOP, V3.5.8 resource loading).

## References

- [MDN: Cross-Origin-Opener-Policy (COOP)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cross-Origin-Opener-Policy)
- [MDN: Cross-Origin-Resource-Policy (CORP)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cross-Origin-Resource-Policy)
- [MDN: Cross-Origin-Embedder-Policy (COEP)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cross-Origin-Embedder-Policy)
- [CWE-668: Exposure of Resource to Wrong Sphere](https://cwe.mitre.org/data/definitions/668.html)
