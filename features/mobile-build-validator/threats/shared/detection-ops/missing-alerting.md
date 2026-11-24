---
id: missing-alerting
title: Missing Alerting
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A09:2025]
cwe: [CWE-778]
severity: medium
last_verified: 2026-10-29
---

# Missing Alerting

## Why It Is a Threat

Even with logs and monitoring, the absence of timely alerts means no one acts on detected attacks until damage is done. Detection without notification does not reduce dwell time.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trigger high-signal events (repeated auth failures, privilege escalation, data export, a scheduled DAST or penetration test) and confirm an actionable alert reaches a responder within a defined SLA.

## How to Fix

- Define alerts for high-severity events with ownership, routing, playbooks, and response SLAs; integrate with on-call.
- Test alerts periodically, including that alerting itself is monitored. Map to A09:2025, ASVS 5.0 V16.4 (logs sent to a separate system for detection and alerting), and `delayed-incident-response`.

## References

- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
- [CWE-778: Insufficient Logging](https://cwe.mitre.org/data/definitions/778.html)
- [CISA: Best Practices for Event Logging and Threat Detection](https://www.cisa.gov/resources-tools/resources/best-practices-event-logging-and-threat-detection)
