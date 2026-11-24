---
id: csv-formula-injection
title: CSV / Spreadsheet Formula Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025]
cwe: [CWE-1236]
severity: medium
last_verified: 2026-10-29
aliases: [formula-injection, csv-injection]
---

# CSV / Spreadsheet Formula Injection

## Why It Is a Threat

Exported data containing cells that begin with `=`, `+`, `-`, `@`, tab, or CR is interpreted as a formula when opened in Excel, LibreOffice, or Google Sheets. Formulas can pull in or send out data (e.g. web-request or hyperlink functions) and, on older or permissive desktop configurations, trigger command execution on the machine of the person who opens the export. Modern spreadsheet apps warn before external content runs, but users often click through.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit field values beginning with `=`, `+`, `-`, `@` (benign formulas such as simple arithmetic) that flow into CSV/XLSX exports; open the export in a spreadsheet app and observe whether the value is treated as a formula.

## How to Fix

- Escape export cells: when a field starts with `=`, `+`, `-`, `@`, tab, CR, or a null character, prefix it with a single quote, and quote fields per RFC 4180 (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.10).
- Apply the same escaping to XLSX/ODF exports, not just CSV; set correct content types. Validate input server-side, but treat export-time escaping as the control.

## References

- [OWASP WSTG (latest) WSTG-INJT-21: CSV Injection](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/07-Injection/21-CSV_Injection/)
- [OWASP Community: CSV Injection](https://owasp.org/www-community/attacks/CSV_Injection)
- [CWE-1236: Improper Neutralization of Formula Elements in a CSV File](https://cwe.mitre.org/data/definitions/1236.html)
