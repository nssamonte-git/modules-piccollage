---
id: email-header-injection
title: Email-Header Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-10]
cwe: [CWE-93]
severity: medium
last_verified: 2026-10-29
aliases: [smtp-header-injection]
---

# Email-Header Injection

## Why It Is a Threat

CR/LF in user-supplied email fields (to, subject, from, reply-to, display name) lets an attacker inject extra headers or recipients — sending spam through the application, adding hidden BCC recipients to exfiltrate messages, or spoofing sender identity. Common in contact, invite, and notification forms; related IMAP/SMTP command injection affects webmail and mail-relay features.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inject a newline plus a header (e.g. an extra recipient) into email-driving inputs and observe whether additional headers or recipients are honored, using only owner-approved test mailboxes.

## How to Fix

- Reject CR/LF and control characters in any value used as an email header; validate addresses strictly (ASVS 5.0 V1.3 Sanitization, requirement 1.3.11 for SMTP/IMAP injection).
- Use a maintained mail library that sets headers through structured APIs and does not accept raw header strings; fix the sender and recipient server-side where possible.

## References

- [OWASP WSTG v4.2 WSTG-INPV-10: Testing for IMAP SMTP Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/10-Testing_for_IMAP_SMTP_Injection/)
- [CWE-93: Improper Neutralization of CRLF Sequences ('CRLF Injection')](https://cwe.mitre.org/data/definitions/93.html)
- [OWASP Top 10:2025 A05 Injection](https://top10.owasp.org/2025/A05_2025-Injection/)
