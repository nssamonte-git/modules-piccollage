---
id: llm-sensitive-data-disclosure
title: LLM Sensitive-Data Disclosure
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM02:2025, LLM07:2025]
cwe: [CWE-200, CWE-359]
severity: high
last_verified: 2026-10-29
aliases: [system-prompt-leakage, llm-data-leakage]
---

# LLM Sensitive-Data Disclosure

## Why It Is a Threat

The model reveals sensitive data — other users' data, PII, secrets, proprietary logic, or the system prompt itself (which may contain keys, internal rules, filter criteria, or role details) — through its responses, because sensitive content entered its context, training, fine-tuning, or retrieval and was not access-controlled. A leaked system prompt mainly matters when it holds secrets or is used as a security control (LLM07:2025). User prompts sent to a provider may also be retained or used for training.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate), with synthetic users and data.

- Probe for system-prompt extraction and cross-user, PII, or secret disclosure; review what the system prompt contains.
- Check whether RAG retrieval enforces per-user authorization (`llm-vector-db-leakage`) and what the provider's data-retention and training terms are.

## How to Fix

- Keep secrets and credentials out of prompts and context; assume the system prompt can be read; enforce security controls (authorization, privilege separation) outside the model.
- Enforce per-user authorization on retrieved data (ASVS 5.0 V8.2.2); filter/redact model output; sanitize or minimize sensitive data before training, fine-tuning, or sending to a provider (ASVS 5.0 V14.2.3), and choose provider settings that disable retention or training where needed. See `sensitive-data-exposure`.

## References

- [OWASP LLM02:2025 Sensitive Information Disclosure](https://genai.owasp.org/llmrisk/llm022025-sensitive-information-disclosure/)
- [OWASP LLM07:2025 System Prompt Leakage](https://genai.owasp.org/llmrisk/llm072025-system-prompt-leakage/)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
