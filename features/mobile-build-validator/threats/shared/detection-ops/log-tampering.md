---
id: log-tampering
title: Log Tampering or Deletion
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A09:2025]
cwe: [CWE-732, CWE-117, CWE-778]
severity: medium
last_verified: 2026-10-29
aliases: [log-deletion]
---

# Log Tampering or Deletion

## Why It Is a Threat

An attacker (or insider) who can modify, delete, or forge log entries, or disable logging, erases evidence, hides intrusion, and defeats detection and forensics. This is easiest when logs stay on the compromised host or are writable by the service that produces them.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review whether application and service accounts can modify or delete their own logs or change logging configuration; check for log-injection handling, tamper evidence, and near-real-time off-host shipping.

## How to Fix

- Ship logs to a logically separate system in near real time and store them append-only or immutable (WORM); restrict write, delete, and logging-configuration rights.
- Encode log data to prevent injection, enable integrity validation (e.g. digest or hash chaining), and alert on gaps or logging being disabled. Map to A09:2025 and ASVS 5.0 V16.4 Log Protection.

## References

- [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html)
- [MITRE ATT&CK T1070: Indicator Removal](https://attack.mitre.org/techniques/T1070/)
- [CAPEC-268: Audit Log Manipulation](https://capec.mitre.org/data/definitions/268.html)
- [AWS: Validating CloudTrail log file integrity](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-log-file-validation-intro.html)
