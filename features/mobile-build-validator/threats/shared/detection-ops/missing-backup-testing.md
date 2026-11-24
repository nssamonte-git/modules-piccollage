---
id: missing-backup-testing
title: Missing Backup Testing
category: detection-ops
platforms: [shared, backend]
owasp_refs: [A09:2025]
cwe: []
severity: medium
last_verified: 2026-10-29
---

# Missing Backup Testing

## Why It Is a Threat

Backups exist but restores are never tested, so during a real incident (ransomware, data loss) they turn out to be incomplete, corrupt, or unrestorable, and the failure is discovered exactly when recovery is critical.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review backup coverage, frequency, retention, and whether restore drills are performed and time-to-restore is measured against targets.

## How to Fix

- Schedule regular restore tests to a clean, isolated environment; verify integrity and completeness; track recovery time and recovery point objectives (RTO/RPO).
- Keep offline or immutable copies (`compromised-backups`, `ransomware`). Map to the backups and runbook guidance and NIST SP 800-34 contingency planning.

## References

- [CISA #StopRansomware Guide](https://www.cisa.gov/stopransomware/ransomware-guide)
- [NIST SP 800-34 Rev. 1: Contingency Planning Guide for Federal Information Systems](https://csrc.nist.gov/pubs/sp/800/34/r1/upd1/final)
- [NIST SP 800-184: Guide for Cybersecurity Event Recovery](https://csrc.nist.gov/pubs/sp/800/184/final)
