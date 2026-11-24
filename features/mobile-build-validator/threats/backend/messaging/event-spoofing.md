---
id: event-spoofing
title: Event Spoofing
category: backend-messaging
platforms: [backend]
owasp_refs: [A08:2025, A06:2025]
cwe: [CWE-345, CWE-290]
severity: high
last_verified: 2026-10-29
---

# Event Spoofing

## Why It Is a Threat

In event-driven systems, consumers act on events as facts. If events are not authenticated, an attacker who can reach the bus or the consumer endpoint forges events ("payment.completed", "user.verified") to drive unauthorized state changes and side effects across services.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Determine whether consumers verify event source and signature; in a test environment, inject a forged event and observe whether downstream actions execute.

## How to Fix

- Sign events or use authenticated delivery, and verify signature and source at the consumer (for example SNS message signatures or Event Grid Entra-authenticated delivery); authorize the action the event implies, not just its presence.
- Treat event payloads as untrusted and validate them. Map to ASVS 5.0 V4.1 Generic Web Service Security (4.1.5) and V2.2 Input Validation; see `webhook-security`, `unauthorized-message-publishing`.

## References

- [Amazon SNS: Verifying the signatures of Amazon SNS messages](https://docs.aws.amazon.com/sns/latest/dg/sns-verify-signature-of-message.html)
- [Microsoft Learn: Authenticate event delivery in Azure Event Grid](https://learn.microsoft.com/en-us/azure/event-grid/security-authentication)
- [OWASP Microservices Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Microservices_Security_Cheat_Sheet.html)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
