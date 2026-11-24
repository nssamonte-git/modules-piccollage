---
id: llm-prompt-injection
title: Prompt Injection (Direct and Indirect)
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM01:2025, ASI01:2026]
cwe: [CWE-1427, CWE-77]
severity: high
last_verified: 2026-10-29
aliases: [indirect-prompt-injection, jailbreak-llm]
---

# Prompt Injection (Direct and Indirect)

## Why It Is a Threat

Attacker-controlled text overrides the model's instructions — directly (malicious user input) or indirectly (content the model ingests: web pages, documents, emails, images, tool output, or retrieved RAG chunks). The model then ignores guardrails, leaks data, or invokes tools maliciously; in agents this becomes goal hijack (ASI01:2026). Instructions can be hidden from humans (white text, metadata, image content), and RAG or fine-tuning does not remove the risk. The top LLM risk.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map every channel that reaches the context window: user input, retrieved documents, browsed pages, file and image uploads, tool and MCP results, and agent memory.
- With benign canary instructions, test whether text in each channel can override the system prompt, change the agent's goal, trigger tools, or alter output; check that guardrail and approval steps still apply.

## How to Fix

- Treat all model input (including retrieved, tool, and memory content) as untrusted; separate and label system instructions versus external data; constrain tools and require approval for sensitive actions (`llm-excessive-agency`).
- Enforce authorization, data access, and output validation outside the model (ASVS 5.0 V8 Authorization); do not rely on prompt-only guardrails. Filter input and output, run adversarial tests regularly, and keep the agent's goal and tool set fixed at runtime. See `agentic-supply-chain` (tool-description poisoning) and `agent-memory-context-poisoning`.

## References

- [OWASP LLM01:2025 Prompt Injection](https://genai.owasp.org/llmrisk/llm01-prompt-injection/)
- [OWASP LLM Prompt Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/LLM_Prompt_Injection_Prevention_Cheat_Sheet.html)
- [OWASP Top 10 for Agentic Applications 2026 (ASI01 Agent Goal Hijack)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [CWE-1427: Improper Neutralization of Input Used for LLM Prompting](https://cwe.mitre.org/data/definitions/1427.html)
