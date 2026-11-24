---
id: log-injection
title: Log Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A09:2025, A05:2025]
cwe: [CWE-117]
severity: medium
last_verified: 2026-10-29
aliases: [log-forging]
---

# Log Injection

## Why It Is a Threat

Unsanitized input written to logs lets an attacker forge or split log entries (hiding activity, framing others, breaking log integrity) and, when logs are rendered or processed by a sink that interprets content, enables downstream attacks — script in web-based log viewers, or lookup evaluation inside the logging library itself (as in Log4Shell, CVE-2021-44228).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inject newlines, control sequences, and benign format/lookup tokens into fields that get logged (user name, User-Agent, headers); inspect how the log store and viewer render them.

## How to Fix

- Encode or neutralize CR/LF and control characters before logging, and prefer structured (key-value/JSON) logging that encodes values (ASVS 5.0 V16.4 Log Protection, requirement 16.4.1).
- Never evaluate or interpolate untrusted log data; keep logging libraries patched and disable message lookups. HTML-encode log data in any web-based viewer.

## References

- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
- [CWE-117: Improper Output Neutralization for Logs](https://cwe.mitre.org/data/definitions/117.html)
- [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html)
- [OWASP Community: Log Injection](https://owasp.org/www-community/attacks/Log_Injection)
