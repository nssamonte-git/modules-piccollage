---
id: xxe
title: XML External Entity (XXE)
category: injection
platforms: [shared, web, backend]
owasp_refs: [A02:2025, A05:2025, WSTG-INPV-07]
cwe: [CWE-611, CWE-776]
severity: high
last_verified: 2026-10-29
---

# XML External Entity (XXE)

## Why It Is a Threat

An XML parser processes DTDs and external entities in untrusted XML, letting an attacker read local files, perform SSRF, or cause denial of service through recursive entity expansion ("billion laughs"). Affects XML APIs, SOAP, SAML, SVG/Office uploads, RSS/XLIFF imports, and config imports. OWASP Top 10:2025 maps it to A02 Security Misconfiguration because the fix is parser configuration.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit XML with an external-entity probe pointing to an owner-approved out-of-band collector (OOB) or an error/file-read of a known-safe path; confirm the parser resolves it.
- Test indirect XML inputs: SVG, DOCX/XLSX, SAML, and JSON endpoints that also accept `Content-Type: application/xml`.
- Source review by stack (`source-code-vapt`): PHP `simplexml_load_string` or `DOMDocument::loadXML` with `LIBXML_NOENT`; Python `xml.etree`, `minidom`, `xml.sax`, or `lxml` on untrusted XML instead of `defusedxml`.

## How to Fix

- Disable DTD processing entirely, or at least external entities and external DTD loading, in every XML parser that handles untrusted input (ASVS 5.0 V1.5 Safe Deserialization, requirement 1.5.1). Defaults vary by parser and version (libxml2 2.9+ disables external entities by default; Java JAXP factories need explicit hardening), so configure each parser explicitly rather than relying on defaults.
- Prefer simpler formats (e.g. JSON) where possible; keep XML libraries patched and limit entity expansion.

## References

- [OWASP XML External Entity Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html)
- [CWE-611: Improper Restriction of XML External Entity Reference](https://cwe.mitre.org/data/definitions/611.html)
- [CWE-776: Improper Restriction of Recursive Entity References in DTDs ('XML Entity Expansion')](https://cwe.mitre.org/data/definitions/776.html)
- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
