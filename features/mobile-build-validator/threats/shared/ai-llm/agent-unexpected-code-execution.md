---
id: agent-unexpected-code-execution
title: Agent Unexpected Code Execution (RCE)
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI05:2026, LLM05:2025, LLM01:2025]
cwe: [CWE-94, CWE-95, CWE-78]
severity: critical
last_verified: 2026-10-29
aliases: [agent-rce, vibe-coding-rce, agent-generated-code-execution, agent-sandbox-escape]
---

# Agent Unexpected Code Execution (RCE)

## Why It Is a Threat

An AI agent or coding assistant generates and runs code, shell commands, package installs, or deserialized objects that an attacker has steered through prompt injection, tool chaining, or poisoned dependencies, turning text into remote code execution on the agent's host or container. Because the code is produced at runtime, it can bypass review and the usual build-time controls.

Common paths: injected instructions that the agent turns into shell commands; hallucinated or backdoored code applied without review; unsafe `eval` in agent memory or tool logic; deserializing agent-produced objects; a chain of individually allowed tools (upload, traversal, dynamic load) that ends in execution; and installing an unpinned or malicious package during a "fix the build" task. Outcomes include data deletion, credential theft, persistence, and sandbox escape. This extends `llm-insecure-output-handling`, `code-injection`, and `os-command-injection` to agent runtimes.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map every place the agent can write files, run commands, evaluate code, install packages, or deserialize data, and the privileges it runs with.
- Check whether execution is gated by an allowlist, static scan, or human approval, and whether generated code reaches production directly.
- In an isolated sandbox, submit benign marker instructions in user input and ingested content and confirm they are not executed.
- Verify the sandbox: non-root user, restricted network egress, working-directory-only filesystem access, and resource limits.
- Look for `eval`-style evaluators in agent memory or orchestration code.

## How to Fix

- Separate code generation from execution with a validation gate; apply output handling controls and treat generated code as untrusted (ASVS 5.0 V1 Encoding and Sanitization, V15 Secure Coding and Architecture).
- Ban `eval` and unsafe deserialization in agents; use safe interpreters and taint-track generated code.
- Run code in per-session sandboxed containers, never as root, with no or allowlisted network access and a dedicated working directory.
- Require human approval for elevated runs; keep the auto-execution allowlist under version control.
- Pin dependencies and lockfiles, block known-vulnerable packages, and log all generation and execution (ASVS 5.0 V16 Security Logging and Error Handling).

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI05 Unexpected Code Execution)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP LLM05:2025 Improper Output Handling](https://genai.owasp.org/llmrisk/llm052025-improper-output-handling/)
- [CWE-94: Improper Control of Generation of Code](https://cwe.mitre.org/data/definitions/94.html)
- [CWE-95: Improper Neutralization of Directives in Dynamically Evaluated Code](https://cwe.mitre.org/data/definitions/95.html)
