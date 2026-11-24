---
id: backend-mass-assignment
title: Mass Assignment / Over-Posting
platforms: [backend]
owasp_refs: [API3:2023, A08:2025]
cwe: [CWE-915]
severity: high
last_verified: 2026-10-29
---

# Mass Assignment / Over-Posting

## Why It Is a Threat

The framework binds client-supplied fields directly to internal model attributes, so an attacker can set fields the UI never exposes (`role`, `is_admin`, `verified`, `balance`, `owner_id`, `tenant_id`) by adding them to the request body. This yields privilege escalation, ownership or tenant takeover, and integrity violations.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with synthetic accounts.

- Diff the response object against the documented or editable model to spot writable internal fields.
- Add unexpected fields (`role`, `isAdmin`, `verified`, `ownerId`, `price`) to create and update requests and confirm whether the value is accepted and persisted.
- Test nested or relationship payloads and JSON that the client form does not normally send.
- Source review by stack (`source-code-vapt`): Laravel `$guarded = []`, `Model::unguard()`, `create($request->all())`; PHP `extract($_POST)`; CodeIgniter 4 wide `$allowedFields` with `insert($this->request->getPost())`; Django REST framework `fields = '__all__'`; FastAPI one Pydantic model for input and database writes.

## How to Fix

- Bind only an explicit allowlist of fields per action (DTOs, `$fillable`, serializer `fields`, strong params); never bind the raw request to the model.
- Set sensitive fields (role, tenant, ownership, money) server-side through dedicated, authorized flows, not user-supplied input.
- Add regression tests asserting protected fields cannot be set via the API. Map to API3:2023 and ASVS 5.0 V15.3 Defensive Coding (15.3.3) and V8.2 (8.2.3).

## References

- [OWASP Mass Assignment Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Mass_Assignment_Cheat_Sheet.html)
- [OWASP API3:2023 Broken Object Property Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa3-broken-object-property-level-authorization/)
- [CWE-915: Improperly Controlled Modification of Dynamically-Determined Object Attributes](https://cwe.mitre.org/data/definitions/915.html)
