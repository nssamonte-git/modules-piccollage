---
id: llm-excessive-agency
title: Excessive Agency / Tool-Call Abuse
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM06:2025, ASI02:2026]
cwe: [CWE-250, CWE-862]
severity: high
last_verified: 2026-10-29
aliases: [excessive-agent-permissions, tool-call-manipulation]
---

# Excessive Agency / Tool-Call Abuse

## Why It Is a Threat

An LLM agent has excessive functionality (tools it does not need), excessive permissions (broad scopes, write or admin access), or excessive autonomy (acts without confirmation), so a prompt injection (`llm-prompt-injection`), poisoned tool output, or a bad inference triggers harmful actions: sending money, deleting data, calling internal APIs. Tool-call arguments can also be manipulated so a legitimate tool is misused (ASI02:2026 Tool Misuse and Exploitation).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory agent tools, extensions, and MCP servers with their scopes and credentials; flag open-ended tools (shell, raw SQL, arbitrary URL fetch) and tools that run with a shared service identity.
- In a sandbox, test whether injected instructions can invoke sensitive tools, chain tools, or alter tool arguments without authorization or human approval.

## How to Fix

- Minimize tools and their functionality; give each the narrowest read-only scope by default; require human approval for sensitive or irreversible actions (ASVS 5.0 V2.3.5 multi-user approval for high-value flows).
- Authorize each tool call server-side against the end user's own permissions, not the agent's or the model's claim (ASVS 5.0 V8.3.3); validate tool arguments against strict schemas; rate-limit and log tool use.
- See `agentic-supply-chain` for MCP and tool provenance and `agent-identity-privilege-abuse` for delegated credentials.

## References

- [OWASP LLM06:2025 Excessive Agency](https://genai.owasp.org/llmrisk/llm062025-excessive-agency/)
- [OWASP Top 10 for Agentic Applications 2026 (ASI02 Tool Misuse and Exploitation)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP AI Agent Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/AI_Agent_Security_Cheat_Sheet.html)
- [CWE-250: Execution with Unnecessary Privileges](https://cwe.mitre.org/data/definitions/250.html)
