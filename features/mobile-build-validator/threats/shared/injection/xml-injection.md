---
id: xml-injection
title: XML Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-07]
cwe: [CWE-91]
severity: medium
last_verified: 2026-10-29
---

# XML Injection

## Why It Is a Threat

Untrusted input is inserted into XML content or structure without encoding, letting an attacker add or alter elements and attributes — tampering with data, bypassing validation, or poisoning downstream consumers such as SOAP services and SAML or payment messages. For external-entity attacks see `xxe`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inject XML metacharacters (`<`, `>`, `&`, `'`, `"`, CDATA delimiters, extra elements) into fields serialized to XML; observe whether structure changes are accepted or reach downstream systems.

## How to Fix

- Build XML with a safe DOM/streaming API rather than string concatenation, and encode user data for its XML context (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.1).
- Validate inbound XML against a strict local schema (XSD) and reject malformed or unexpected structure server-side; configure parsers securely (see `xxe`).

## References

- [OWASP WSTG v4.2 WSTG-INPV-07: Testing for XML Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/07-Testing_for_XML_Injection/)
- [CWE-91: XML Injection (aka Blind XPath Injection)](https://cwe.mitre.org/data/definitions/91.html)
- [OWASP XML Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/XML_Security_Cheat_Sheet.html)
