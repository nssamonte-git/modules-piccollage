---
id: destructive-admin-actions
title: Destructive Administrative Actions
category: availability
platforms: [shared, web, backend]
owasp_refs: [A06:2025, A01:2025]
cwe: [CWE-269, CWE-250]
severity: high
last_verified: 2026-10-29
---

# Destructive Administrative Actions

## Why It Is a Threat

A compromised admin account, an insider, or an accident triggers mass deletion, resource teardown, or data wipes. Over-broad admin privileges and missing guardrails turn one action (or one phished admin) into an outage or permanent data loss.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review admin capabilities for destructive operations: are they least-privilege, confirmed, approved by a second person where high impact, rate-limited, reversible, and logged?

## How to Fix

- Apply least privilege and separation of duties; require multi-person approval for high-impact bulk actions; soft-delete with recovery windows and protect backups from the same admin role.
- Log and alert on destructive admin actions. Map to ASVS 5.0 V8 Authorization (8.2.1), ASVS 5.0 V2 Validation and Business Logic (2.3.5), and `detection-ops`.

## References

- [MITRE ATT&CK T1485: Data Destruction](https://attack.mitre.org/techniques/T1485/)
- [CWE-269: Improper Privilege Management](https://cwe.mitre.org/data/definitions/269.html)
- [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
