---
id: api-versioning-security-drift
title: Inconsistent Security Across API Versions
category: backend-api-inventory
platforms: [backend]
owasp_refs: [API9:2023]
cwe: [CWE-693]
severity: high
last_verified: 2026-10-29
aliases: [old-api-versions, version-security-drift]
---

# Inconsistent Security Across API Versions

## Why It Is a Threat

Older or parallel API versions (`/v1` alongside `/v2`, beta or partner hosts) lack fixes, stronger authorization, or rate limits present in the current version, so attackers downgrade to the weakest version to bypass controls. Old versions often still reach the same production data, and undocumented versions also evade monitoring (`incomplete-asset-inventory`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Enumerate active API versions and hosts; compare authentication, authorization, validation, and rate limiting across versions; test whether old versions bypass new controls.

## How to Fix

- Keep an inventory of every exposed version and host, apply security controls uniformly across all live versions, and retire old versions on a published deprecation plan; monitor all of them.
- Centralize authentication and validation so fixes apply everywhere. Map to API9:2023 and ASVS 5.0 V6.3.4 (no undocumented pathways; consistent control strength).

## References

- [OWASP API9:2023 Improper Inventory Management](https://owasp.org/API-Security/editions/2023/en/0xa9-improper-inventory-management/)
- [OWASP Attack Surface Analysis Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Attack_Surface_Analysis_Cheat_Sheet.html)
