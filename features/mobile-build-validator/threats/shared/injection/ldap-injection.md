---
id: ldap-injection
title: LDAP Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-06]
cwe: [CWE-90]
severity: medium
last_verified: 2026-10-29
---

# LDAP Injection

## Why It Is a Threat

Untrusted input is placed into an LDAP search filter or distinguished name (DN) without escaping, letting an attacker alter the query to bypass authentication, enumerate directory entries, or disclose attributes (e.g. injecting `*` or boolean filter logic).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- In login, search, and user-lookup features backed by LDAP or Active Directory, submit filter metacharacters (`*`, `(`, `)`, `&`, `|`) and observe authentication or result-set changes.

## How to Fix

- Escape untrusted input with the correct encoder for its position: filter escaping for search filters and DN escaping for distinguished names; prefer framework APIs that build filters safely (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.6).
- Validate input against an allowlist, bind with least-privilege service accounts, and never authenticate by searching with a user-supplied filter alone.

## References

- [OWASP LDAP Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/LDAP_Injection_Prevention_Cheat_Sheet.html)
- [OWASP WSTG v4.2 WSTG-INPV-06: Testing for LDAP Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/06-Testing_for_LDAP_Injection/)
- [CWE-90: Improper Neutralization of Special Elements used in an LDAP Query ('LDAP Injection')](https://cwe.mitre.org/data/definitions/90.html)
