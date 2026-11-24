---
id: redos
title: Regular-Expression Denial of Service (ReDoS)
category: injection
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-1333, CWE-400]
severity: medium
last_verified: 2026-10-29
---

# Regular-Expression Denial of Service (ReDoS)

## Why It Is a Threat

A regex with catastrophic backtracking processes attacker-crafted input in exponential or high-polynomial time, pinning CPU and causing denial of service from a single small request. Single-threaded runtimes such as Node.js are hit hardest because one slow match blocks every request. Also tracked under availability (`availability` category).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify regexes applied to user input (validation, parsing, routing, WAF rules), including user-supplied patterns, with nested quantifiers or overlapping alternation; static regex analyzers help.
- Submit a crafted input and measure response-time growth with bounded input sizes and owner approval; confirm a CPU spike without overwhelming the service.

## How to Fix

- Rewrite vulnerable patterns to avoid nested or ambiguous quantifiers, or use a linear-time engine (e.g. RE2, Rust `regex`, .NET `RegexOptions.NonBacktracking`) (ASVS 5.0 V1.3 Sanitization, requirement 1.3.12).
- Bound input length before matching, set regex match timeouts where the engine supports them, and never compile user-supplied patterns without escaping (ASVS 5.0 V1.2 requirement 1.2.9).

## References

- [OWASP Community: Regular expression Denial of Service - ReDoS](https://owasp.org/www-community/attacks/Regular_expression_Denial_of_Service_-_ReDoS)
- [CWE-1333: Inefficient Regular Expression Complexity](https://cwe.mitre.org/data/definitions/1333.html)
- [OWASP Top 10:2025 A06 Insecure Design](https://top10.owasp.org/2025/A06_2025-Insecure_Design/)
