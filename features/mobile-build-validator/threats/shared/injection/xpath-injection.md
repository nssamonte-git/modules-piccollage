---
id: xpath-injection
title: XPath Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-09]
cwe: [CWE-643]
severity: medium
last_verified: 2026-10-29
---

# XPath Injection

## Why It Is a Threat

Untrusted input is concatenated into an XPath query over XML data, letting an attacker alter the query to bypass authentication or extract nodes they should not access (analogous to SQL injection for XML stores). XPath has no access controls within a document, so a successful injection can expose the whole document.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- In features that query XML via XPath (auth, lookups), submit XPath metacharacters (`'`, `"`, `or 1=1`, `]`) and observe authentication or result changes, including boolean-differential (blind) behaviour.

## How to Fix

- Use parameterized XPath (variable resolvers) or precompiled expressions with variable binding; never concatenate input (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.7).
- Validate input against an allowlist and constrain it to expected types.

## References

- [OWASP WSTG v4.2 WSTG-INPV-09: Testing for XPath Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/09-Testing_for_XPath_Injection/)
- [CWE-643: Improper Neutralization of Data within XPath Expressions ('XPath Injection')](https://cwe.mitre.org/data/definitions/643.html)
- [OWASP Community: XPATH Injection](https://owasp.org/www-community/attacks/XPATH_Injection)
