---
id: http-parameter-pollution
title: HTTP Parameter Pollution
category: web-url
platforms: [web]
owasp_refs: [A05:2025, WSTG-INPV-04]
cwe: [CWE-235]
severity: medium
last_verified: 2026-10-29
aliases: [duplicate-parameter-attack, hpp]
---

# HTTP Parameter Pollution

## Why It Is a Threat

Supplying a parameter multiple times, or in more than one place (query, body, cookie), causes components to pick different occurrences (first, last, concatenated, array), letting an attacker bypass validation or WAFs, tamper with logic, or smuggle values past one layer that another layer uses. Server-side HPP also injects extra parameters into back-end API calls built from user input.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send duplicate parameters (query, body, and mixed) and observe which value each layer uses; test validation/authorization bypass via the discrepancy, and encoded delimiters (`%26`) that inject parameters into back-end requests.

## How to Fix

- Define and enforce consistent handling of duplicate parameters across proxy, framework, and app; reject unexpected duplicates; read each parameter from its expected source only.
- Validate after canonical parsing, and build back-end query strings with a URL-encoding library rather than string concatenation.
- Map to ASVS 5.0 V15.3.7 (HTTP parameter pollution) and V1.2.2.

## References

- [OWASP WSTG-INPV-04: Testing for HTTP Parameter Pollution](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/04-Testing_for_HTTP_Parameter_Pollution/)
- [CWE-235: Improper Handling of Extra Parameters](https://cwe.mitre.org/data/definitions/235.html)
- [CAPEC-460: HTTP Parameter Pollution (HPP)](https://capec.mitre.org/data/definitions/460.html)
