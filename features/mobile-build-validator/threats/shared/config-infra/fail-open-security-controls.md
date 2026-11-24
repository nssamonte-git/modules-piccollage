---
id: fail-open-security-controls
title: Fail-Open Security Controls
category: config-infra
platforms: [shared, web, backend, android, ios]
owasp_refs: [A10:2025, A01:2025]
cwe: [CWE-636, CWE-755]
severity: high
last_verified: 2026-10-29
aliases: [failing-open, fail-open, not-failing-securely, fail-open-authorization]
---

# Fail-Open Security Controls

## Why It Is a Threat

A security check fails open when it grants access or accepts input after it errors, times out, or reaches a state it did not expect, for example an authorization service outage treated as "allow", a signature or certificate verification exception treated as "valid", or a feature flag whose fallback value turns protection off. Attackers who can induce the failure (by overloading a dependency, sending malformed tokens, or triggering parser exceptions) bypass authentication, authorization, rate limiting, or integrity checks without breaking them directly. This card covers the allow-on-error decision itself; general exception and cleanup bugs are in `improper-exception-handling`, and insecure shipped settings are in `insecure-defaults`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review code for security decisions inside `try`/`catch` blocks or error callbacks: look for a catch path that returns `true`, `allow`, or a cached "last known good" grant, and for default branches in switch or policy evaluation that permit access.
- Check how each control behaves when its dependency is unavailable: policy engine, identity provider, token introspection, WAF or rate limiter, license or entitlement service, malware scanner, and remote feature-flag service. Compare the documented failure mode with the configured one.
- In an owner-approved test environment, simulate dependency timeouts or errors (fault injection, blocked egress) and confirm protected actions are denied, not allowed.
- Send malformed, truncated, or oversized tokens, certificates, and signed payloads and confirm the verifier rejects them rather than skipping validation.
- On mobile, confirm that certificate-pinning or integrity-check errors block the sensitive action instead of falling back to an unprotected path.

## How to Fix

- Default to deny: every security decision returns "deny" unless an explicit success path is reached; treat exceptions, timeouts, and unknown states as failures (ASVS 5.0 V8 Authorization, V16 Security Logging and Error Handling).
- Make verification functions return only explicit success; never convert a verification exception into a valid result (ASVS 5.0 V9 Self-contained Tokens, V11 Cryptography).
- Give remote flags and policy clients a secure fallback value, bounded timeouts, and circuit breakers whose open state blocks rather than bypasses the protected action.
- Log and alert on control failures and fail-open overrides so an induced outage is visible (see `missing-alerting`).
- Where availability needs a degraded mode, document it, limit it to low-risk operations, and require an explicit, audited decision to enable it.

## References

- [OWASP Top 10:2025 A10 Mishandling of Exceptional Conditions](https://top10.owasp.org/2025/A10_2025-Mishandling_of_Exceptional_Conditions/)
- [CWE-636: Not Failing Securely ('Failing Open')](https://cwe.mitre.org/data/definitions/636.html)
- [CWE-755: Improper Handling of Exceptional Conditions](https://cwe.mitre.org/data/definitions/755.html)
- [OWASP Error Handling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Error_Handling_Cheat_Sheet.html)
