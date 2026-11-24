---
id: unauthorized-message-publishing
title: Unauthorized Message Publishing / Consumer Impersonation
category: backend-messaging
platforms: [backend]
owasp_refs: [A01:2025, A06:2025]
cwe: [CWE-306, CWE-862]
severity: high
last_verified: 2026-10-29
aliases: [consumer-impersonation]
---

# Unauthorized Message Publishing / Consumer Impersonation

## Why It Is a Threat

A broker that does not authenticate and authorize producers and consumers lets an attacker publish messages that trigger privileged actions, or impersonate a consumer to read others' messages, abusing the trust services place in the bus. Common when the queue is assumed "internal" or shares one broad credential across services.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check broker authentication and per-topic or per-queue authorization for publish and subscribe; test whether an unauthorized client can publish to or read sensitive topics; look for shared or over-broad connection credentials.

## How to Fix

- Authenticate every producer and consumer with its own identity (mTLS, workload identity, or short-lived tokens) and authorize per topic or queue with least privilege; do not rely on network position.
- Sign and verify message provenance where actions are sensitive. Map to ASVS 5.0 V13.2 Backend Communication Configuration (13.2.1, 13.2.2); see `unauthenticated-service-to-service`.

## References

- [Microsoft Learn: Azure Service Bus authentication and authorization](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-authentication-and-authorization)
- [Amazon SQS security best practices](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-security-best-practices.html)
- [OWASP Microservices Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Microservices_Security_Cheat_Sheet.html)
- [CWE-862: Missing Authorization](https://cwe.mitre.org/data/definitions/862.html)
