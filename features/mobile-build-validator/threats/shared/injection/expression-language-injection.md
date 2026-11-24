---
id: expression-language-injection
title: Expression-Language Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025]
cwe: [CWE-917]
severity: high
last_verified: 2026-10-29
aliases: [el-injection, ognl-injection, spel-injection]
---

# Expression-Language Injection

## Why It Is a Threat

Untrusted input is evaluated by an expression language (OGNL, SpEL, Jakarta EL, MVEL, JEXL), often in Java frameworks, allowing attackers to invoke methods and reach remote code execution — the class behind several high-profile framework CVEs (e.g. Apache Struts OGNL flaws).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify inputs reaching expression evaluation (framework parameters, error and validation messages, rule engines, dynamic routing); probe with a benign expression and observe evaluation.
- Confirm evaluation only; RCE escalation requires explicit approval.

## How to Fix

- Do not evaluate user input as expressions; keep frameworks patched to versions that remove or restrict dangerous evaluation (ASVS 5.0 V1.3 Sanitization, requirement 1.3.2).
- Where expressions are required, use a restricted evaluator (e.g. SpEL `SimpleEvaluationContext` rather than `StandardEvaluationContext`) with an allowlist of properties and no type or method access.

## References

- [OWASP Community: Expression Language Injection](https://owasp.org/www-community/vulnerabilities/Expression_Language_Injection)
- [CWE-917: Improper Neutralization of Special Elements used in an Expression Language Statement ('Expression Language Injection')](https://cwe.mitre.org/data/definitions/917.html)
- [OWASP Top 10:2025 A05 Injection](https://top10.owasp.org/2025/A05_2025-Injection/)
