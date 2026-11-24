---
id: public-data-store
title: Publicly Exposed Databases and Buckets
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-732, CWE-668, CWE-306]
severity: critical
last_verified: 2026-10-29
aliases: [public-databases, public-object-storage-buckets, backup-files-in-public-directories]
---

# Publicly Exposed Databases and Buckets

## Why It Is a Threat

Databases, search clusters, object-storage buckets, or backup files are readable or writable by anyone because of misconfiguration (public ACLs or policies, anonymous access, internet-exposed ports), exposing entire datasets. One of the most common causes of large breaches. Backup specifics: `backup-snapshot-leakage`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review storage and database access policies, ACLs, and account-level public-access settings for anonymous or public access; check for unauthenticated database ports and for backups or dumps in web-reachable paths.

## How to Fix

- Keep data stores private by default with least-privilege access; enable account-level public-access blocks (e.g. S3 Block Public Access) and require authentication on every database.
- Continuously scan for public exposure and alert. Map to A02:2025, ASVS 5.0 V14 Data Protection, and the cloud IAM review.

## References

- [AWS: Blocking public access to your Amazon S3 storage](https://docs.aws.amazon.com/AmazonS3/latest/userguide/access-control-block-public-access.html)
- [OWASP WSTG: Test Cloud Storage](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/11-Cloud_Storage)
- [MITRE ATT&CK T1530: Data from Cloud Storage](https://attack.mitre.org/techniques/T1530/)
- [OWASP Database Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Database_Security_Cheat_Sheet.html)
