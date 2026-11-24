---
id: backup-snapshot-leakage
title: Backup or Database Snapshot Leakage
category: data-privacy
platforms: [shared, web, backend]
owasp_refs: [A02:2025, A04:2025]
cwe: [CWE-530, CWE-200, CWE-312]
severity: high
last_verified: 2026-10-29
---

# Backup or Database Snapshot Leakage

## Why It Is a Threat

Backups, database snapshots, dumps, and exports are stored unencrypted or in over-permissive locations (public buckets, snapshots shared publicly or with other cloud accounts, shared drives, `.bak`/`.sql` files left on a web server), so a single misconfiguration exposes the entire dataset. Backups often outlive deletion and retention rules and are a leading cause of large breaches. Related: `public-data-store`, `data-retention-violation`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory backups, snapshots, and dumps with their location, owner, encryption, and retention; check object-storage permissions and snapshot sharing settings (public or cross-account).
- Check web roots and artifact stores for downloadable backup or dump files, and verify who can restore or copy backups.

## How to Fix

- Encrypt backups and snapshots at rest with managed keys; keep them private-by-default with least-privilege access, ideally in a separate account or vault with immutable copies (ASVS 5.0 V14.1.2 protection levels and V11 Cryptography).
- Never store backups in the web root; apply retention to backups too; test restores regularly. Alert on public or over-shared backup resources (A02:2025).

## References

- [CWE-530: Exposure of Backup File to an Unauthorized Control Sphere](https://cwe.mitre.org/data/definitions/530.html)
- [NIST SP 800-209: Security Guidelines for Storage Infrastructure](https://csrc.nist.gov/pubs/sp/800/209/final)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
