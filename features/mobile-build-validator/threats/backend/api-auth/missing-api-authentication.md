---
id: missing-api-authentication
title: Missing API Authentication
category: backend-api-auth
platforms: [backend]
owasp_refs: [API2:2023, A07:2025]
cwe: [CWE-306]
severity: critical
last_verified: 2026-10-29
aliases: [unprotected-internal-api]
---

# Missing API Authentication

## Why It Is a Threat

An endpoint exposes sensitive data or actions without requiring authentication. Typical cases are internal, admin, or microservice APIs assumed to be unreachable, new endpoints that skipped the auth middleware, and alternate routes (older versions, other HTTP methods, health or management endpoints) that bypass the protected path. Anyone who finds it has full access. Related: `fail-open-security-controls`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Enumerate endpoints (including internal, undocumented, older-version, and alternate-method routes) and call them without credentials; confirm each service enforces authentication itself, not only the gateway.
- Source review by stack (`source-code-vapt`): CodeIgniter 4 route groups without an auth filter; FastAPI routes without an authenticating `Depends(...)`; Rust axum routes added after `Router::layer` (prefer `route_layer`); PostgREST tables and functions granted to `db-anon-role`.

## How to Fix

- Require authentication by default (deny-by-default) on every route, with explicit, reviewed exceptions for public endpoints; enforce it in the service, not only at the edge.
- Authenticate service-to-service traffic with mTLS or short-lived service tokens (`unauthenticated-service-to-service`). Map to API2:2023 and ASVS 5.0 V6 Authentication and V13.2 Backend Communication Configuration.

## References

- [OWASP API2:2023 Broken Authentication](https://owasp.org/API-Security/editions/2023/en/0xa2-broken-authentication/)
- [CWE-306: Missing Authentication for Critical Functionality](https://cwe.mitre.org/data/definitions/306.html)
- [OWASP REST Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html)
- [OWASP Microservices Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Microservices_Security_Cheat_Sheet.html)
