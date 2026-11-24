---
id: alert-fatigue
title: Alert Fatigue / Excessive False Positives
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A09:2025]
cwe: [CWE-778]
severity: low
last_verified: 2026-10-29
aliases: [excessive-false-positives]
---

# Alert Fatigue / Excessive False Positives

## Why It Is a Threat

Too many low-value or false-positive alerts desensitize responders, so real incidents are missed, recognized too late, or buried (a contributing factor in major breaches). Detection that is too noisy is effectively no detection.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review alert volume, false-positive rate, and time to triage; check whether high-severity alerts are distinguishable and acted upon, and whether each alert has a playbook.

## How to Fix

- Tune and prioritize detections; suppress or aggregate noise; reserve paging for high-confidence, high-severity events; add near-zero-noise signals such as honeytokens.
- Track alert precision and review use cases and playbooks periodically. Map to A09:2025.

## References

- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
- [CISA: Best Practices for Event Logging and Threat Detection](https://www.cisa.gov/resources-tools/resources/best-practices-event-logging-and-threat-detection)
