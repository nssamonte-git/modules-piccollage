---
id: web-push-abuse
title: Web Push / Background-Sync Abuse
category: web-pwa
platforms: [web]
owasp_refs: [A06:2025]
cwe: [CWE-770, CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [background-sync-abuse, push-permission-abuse]
---

# Web Push / Background-Sync Abuse

## Why It Is a Threat

Coerced notification permission, a compromised script that subscribes users, or a leaked VAPID private key lets attackers send spam or phishing notifications that look system-level and come from your trusted origin. Background sync and push handlers run when the app is closed and can exfiltrate data or act with stale authorization, consuming resources and trust.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review push-permission prompting (is it gated on a user action?), where the VAPID private key is stored, how subscription endpoints are protected, notification content controls, and what data and actions background-sync and push handlers can reach.

## How to Fix

- Request notification permission only after a clear user action; keep the VAPID private key in server-side secret storage and rotate it if exposed; treat subscription endpoints as secrets bound to the user.
- Build notification content from server-side templates, rate-limit pushes, and have sync and push handlers re-check authorization with the current session before acting.
- Map to ASVS 5.0 V2.4 Anti-automation, V13.3 Secret Management, and V8 Authorization; see `spam-content-flooding`.

## References

- [MDN: Push API](https://developer.mozilla.org/en-US/docs/Web/API/Push_API)
- [W3C: Push API](https://www.w3.org/TR/push-api/)
- [RFC 8292: Voluntary Application Server Identification (VAPID) for Web Push](https://www.rfc-editor.org/rfc/rfc8292.html)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
