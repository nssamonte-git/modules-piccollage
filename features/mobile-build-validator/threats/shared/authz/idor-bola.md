---
id: idor-bola
title: Insecure Direct Object Reference / Broken Object Level Authorization
category: authz
platforms: [shared, web, backend, android, ios]
owasp_refs: [API1:2023, A01:2025, WSTG-ATHZ-04, WSTG-APIT-02]
cwe: [CWE-639]
severity: high
last_verified: 2026-10-29
aliases: [ownership-check-bypass, bola]
---

# Insecure Direct Object Reference / Broken Object Level Authorization

## Why It Is a Threat

The application exposes a reference to an object (id, filename, key) and enforces authorization weakly or not at all, so a user can access or modify another user's or tenant's object by changing the reference. It is ranked first in the OWASP API Security Top 10 2023, enabling cross-user data disclosure and tampering.

## How to Find or Test

Authorized testing only, with two synthetic accounts (A and B) (follow the `smx-ai-vapt` authorization gate).

- Enumerate object references across read, list, update, delete, export, and secondary/async endpoints.
- Authenticate as A and request B's object ids; a successful cross-user read or write confirms the flaw.
- Test indirect references too: ids leaked in earlier responses, predictable sequences, GraphQL node ids, and ids embedded in exports or webhooks.
- Source review by stack (`source-code-vapt`): Laravel `Model::find($id)` or `findOrFail` without a policy or user scope, and nested bindings without `scopeBindings()`; Django REST framework `get_queryset` not filtered by user; FastAPI lookups not scoped to the current user; Rust handlers loading by id without an owner check.

## How to Fix

- Enforce object-level authorization server-side on every access path, deny-by-default; never rely on the client hiding the reference.
- Scope every query by the authenticated principal/tenant (e.g. `where owner_id = current_user`), including background jobs, exports, and search.
- Prefer unguessable, non-sequential ids as defense-in-depth, but treat authorization — not obscurity — as the control. Map remediation to API1:2023 and ASVS 5.0 V8 Authorization (8.2.2).

## References

- [OWASP API1:2023 Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa1-broken-object-level-authorization/)
- [OWASP Insecure Direct Object Reference Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Insecure_Direct_Object_Reference_Prevention_Cheat_Sheet.html)
- [OWASP WSTG-ATHZ-04: Insecure Direct Object References](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/05-Authorization/04-Insecure_Direct_Object_References/)
- [CWE-639: Authorization Bypass Through User-Controlled Key](https://cwe.mitre.org/data/definitions/639.html)
