---
id: llm-model-dos
title: Model Denial of Service / Unbounded Consumption
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM10:2025, API4:2023]
cwe: [CWE-400, CWE-770]
severity: medium
last_verified: 2026-10-29
aliases: [llm-unbounded-consumption, llm-denial-of-wallet]
---

# Model Denial of Service / Unbounded Consumption

## Why It Is a Threat

Attackers send expensive or high-volume prompts (variable-length input floods, inputs that overflow the context window, resource-heavy queries, recursive or agentic loops) to exhaust compute and run up large inference bills — both availability and denial-of-wallet impact (`cost-exhaustion`), made worse by per-token pricing, long context windows, and agents that fan out into many model and tool calls.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate); keep volume within agreed limits.

- Review rate limits, per-user and per-tenant quotas, max input/output token caps, request timeouts, queue limits, and agent step/loop bounds.
- Assess cost controls on the inference path: budget caps, spend alerts, and what happens when a provider quota is exhausted.

## How to Fix

- Enforce per-user rate limits and quotas, max input size and output tokens, timeouts, and agent step/loop and queued-action caps (ASVS 5.0 V2.4.1 anti-automation and V15.2.2 resource-demanding functionality).
- Set hard budget limits and alerts on spend; monitor for anomalous usage; degrade gracefully under load instead of failing completely. Cache and throttle expensive operations. See `cost-exhaustion`, `agent-cascading-failures`.

## References

- [OWASP LLM10:2025 Unbounded Consumption](https://genai.owasp.org/llmrisk/llm102025-unbounded-consumption/)
- [OWASP API Security Top 10 2023 API4: Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
- [OWASP Denial of Service Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Denial_of_Service_Cheat_Sheet.html)
