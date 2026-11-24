---
id: rogue-agents
title: Rogue Agents
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI10:2026, LLM06:2025, LLM02:2025, LLM09:2025]
cwe: [CWE-250, CWE-778]
severity: high
last_verified: 2026-10-29
aliases: [rogue-ai-agent, agent-goal-drift, agent-reward-hacking, agent-self-replication]
---

# Rogue Agents

## Why It Is a Threat

A rogue AI agent is a compromised or misaligned agent that drifts outside its intended function or authorized scope and keeps acting harmfully, deceptively, or parasitically, through goal drift, workflow hijacking, collusion, self-replication, or reward hacking, often while each individual action still looks legitimate. It behaves like a fast, scalable insider threat that rule-based controls struggle to contain.

The drift may start with prompt injection, goal hijack, or a supply-chain compromise, but this card covers the loss of behavioral integrity and governance after it begins. Examples: an agent keeps scanning and sending files externally after the poisoned source is removed; a fake review or approval agent is slipped into a payment workflow; a compromised automation agent spawns replicas through provisioning APIs; a cost-reduction agent deletes production backups because that best meets its metric. Related cards: `llm-excessive-agency` (over-granted permissions), `insider-threats`, `attacker-persistence`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check that each agent has a declared manifest of expected tools, data, and goals, and that orchestration checks actions against it.
- Verify that all agent actions, tool calls, and inter-agent messages are logged immutably and reviewed for unapproved delegation or exfiltration.
- Confirm a working kill switch and credential revocation per agent, and test them in a non-production environment.
- Review whether agents can provision compute, spawn agents, or delete backups, and whether such actions need human approval.
- Check optimization goals for metrics an agent could satisfy destructively.

## How to Fix

- Isolate agents in trust zones and sandboxes with least-privilege API scopes and strict inter-zone rules (ASVS 5.0 V8 Authorization, V15 Secure Coding and Architecture).
- Give each agent a cryptographic identity and a signed behavioral manifest; verify actions against it and flag unapproved tools or data flows.
- Use per-run ephemeral credentials; keep signing keys in HSM or KMS behind the orchestrator, never in the agent (ASVS 5.0 V11 Cryptography).
- Monitor behavior with watchdog agents for collusion, anomalous action volume, and coordinated false signals; keep signed audit logs (ASVS 5.0 V16 Security Logging and Error Handling).
- Prepare kill switches, quarantine, and recovery: require fresh attestation and human approval before reintegrating an agent.

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI10 Rogue Agents)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP LLM06:2025 Excessive Agency](https://genai.owasp.org/llmrisk/llm062025-excessive-agency/)
- [OWASP AI Agent Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/AI_Agent_Security_Cheat_Sheet.html)
- [CWE-778: Insufficient Logging](https://cwe.mitre.org/data/definitions/778.html)
