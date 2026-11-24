---
id: insecure-web-manifest
title: Insecure Web-App Manifest / Stale Cached Code
category: web-pwa
platforms: [web]
owasp_refs: [A02:2025, A03:2025]
cwe: [CWE-1104, CWE-1329]
severity: low
last_verified: 2026-10-29
aliases: [stale-vulnerable-cached-code]
---

# Insecure Web-App Manifest / Stale Cached Code

## Why It Is a Threat

A misconfigured web-app manifest widens attack surface: an over-broad `scope`, or `share_target`, `protocol_handlers`, and `file_handlers` entries that route untrusted input into the app without validation. Separately, aggressive service-worker caching can keep serving stale, vulnerable application code long after a security fix shipped, leaving installed users exploitable.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the manifest `scope`, `start_url`, and handler entries, and how the app validates data arriving through them.
- Review the service-worker cache strategy and HTTP caching of the worker script; check whether a security fix reaches already-installed clients promptly.

## How to Fix

- Scope the manifest minimally and treat share-target, protocol-handler, and file-handler input as untrusted (validate, encode, authorize).
- Version caches and delete old ones on `activate`; do not cache the service-worker script for long, and use `skipWaiting`/`clients.claim` deliberately so patches deploy; keep a kill switch (an updated worker, or `Clear-Site-Data`).
- Map to A03:2025 and ASVS 5.0 V2.2 Input Validation and V15.2.1 (remediation time frames); see `malicious-service-worker`.

## References

- [MDN: Web application manifest](https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Manifest)
- [W3C: Web Application Manifest](https://www.w3.org/TR/appmanifest/)
- [MDN: Service Worker API](https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API)
- [CWE-1329: Reliance on Component That is Not Updateable](https://cwe.mitre.org/data/definitions/1329.html)
