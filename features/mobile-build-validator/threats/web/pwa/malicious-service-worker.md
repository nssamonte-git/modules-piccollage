---
id: malicious-service-worker
title: Malicious Service-Worker Registration
category: web-pwa
platforms: [web]
owasp_refs: [A05:2025, A02:2025]
cwe: [CWE-79, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [service-worker-cache-poisoning, persistent-malicious-offline-content, excessive-service-worker-scope]
---

# Malicious Service-Worker Registration

## Why It Is a Threat

A service worker sits between the app and the network with broad scope and persistence. If an attacker registers or replaces one (via XSS plus a same-origin script they control, such as an uploaded file or a JSONP endpoint), they can intercept and modify requests, poison the offline cache, and persist malicious code that survives page reloads and logout, a durable, stealthy foothold.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review where and how service workers are registered, their scope, any `Service-Worker-Allowed` header, and whether any same-origin path can serve attacker-influenced JavaScript (uploads, JSONP, callbacks); test whether XSS could register or replace a worker (`xss`).

## How to Fix

- Register only a fixed worker script at the narrowest scope needed; do not widen scope with `Service-Worker-Allowed`; ensure no same-origin path serves user-controlled content as JavaScript; restrict `worker-src` in CSP and prevent the XSS that could register workers.
- Validate cached responses, version the worker, and on logout or incident send `Clear-Site-Data: "storage"` (which also unregisters service workers) or ship an unregistering worker.
- Map to ASVS 5.0 V3.4.3 (CSP), V3.5.6 (no JSONP), and V14.3.1 (clear client data).

## References

- [MDN: Service Worker API](https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API)
- [MDN: Service-Worker-Allowed header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Service-Worker-Allowed)
- [MDN: Clear-Site-Data header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Clear-Site-Data)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
