---
id: agent-cascading-failures
title: Agent Cascading Failures
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI08:2026, LLM06:2025, LLM01:2025, LLM04:2025]
cwe: [CWE-400, CWE-770]
severity: high
last_verified: 2026-10-29
aliases: [multi-agent-cascade, cascading-hallucination, agent-feedback-loop]
---

# Agent Cascading Failures

## Why It Is a Threat

A single fault in a multi-agent system, such as a hallucination, injected prompt, poisoned memory, or tainted tool, spreads through autonomous agents that plan, delegate, and act on each other's output, compounding into system-wide harm faster than humans can intervene. This card is about the propagation and amplification, not the original defect: record that under `agentic-supply-chain`, `agent-memory-context-poisoning`, or `insecure-inter-agent-communication`.

Typical chains: an executor runs a compromised planner's steps without validation; one poisoned update makes peer agents act on false alerts; one agent's misused credential leads downstream agents to repeat the action; an orchestrator auto-deploys a bad release to every agent; two agents reinforce each other's errors in a feedback loop; and human oversight weakens into bulk approvals after a run of successes. Warning signs include rapid fan-out of tasks, spread across tenants or domains, oscillating retries, queue storms, and repeated identical intents. For infrastructure-level retry amplification see `retry-storm`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map agent dependencies: who consumes whose output, and which steps run without independent validation or a human gate.
- Check for blast-radius limits between planner and executor: quotas, rate limits, circuit breakers, and progress caps.
- In an isolated replica, inject one benign faulty output and measure how far it propagates and whether monitoring throttles or pauses it.
- Confirm that inter-agent messages, policy decisions, and actions are logged with lineage so a cascade can be traced and rolled back.

## How to Fix

- Design for failure: assume any model, agent, or external source can be wrong or unavailable; isolate agents with least privilege, segmentation, and scoped APIs (ASVS 5.0 V15 Secure Coding and Architecture).
- Separate planning from execution with an external policy engine; validate each high-impact tool call against policy-as-code using short-lived, task-scoped credentials (ASVS 5.0 V8 Authorization).
- Add output validation and human checkpoints before results propagate downstream.
- Rate-limit, set quotas and circuit breakers, and auto-pause on anomalous fan-out; track drift against behavior baselines.
- Replay recorded agent actions in an isolated clone before widening automation.
- Keep tamper-evident, time-stamped logs bound to agent identities with lineage metadata (ASVS 5.0 V16 Security Logging and Error Handling).

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI08 Cascading Failures)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP LLM06:2025 Excessive Agency](https://genai.owasp.org/llmrisk/llm062025-excessive-agency/)
- [CWE-400: Uncontrolled Resource Consumption](https://cwe.mitre.org/data/definitions/400.html)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
