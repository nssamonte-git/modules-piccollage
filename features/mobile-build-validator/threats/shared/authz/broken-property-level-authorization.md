---
id: broken-property-level-authorization
title: Broken Object Property-Level Authorization
category: authz
platforms: [shared, web, backend]
owasp_refs: [API3:2023, A01:2025, WSTG-APIT-03]
cwe: [CWE-915, CWE-213]
severity: high
last_verified: 2026-10-29
aliases: [bopla, excessive-data-exposure]
---

# Broken Object Property-Level Authorization

## Why It Is a Threat

Authorization is enforced at the object level but not per property, so users read properties they should not see (excessive data exposure) or write properties they should not change (mass assignment) — e.g. reading another user's email, or setting `role`/`verified` on their own record. The backend instance of the write side is `backend-mass-assignment`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inspect responses for fields beyond what the UI shows (internal flags, other users' data, PII), including nested objects and GraphQL fields.
- Add unexpected writable fields to create/update requests and confirm whether they persist.
- Source review by stack (`source-code-vapt`): Laravel models returned directly without `$hidden` or API Resources; Django REST framework `fields = '__all__'`; FastAPI routes without `response_model`.

## How to Fix

- Serialize responses with an explicit field allowlist per role; bind input with an explicit allowlist (DTO/strong params/`$fillable`). Never rely on the client to filter returned data.
- Enforce sensitive properties via dedicated authorized flows, not generic object updates. Add tests for both read and write exposure. Map to API3:2023 and ASVS 5.0 V8 Authorization (8.2.3) and V15 Secure Coding and Architecture (15.3.1, 15.3.3).

## References

- [OWASP API3:2023 Broken Object Property Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa3-broken-object-property-level-authorization/)
- [OWASP WSTG-APIT-03: Excessive Data Exposure](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/12-API_Testing/03-Excessive_Data_Exposure/)
- [OWASP Mass Assignment Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Mass_Assignment_Cheat_Sheet.html)
- [CWE-915: Improperly Controlled Modification of Dynamically-Determined Object Attributes](https://cwe.mitre.org/data/definitions/915.html)
