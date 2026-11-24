---
id: unsafe-postmessage
title: Unsafe postMessage Handling
category: web-browser
platforms: [web]
owasp_refs: [A08:2025, A05:2025, WSTG-CLNT-11]
cwe: [CWE-346, CWE-345]
severity: medium
last_verified: 2026-10-29
aliases: [missing-origin-validation]
---

# Unsafe postMessage Handling

## Why It Is a Threat

A `message` event handler that does not validate the sender's `origin` lets any page or frame send data the app trusts, leading to XSS, data theft, or action injection. A sender that uses `targetOrigin: '*'` can leak its message to whatever page currently occupies the target window.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review `addEventListener('message', ...)` handlers for exact `event.origin` checks (flag `indexOf`, `startsWith`, or loose regex checks) and for how `event.data` reaches sinks; review `postMessage` calls for `'*'` target origins.
- Send cross-origin messages from a test page and observe handling.

## How to Fix

- Compare `event.origin` exactly against an allowlist in every handler, validate the message structure, and discard anything else; never pass `event.data` to HTML, script, or URL sinks.
- Always send with an explicit `targetOrigin`, never `'*'`, for sensitive data.
- Map to ASVS 5.0 V3.5.5 and the HTML5 Security Cheat Sheet; see `client-side-security`.

## References

- [OWASP WSTG: Testing Web Messaging (WSTG-CLNT-11)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/11-Web_Messaging/)
- [MDN: Window.postMessage()](https://developer.mozilla.org/en-US/docs/Web/API/Window/postMessage)
- [CWE-346: Origin Validation Error](https://cwe.mitre.org/data/definitions/346.html)
- [OWASP HTML5 Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/HTML5_Security_Cheat_Sheet.html)
