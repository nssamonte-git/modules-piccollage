---
id: compromised-backups
title: Compromised Backups
category: detection-ops
platforms: [shared, backend]
owasp_refs: [A09:2025, A02:2025]
cwe: [CWE-312, CWE-284]
severity: high
last_verified: 2026-10-29
---

# Compromised Backups

## Why It Is a Threat

Attackers delete, encrypt, or steal backups (a standard ransomware step before encrypting production), or backups are exposed through weak protection, destroying the recovery path or leaking the full dataset. Exposure variant: `backup-snapshot-leakage`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review backup access controls, encryption, immutability, and isolation from production credentials and admin accounts; check whether a production compromise could reach, delete, or shorten retention of backups.

## How to Fix

- Isolate backups in a separate account or tenant with separate credentials; keep encrypted, immutable or offline copies (e.g. object lock or vault lock, following the 3-2-1 approach); alert on backup deletion, policy, or retention changes.
- Test restores regularly (`missing-backup-testing`). Map to A02:2025, ASVS 5.0 V14 Data Protection, and the backups runbook.

## References

- [CISA #StopRansomware Guide](https://www.cisa.gov/stopransomware/ransomware-guide)
- [MITRE ATT&CK T1490: Inhibit System Recovery](https://attack.mitre.org/techniques/T1490/)
- [AWS Backup Vault Lock](https://docs.aws.amazon.com/aws-backup/latest/devguide/vault-lock.html)
