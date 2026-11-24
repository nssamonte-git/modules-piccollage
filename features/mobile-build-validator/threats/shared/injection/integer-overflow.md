---
id: integer-overflow
title: Integer Overflow / Underflow
category: injection
platforms: [shared, backend, android, ios]
owasp_refs: [A05:2025, MASVS-CODE]
cwe: [CWE-190, CWE-191]
severity: medium
last_verified: 2026-10-29
aliases: [integer-underflow]
---

# Integer Overflow / Underflow

## Why It Is a Threat

Arithmetic on attacker-influenced values wraps past type limits, producing wrong sizes, indices, balances, or loop bounds — leading to memory-safety bugs in native code (an undersized allocation followed by an overflow), logic flaws, or financial discrepancies (e.g. a negative or huge quantity). Behaviour differs by language: C/C++, Java, and Kotlin wrap silently, Rust wraps in release builds unless overflow checks are enabled, and Swift's standard operators trap (a crash, i.e. denial of service).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Supply boundary values (max/min, negative, very large, zero) to size, count, price, offset, and length parameters; observe wraparound effects on logic or allocation.
- In native code, review size calculations before allocation and run UBSan/fuzzing on an authorized build.
- Source review by stack (`source-code-vapt`): Rust arithmetic on input sizes in release builds, where overflow wraps unless `overflow-checks` is on; use `checked_*`.

## How to Fix

- Validate sign and range before arithmetic, allocation, and indexing; use checked or saturating arithmetic (`Math.addExact`, Rust `checked_*`, compiler overflow builtins) and wide enough types (ASVS 5.0 V1.4 Memory, String, and Unmanaged Code, requirement 1.4.2).
- Use decimal or fixed-point types for money and enforce business limits server-side. In native code, pair with bounds checks.

## References

- [CWE-190: Integer Overflow or Wraparound](https://cwe.mitre.org/data/definitions/190.html)
- [CWE-191: Integer Underflow (Wrap or Wraparound)](https://cwe.mitre.org/data/definitions/191.html)
- [OWASP Input Validation Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html)
