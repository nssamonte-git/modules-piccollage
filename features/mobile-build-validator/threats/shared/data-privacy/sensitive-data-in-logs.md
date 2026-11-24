---
id: sensitive-data-in-logs
title: Sensitive Data in Logs, Analytics, and Crash Reports
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A09:2025, MASVS-STORAGE]
cwe: [CWE-532, CWE-200]
severity: medium
last_verified: 2026-10-29
aliases: [sensitive-data-in-analytics, sensitive-data-in-crash-reports]
---

# Sensitive Data in Logs, Analytics, and Crash Reports

## Why It Is a Threat

Sensitive values (credentials, tokens, PII, full requests) are written to application logs, mobile system logs, analytics events, or crash reports, then exposed to operators, third-party SDKs, log pipelines, and anyone with log or device access — a common, low-visibility leak that often outlives the data's retention period.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Exercise key flows and inspect server logs, device logs (Android logcat, iOS unified log), analytics payloads, and crash dumps for tokens, PII, request bodies, and headers.
- Review third-party analytics and crash SDKs for what they collect and where they send it.
- Source review by stack (`source-code-vapt`): Android `Log.d`/`Log.i`/Timber with tokens or bodies; iOS `NSLog`, `os_log` `%{public}@`, `Logger` `privacy: .public`; React Native `console.log`; Flutter `print`/`debugPrint`; Laravel `Log::info($request->all())`; Python logging of request bodies or headers; Rust `#[derive(Debug)]` on secrets and `#[tracing::instrument]` without `skip(...)`; Postgres `log_statement = 'all'` and `pg_stat_statements`.

## How to Fix

- Log by data protection level: redact or deny-list sensitive fields before logging, never log secrets, full auth headers, or full PII (ASVS 5.0 V16.2.5); restrict and protect log access (ASVS 5.0 V16.4.2).
- Strip debug logging from release builds and mark dynamic values as private in iOS logging; scrub crash reports and constrain analytics to non-sensitive events.

## References

- [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html)
- [CWE-532: Insertion of Sensitive Information into Log File](https://cwe.mitre.org/data/definitions/532.html)
- [Android Developers: Log Info Disclosure](https://developer.android.com/privacy-and-security/risks/log-info-disclosure)
- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
