---
id: insider-threats
title: Insider Threats / Privileged-User Abuse
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A01:2025, A09:2025]
cwe: [CWE-269, CWE-284]
severity: high
last_verified: 2026-10-29
aliases: [privileged-user-abuse, malicious-insider]
---

# Insider Threats / Privileged-User Abuse

## Why It Is a Threat

Employees, contractors, or admins with legitimate access misuse it: stealing data, abusing privileges, or sabotaging systems. Hard to detect because actions are authorized. Overlaps `fraud-legitimate-requests` and `destructive-admin-actions`.

## How to Find or Test

Authorized testing only (controls review; follow the `smx-ai-vapt` authorization gate).

- Review least privilege, separation of duties, access reviews, offboarding, and monitoring and audit of privileged actions and bulk data access.

## How to Fix

- Enforce least privilege, separation of duties, and just-in-time access; log and alert on privileged and bulk-data actions to a store the actor cannot modify; run periodic access reviews and prompt offboarding.
- Require approvals for sensitive actions. Map to A09:2025, ASVS 5.0 V8 Authorization, and V16.3 (log authorization decisions and sensitive data access).

## References

- [CISA: Insider Threat Mitigation](https://www.cisa.gov/topics/physical-security/insider-threat-mitigation)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html)
