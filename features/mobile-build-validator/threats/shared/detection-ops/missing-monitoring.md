---
id: missing-monitoring
title: Missing Monitoring / Detection
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A09:2025]
cwe: [CWE-778, CWE-223]
severity: medium
last_verified: 2026-10-29
aliases: [no-centralized-monitoring, missing-intrusion-detection, missing-abuse-detection]
---

# Missing Monitoring / Detection

## Why It Is a Threat

Without centralized monitoring, intrusion detection, or abuse detection, attacks and misuse proceed unnoticed for long dwell times, turning containable incidents into major breaches.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check for centralized log aggregation or a SIEM, detection rules for auth abuse and anomalies, and coverage across app, infrastructure, cloud control-plane, and CI/CD logs.

## How to Fix

- Centralize logs into a SIEM with detection rules for auth failures, privilege changes, and abuse patterns; cover all tiers, including cloud audit (`cloud-audit-logging-gaps`) and CI/CD logs.
- Tune detections and review coverage regularly against known attacker techniques. Map to A09:2025 and ASVS 5.0 V16.4 (logs sent to a separate system for analysis and detection).

## References

- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
- [CISA: Best Practices for Event Logging and Threat Detection](https://www.cisa.gov/resources-tools/resources/best-practices-event-logging-and-threat-detection)
- [NIST SP 800-92: Guide to Computer Security Log Management](https://csrc.nist.gov/pubs/sp/800/92/final)
