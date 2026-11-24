---
id: llm-data-poisoning
title: Training / Retrieval Data Poisoning
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM04:2025]
cwe: [CWE-1427, CWE-20, CWE-345]
severity: high
last_verified: 2026-10-29
aliases: [training-data-poisoning, retrieval-data-poisoning, model-poisoning]
---

# Training / Retrieval Data Poisoning

## Why It Is a Threat

Attackers tamper with pre-training data, fine-tuning sets, embeddings, or the documents a RAG system retrieves, embedding backdoors (dormant triggers), bias, or hidden instructions (a vector for `llm-prompt-injection`) that alter model behavior or decisions — corrupting outputs at scale and often staying undetected until triggered. Poisoned model files can also carry malicious code (`llm-supply-chain`).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review provenance and integrity of training, fine-tune, and RAG data and their ingestion pipelines; check who can write to each source.
- In a test environment, check whether untrusted content can enter the corpus and influence outputs, and whether evaluations or red-team tests would detect a trigger.

## How to Fix

- Source data from trusted, integrity-verified locations and track provenance and versions (data version control, ML BOM); validate/sanitize and curate ingestion; isolate and review untrusted/user-contributed content before indexing or training.
- Sandbox exposure to unverified sources, monitor training loss and output for anomalies, and red-team models before release. See `poisoned-development-data`, `agent-memory-context-poisoning`.

## References

- [OWASP LLM04:2025 Data and Model Poisoning](https://genai.owasp.org/llmrisk/llm042025-data-and-model-poisoning/)
- [NIST AI 100-2 E2025: Adversarial Machine Learning Taxonomy](https://csrc.nist.gov/pubs/ai/100/2/e2025/final)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
