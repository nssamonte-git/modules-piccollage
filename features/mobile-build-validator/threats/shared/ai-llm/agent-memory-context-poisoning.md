---
id: agent-memory-context-poisoning
title: Agent Memory and Context Poisoning
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI06:2026, LLM04:2025, LLM08:2025, LLM01:2025]
cwe: [CWE-1427, CWE-345]
severity: high
last_verified: 2026-10-29
aliases: [agent-memory-poisoning, long-term-memory-poisoning, shared-memory-poisoning, persistent-prompt-injection]
---

# Agent Memory and Context Poisoning

## Why It Is a Threat

Attackers plant false facts or hidden instructions in an AI agent's persistent memory, conversation summaries, or shared context so that the poison survives the session and biases later reasoning, tool use, or data handling. Unlike one-shot prompt injection, the corruption persists across sessions and can spread to other users or cooperating agents.

Ways in: indirect prompt injection that gets saved to an assistant's long-term memory; content injected mid-task that is later summarized into memory; repeated reinforcement of a false value until it is stored as fact; slow drift from subtly tainted inputs; shared memory reused by several agents; and loose namespace filters that pull another tenant's content into retrieval. Effects include approving fraudulent transactions, relabeling malicious activity as normal, trigger-based backdoors, and ongoing data exfiltration. Poisoned memory often leads to goal hijack. Related cards: `llm-prompt-injection` (one-time input), `llm-data-poisoning` (training and corpus data), `llm-vector-db-leakage` (retrieval authorization).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map what the agent stores (summaries, memory tool entries, embeddings, shared scratchpads), who can write to it, and how long entries live.
- Check whether memory writes are validated, attributed to a source, and segmented per user, tenant, and task.
- In a test tenant, plant a benign marker fact through an ingested document and check whether it persists into a new session or another user's context.
- Confirm that agent-generated outputs are not automatically re-ingested as trusted memory.
- Verify rollback, quarantine, and expiry of suspect memory entries.

## How to Fix

- Scan memory writes for instructions and sensitive data before commit; accept writes only from authenticated, curated sources (ASVS 5.0 V2 Validation and Business Logic).
- Segment memory per user, tenant, and domain with strict namespaces; enforce per-task access (ASVS 5.0 V8 Authorization).
- Record provenance and trust scores for entries; decay or expire unverified memory; require stronger provenance to surface high-impact memory.
- Block automatic re-ingestion of the agent's own outputs into trusted memory.
- Version and snapshot memory with rollback; encrypt it at rest and in transit (ASVS 5.0 V14 Data Protection); require human review for high-risk actions.

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI06 Memory and Context Poisoning)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP LLM04:2025 Data and Model Poisoning](https://genai.owasp.org/llmrisk/llm042025-data-and-model-poisoning/)
- [OWASP LLM08:2025 Vector and Embedding Weaknesses](https://genai.owasp.org/llmrisk/llm082025-vector-and-embedding-weaknesses/)
- [OWASP AI Agent Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/AI_Agent_Security_Cheat_Sheet.html)
- [CWE-1427: Improper Neutralization of Input Used for LLM Prompting](https://cwe.mitre.org/data/definitions/1427.html)
