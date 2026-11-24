---
id: improper-exception-handling
title: Improper Exception Handling and Inconsistent State
category: config-infra
platforms: [shared, web, backend, android, ios]
owasp_refs: [A10:2025]
cwe: [CWE-755, CWE-390, CWE-460, CWE-772]
severity: medium
last_verified: 2026-10-29
aliases: [swallowed-exceptions, unhandled-exceptions, partial-transactions, mishandled-exceptional-conditions]
---

# Improper Exception Handling and Inconsistent State

## Why It Is a Threat

Improper exception handling means errors are unchecked, silently swallowed, or handled too late, so an abnormal condition leaves the application with half-finished transactions, corrupted state, or leaked resources such as file handles, locks, and connections. Attackers who can trigger the error path (network interruption mid-request, malformed input, quota limits) use it to get partial effects without the matching checks, for example a debit without a credit, a duplicated transfer, or a resource pool drained until the service stops responding. Leaking error details to clients is a separate issue covered by `verbose-errors`; a security check that allows access on error is covered by `fail-open-security-controls`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review code for empty or log-only `catch` blocks, ignored return codes, broad catches that continue normal flow, and missing `finally`/`using`/`defer` cleanup around files, locks, sockets, and database connections.
- Trace multi-step operations (payments, account changes, inventory, provisioning) and confirm each runs in one transaction or has a compensating rollback when any step fails.
- In an owner-approved environment, interrupt multi-step requests (drop the connection, inject dependency errors, exceed size limits) and compare persisted state with the expected all-or-nothing result.
- Repeat error-triggering requests at a modest, approved rate and watch for growing open handles, connections, or memory that are never released (see `resource-exhaustion`).
- Check that a global exception handler exists and that unhandled exceptions in async jobs and message consumers are logged, not lost.

## How to Fix

- Catch exceptions where they occur, handle them meaningfully, and keep a global handler as a safety net; never swallow an exception without a decision (ASVS 5.0 V16 Security Logging and Error Handling).
- Wrap multi-step changes in atomic transactions and roll back everything on failure (fail closed); make retried operations idempotent (see `race-condition`, `workflow-bypass`).
- Release resources deterministically with `finally`, `try-with-resources`, `using`, or `defer`, and bound pools, queues, and retries.
- Validate input early to prevent avoidable exceptions (ASVS 5.0 V2 Validation and Business Logic), and use consistent, centralized error handling across the codebase (ASVS 5.0 V15 Secure Coding and Architecture).
- Log exceptions server side with context and alert on repeated error patterns that suggest probing.

## References

- [OWASP Top 10:2025 A10 Mishandling of Exceptional Conditions](https://top10.owasp.org/2025/A10_2025-Mishandling_of_Exceptional_Conditions/)
- [CWE-755: Improper Handling of Exceptional Conditions](https://cwe.mitre.org/data/definitions/755.html)
- [CWE-460: Improper Cleanup on Thrown Exception](https://cwe.mitre.org/data/definitions/460.html)
- [OWASP Error Handling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Error_Handling_Cheat_Sheet.html)
