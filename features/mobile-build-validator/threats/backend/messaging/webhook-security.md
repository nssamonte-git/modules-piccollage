---
id: webhook-security
title: Webhook Spoofing, Replay, and SSRF
category: backend-messaging
platforms: [backend]
owasp_refs: [API10:2023, API7:2023, A08:2025, A01:2025]
cwe: [CWE-345, CWE-918, CWE-294]
severity: high
last_verified: 2026-10-29
aliases: [webhook-spoofing, webhook-replay, missing-webhook-signature-validation]
---

# Webhook Spoofing, Replay, and SSRF

## Why It Is a Threat

Inbound webhooks without signature verification let attackers forge provider events (fake payment success, `payment-abuse`); without replay protection, captured webhooks are resent to repeat side effects. Outbound webhooks or callback URLs that fetch user-supplied destinations enable `ssrf`. Related: `unsafe-api-consumption`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), in a sandbox.

- Send unsigned, forged, and replayed webhook requests and confirm rejection; for outbound callbacks, test SSRF to internal or metadata targets only where the owner approves.
- Source review by stack (`source-code-vapt`): Laravel webhook routes without signature and timestamp checks, and signed routes missing `hasValidSignature()`.

## How to Fix

- Verify the provider's HMAC signature over the raw body with a constant-time comparison, check the timestamp, and reject stale or replayed deliveries (delivery id plus time window); make handlers idempotent.
- Allowlist and validate outbound callback URLs and block internal ranges (`ssrf`). Map to API10:2023, API7:2023, and ASVS 5.0 V1.3 Sanitization (1.3.6) and V4.1 (4.1.5).

## References

- [GitHub Docs: Validating webhook deliveries](https://docs.github.com/en/webhooks/using-webhooks/validating-webhook-deliveries)
- [OWASP API10:2023 Unsafe Consumption of APIs](https://owasp.org/API-Security/editions/2023/en/0xaa-unsafe-consumption-of-apis/)
- [OWASP API7:2023 Server Side Request Forgery](https://owasp.org/API-Security/editions/2023/en/0xa7-server-side-request-forgery/)
- [OWASP Server Side Request Forgery Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html)
