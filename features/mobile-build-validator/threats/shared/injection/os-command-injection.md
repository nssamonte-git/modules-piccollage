---
id: os-command-injection
title: OS Command Injection
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, WSTG-INPV-12]
cwe: [CWE-78, CWE-77, CWE-88]
severity: critical
last_verified: 2026-10-29
---

# OS Command Injection

## Why It Is a Threat

Untrusted input is passed to a shell or system command, letting an attacker run arbitrary commands on the host — typically full server compromise. CWE-78 and its parent CWE-77 are both in the 2025 CWE Top 25. A related variant, argument injection, lets input that starts with `-` change a program's options even when no shell is involved.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify features that shell out (image/PDF processing, archive handling, network tools, exports, git or backup wrappers); source review for `system`, `exec`, `popen`, `shell=True`, and backticks is the most reliable starting point.
- Inject a separator plus a harmless, observable marker (e.g. an owner-approved DNS/HTTP callback or a bounded time delay); do not run destructive, persistence, or reverse-shell commands. Escalation requires explicit written approval.
- Source review by stack (`source-code-vapt`): Android `Runtime.getRuntime().exec(` or `ProcessBuilder(` with input; Laravel `Process::run("...$x")` string form and `Artisan::call` with input; PHP `system(`, `exec(`, `shell_exec(`, `passthru(`, backticks; Python `subprocess` with `shell=True` and `os.system(`; Rust `Command::new("sh").arg("-c").arg(format!(...))`.

## How to Fix

- Prefer a library or language API over invoking an OS command at all; otherwise call the program directly with an argument array and no shell interpolation (ASVS 5.0 V1.2 Injection Prevention, requirement 1.2.5).
- Validate arguments against a strict allowlist, end option parsing with `--` before user-supplied operands, and never build command strings from input.
- Run with least privilege and a minimal environment.

## References

- [OWASP OS Command Injection Defense Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/OS_Command_Injection_Defense_Cheat_Sheet.html)
- [OWASP WSTG v4.2 WSTG-INPV-12: Testing for Command Injection](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/12-Testing_for_Command_Injection/)
- [CWE-78: Improper Neutralization of Special Elements used in an OS Command ('OS Command Injection')](https://cwe.mitre.org/data/definitions/78.html)
- [CWE-88: Improper Neutralization of Argument Delimiters in a Command ('Argument Injection')](https://cwe.mitre.org/data/definitions/88.html)
