---
id: cloud-audit-logging-gaps
title: Cloud Audit Logging Gaps
category: backend-cloud
platforms: [backend]
owasp_refs: [A09:2025]
cwe: [CWE-778, CWE-223]
severity: medium
last_verified: 2026-10-29
aliases: [cloudtrail-disabled, missing-cloud-audit-logs, cloud-control-plane-logging, disable-cloud-logs]
---

# Cloud Audit Logging Gaps

## Why It Is a Threat

Cloud audit logging gaps are when control-plane audit logs, such as AWS CloudTrail, Azure Activity Log and Microsoft Entra audit and sign-in logs, or Google Cloud Audit Logs, are disabled, cover only some regions, accounts, or event types, are kept only for the short default retention, or can be stopped or deleted by the same identities they record. An attacker who takes over a cloud identity then acts unseen, and investigators cannot tell what was accessed or changed. Attackers also stop or alter cloud logging deliberately (MITRE ATT&CK T1685.002 Disable or Modify Cloud Log, formerly T1562.008). This card covers cloud provider audit trails; application security events are in `missing-security-logging`, and tampering with logs in general is in `log-tampering`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate). Posture review with read-only access.

- AWS: confirm an organization or multi-Region trail exists, logging is on, log file integrity validation is enabled, data events are captured for sensitive buckets and functions, and the destination bucket sits in a separate, locked-down log account.
- Azure: Activity Log events are kept for 90 days by default; confirm diagnostic settings export subscription Activity Logs and Entra audit, sign-in, and non-interactive sign-in logs to a central workspace or storage with longer retention.
- Google Cloud: Admin Activity logs are always on, but Data Access logs are off by default for most services; confirm they are enabled where needed and that sinks route logs to a protected central project.
- Review who can stop trails, delete diagnostic settings, or change log sinks, and whether those actions raise alerts.

## How to Fix

- Enable organization-wide, all-region audit logging, including data-access events for sensitive resources, and send it to a dedicated logging account or project.
- Protect log storage with immutability or retention locks, encryption, and separate administrators; enable integrity validation where the provider offers it.
- Deny log-disabling actions with organization policies or service control policies, and alert on any stop, delete, or update of trails, diagnostic settings, or sinks (see `missing-alerting`).
- Set retention to meet investigation and regulatory needs, and test that responders can query the logs during an incident.

## References

- [AWS CloudTrail: Security best practices](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/best-practices-security.html)
- [Microsoft Learn: Activity Log in Azure Monitor](https://learn.microsoft.com/en-us/azure/azure-monitor/fundamentals/activity-log)
- [Google Cloud: Cloud Audit Logs overview](https://docs.cloud.google.com/logging/docs/audit)
- [MITRE ATT&CK T1685.002: Disable or Modify Cloud Log](https://attack.mitre.org/techniques/T1685/002/)
- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
