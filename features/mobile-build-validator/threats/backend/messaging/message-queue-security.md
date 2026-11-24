---
id: message-queue-security
title: Message-Queue Poisoning and Replay
category: backend-messaging
platforms: [backend]
owasp_refs: [A06:2025, A08:2025]
cwe: [CWE-20, CWE-345, CWE-294]
severity: high
last_verified: 2026-10-29
aliases: [message-queue-poisoning, poison-messages, message-replay, event-ordering-manipulation, dead-letter-queue-leakage]
---

# Message-Queue Poisoning and Replay

## Why It Is a Threat

Consumers often trust queue messages implicitly. Attackers or a compromised producer inject malicious or malformed messages (poison messages that crash or loop consumers), replay messages to repeat side effects, manipulate ordering, or read sensitive data parked in dead-letter queues, corrupting processing and state. Most brokers deliver at least once, so duplicates also occur without an attacker.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review whether consumers validate and authorize message content, handle malformed or poison messages safely, dedupe replays, and tolerate reordering; check DLQ access controls, encryption, and contents.

## How to Fix

- Validate and authorize every message as untrusted input; use idempotency keys and deduplication; cap redeliveries and route poison messages to a DLQ; do not assume ordering.
- Encrypt and restrict DLQ access, monitor it, and keep sensitive data out of messages. Map to ASVS 5.0 V2.2 Input Validation and V2.3 Business Logic Security; see `race-condition`, `replay-attack`.

## References

- [Amazon SQS: Using dead-letter queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)
- [Microsoft Learn: Azure Service Bus duplicate message detection](https://learn.microsoft.com/en-us/azure/service-bus-messaging/duplicate-detection)
- [Amazon SQS security best practices](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-security-best-practices.html)
- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
