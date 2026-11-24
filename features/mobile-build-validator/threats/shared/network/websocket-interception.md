---
id: websocket-interception
title: WebSocket Interception and Hijacking
category: network
platforms: [shared, web, backend]
owasp_refs: [WSTG-CLNT-10, A01:2025, A04:2025]
cwe: [CWE-1385, CWE-346]
severity: medium
last_verified: 2026-10-29
aliases: [cross-site-websocket-hijacking]
---

# WebSocket Interception and Hijacking

## Why It Is a Threat

WebSocket connections over cleartext (`ws://`) can be intercepted, and connections that authenticate only via cookies are vulnerable to cross-site WebSocket hijacking (CSWSH) — an attacker page opens an authenticated socket and exfiltrates data or sends actions.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check for `ws://` usage; from a different origin, attempt to open an authenticated WebSocket and confirm whether the `Origin` is validated and a CSRF-style token is required.
- Confirm each message is authorized and validated, not just the handshake.

## How to Fix

- Use `wss://` only; check the handshake `Origin` against an allowlist and require a per-connection token obtained through the authenticated HTTPS session (not just ambient cookies).
- Authorize and validate each message server-side and limit message size and rate. Map to ASVS 5.0 V4 API and Web Service (4.4.1–4.4.4).

## References

- [OWASP WebSocket Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/WebSocket_Security_Cheat_Sheet.html)
- [OWASP WSTG-CLNT-10: WebSockets](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/10-WebSockets/)
- [CWE-1385: Missing Origin Validation in WebSockets](https://cwe.mitre.org/data/definitions/1385.html)
- [RFC 6455: The WebSocket Protocol](https://www.rfc-editor.org/rfc/rfc6455)
