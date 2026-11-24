---
id: orphaned-cloud-resources
title: Orphaned Cloud Resources / Shadow Infrastructure
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025]
cwe: [CWE-710]
severity: medium
last_verified: 2026-10-29
aliases: [shadow-infrastructure]
---

# Orphaned Cloud Resources / Shadow Infrastructure

## Why It Is a Threat

Forgotten or unmanaged resources (old instances, snapshots, buckets, test stacks, stale access keys, unused accounts) run unpatched and unmonitored, become easy entry points and sources of data leakage, and evade inventory-based defenses. Released resources that DNS still points to enable `dns-misconfig-subdomain-takeover`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Reconcile running resources in every account and region against the authoritative inventory and IaC state; flag unmanaged, untagged, or stale resources, snapshots, and credentials.

## How to Fix

- Maintain a complete asset inventory; tag owner and lifecycle; auto-expire test resources; decommission cleanly, including DNS records and credentials.
- Continuously scan for drift and shadow infrastructure. Map to A02:2025; see `incomplete-asset-inventory`.

## References

- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
- [OWASP Attack Surface Analysis Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Attack_Surface_Analysis_Cheat_Sheet.html)
- [OWASP API9:2023 Improper Inventory Management](https://owasp.org/API-Security/editions/2023/en/0xa9-improper-inventory-management/)
