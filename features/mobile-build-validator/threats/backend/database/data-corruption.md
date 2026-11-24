---
id: data-corruption
title: Data Corruption / Replication Interception
category: backend-database
platforms: [backend]
owasp_refs: [A08:2025, A06:2025, A04:2025]
cwe: [CWE-345, CWE-319]
severity: medium
last_verified: 2026-10-29
aliases: [replication-channel-interception]
---

# Data Corruption / Replication Interception

## Why It Is a Threat

Integrity failures (missing constraints or validation, unsafe concurrent writes, partial or failed migrations) corrupt data and business state. Unencrypted database replication and backup channels can also be intercepted or tampered with in transit, leaking or altering replicated data. Related: `race-condition`, `unencrypted-data-in-transit`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review data-integrity controls (constraints, transactions, validation, migration safety) and whether replication and backup traffic is encrypted and mutually authenticated.

## How to Fix

- Enforce database constraints, transactions, and server-side validation; make writes atomic and idempotent; test migrations and keep verified, restorable backups.
- Encrypt and authenticate replication channels with TLS and certificate validation. Map to ASVS 5.0 V2.3 Business Logic Security (2.3.3), V15.4 Safe Concurrency, and V12.3 General Service to Service Communication Security; see `missing-backup-testing`.

## References

- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [OWASP Database Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Database_Security_Cheat_Sheet.html)
- [CWE-319: Cleartext Transmission of Sensitive Information](https://cwe.mitre.org/data/definitions/319.html)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
