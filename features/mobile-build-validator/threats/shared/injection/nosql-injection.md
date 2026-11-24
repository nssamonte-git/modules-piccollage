---
id: nosql-injection
title: NoSQL Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-05]
cwe: [CWE-943]
severity: high
last_verified: 2026-10-29
---

# NoSQL Injection

## Why It Is a Threat

Untrusted input reaches a NoSQL query (e.g. MongoDB, CouchDB, Elasticsearch, Redis scripts) as operators or structure rather than data, enabling authentication bypass, query manipulation, and data disclosure — often via JSON bodies or bracketed query parameters that inject operator objects (`$ne`, `$gt`, `$regex`, `$where`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit operator objects where the app expects a scalar (e.g. `{"$ne": null}`, or `param[$ne]=` in query strings) and observe authorization or logic changes.
- Test `$where`/`$function`/server-side JavaScript sinks and array/object type confusion in query parameters.

## How to Fix

- Enforce expected types with schema validation server-side; reject objects and `$`-prefixed keys where scalars are expected (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.4; V15.3 requirement 15.3.5 for type checks).
- Use driver query builders that keep operators separate from user data, or strip `$`-prefixed operator keys from untrusted objects; disable server-side JavaScript evaluation where the database allows it.

## References

- [OWASP WSTG v4.2: Testing for NoSQL Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/05.6-Testing_for_NoSQL_Injection/)
- [CWE-943: Improper Neutralization of Special Elements in Data Query Logic](https://cwe.mitre.org/data/definitions/943.html)
- [OWASP Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Injection_Prevention_Cheat_Sheet.html)
