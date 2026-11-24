---
id: missing-security-logging
title: Missing or Insufficient Security Logging
category: detection-ops
platforms: [shared, web, backend, android, ios]
owasp_refs: [A09:2025]
cwe: [CWE-778, CWE-223]
severity: medium
last_verified: 2026-10-29
aliases: [logs-omit-context]
---

# Missing or Insufficient Security Logging

## Why It Is a Threat

Security-relevant events (successful and failed logins, authorization failures, admin actions, high-value transactions, security-control failures) are not logged, or logs omit context (who, what, when, where), so attacks go undetected and incident response and forensics are crippled.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trigger auth failures, privilege changes, validation and business-logic bypass attempts, and sensitive actions; confirm they are logged with actor, action, target, source, and timestamp (no secrets).

## How to Fix

- Log security events in a structured, consistent format with sufficient context; synchronize clocks and use UTC or explicit offsets; document what is logged and where.
- Avoid logging secrets and PII (`sensitive-data-in-logs`). Map to ASVS 5.0 V16 Security Logging and Error Handling (V16.2 General Logging, V16.3 Security Events) and A09:2025.

## References

- [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html)
- [OWASP Logging Vocabulary Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Vocabulary_Cheat_Sheet.html)
- [CWE-778: Insufficient Logging](https://cwe.mitre.org/data/definitions/778.html)
- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
