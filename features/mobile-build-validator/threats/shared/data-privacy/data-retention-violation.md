---
id: data-retention-violation
title: Data-Retention Violation / Missing Secure Deletion
category: data-privacy
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-212, CWE-459]
severity: medium
last_verified: 2026-10-29
aliases: [missing-secure-deletion]
---

# Data-Retention Violation / Missing Secure Deletion

## Why It Is a Threat

Data is kept longer than allowed or never truly deleted (soft-deletes, lingering backups, caches, logs, search indexes, analytics stores, third-party copies), violating retention obligations and leaving sensitive data exposed to later breach or legal request. Insufficient deletion of user data is one of the OWASP Top 10 Privacy Risks.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the retention policy against reality; trace deletion of a test record through the primary store, replicas, backups, caches, search indexes, logs, and third parties.
- Verify that account-deletion and right-to-erasure flows actually remove or irreversibly anonymize the data.

## How to Fix

- Classify data and define retention per class; enforce TTLs and scheduled deletion (ASVS 5.0 V14.2.7); implement verifiable deletion across all stores and downstream processors.
- For backups and media, use expiry or crypto-shredding (destroy the key) and sanitize retired storage. Honor erasure requests and document deletion under applicable privacy law.

## References

- [OWASP Top 10 Privacy Risks (P6 Insufficient Deletion of User Data)](https://owasp.org/www-project-top-10-privacy-risks/)
- [NIST SP 800-88 Rev. 2: Guidelines for Media Sanitization](https://csrc.nist.gov/pubs/sp/800/88/r2/final)
- [CWE-459: Incomplete Cleanup](https://cwe.mitre.org/data/definitions/459.html)
