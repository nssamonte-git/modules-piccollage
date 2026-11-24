---
id: workflow-bypass
title: Workflow / State Bypass
category: business-logic
platforms: [shared, web, backend]
owasp_refs: [WSTG-BUSL-06, A06:2025]
cwe: [CWE-841]
severity: high
last_verified: 2026-10-29
aliases: [transaction-state-manipulation, approval-process-bypass]
---

# Workflow / State Bypass

## Why It Is a Threat

An attacker skips or reorders required steps in a multi-step process (checkout, KYC, approval, onboarding), for example jumping to "paid" without paying or self-approving, because the server does not enforce valid state transitions.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map the intended flow; attempt to call later steps directly, skip validation or approval steps, or replay a completed step.

## How to Fix

- Enforce a server-side state machine: validate that each transition is allowed from the current state for this actor; never trust client-provided step or state; expire partial states.
- Re-check authorization at each step and require a different approver for approvals. Map to ASVS 5.0 V2 Validation and Business Logic (2.3.1, 2.3.5).

## References

- [OWASP WSTG-BUSL-06 Circumvention of Work Flows](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/10-Business_Logic/06-Circumvention_of_Work_Flows/)
- [CWE-841: Improper Enforcement of Behavioral Workflow](https://cwe.mitre.org/data/definitions/841.html)
- [OWASP Business Logic Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Business_Logic_Security_Cheat_Sheet.html)
