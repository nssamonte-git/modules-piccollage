---
id: incomplete-asset-inventory
title: Incomplete Asset Inventory / Shadow Systems
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [API9:2023, A09:2025]
cwe: []
severity: medium
last_verified: 2026-10-29
aliases: [shadow-apis, shadow-systems]
---

# Incomplete Asset Inventory / Shadow Systems

## Why It Is a Threat

You cannot protect what you do not know exists. Undocumented APIs (shadow and zombie endpoints, old API versions), forgotten services, and unmanaged systems run without patching, monitoring, or review, making them prime, invisible entry points. Cloud variant: `orphaned-cloud-resources`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Reconcile a live inventory of services, APIs, hosts, and third-party data flows against documentation; discover undocumented endpoints, non-production hosts, and old API versions (API9).

## How to Fix

- Maintain an authoritative, automatically updated asset and API inventory with owners, environments, and data sensitivity; retire shadow and zombie endpoints and old versions.
- Gate new services through an inventory and onboarding process. Map to API9:2023 and A09:2025.

## References

- [OWASP API9:2023 Improper Inventory Management](https://owasp.org/API-Security/editions/2023/en/0xa9-improper-inventory-management/)
- [OWASP Attack Surface Analysis Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Attack_Surface_Analysis_Cheat_Sheet.html)
