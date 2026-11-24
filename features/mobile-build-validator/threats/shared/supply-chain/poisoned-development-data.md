---
id: poisoned-development-data
title: Poisoned Development Data
category: supply-chain
platforms: [shared, web, backend]
owasp_refs: [A08:2025, LLM04:2025, A06:2025]
cwe: [CWE-1357, CWE-20]
severity: medium
last_verified: 2026-10-29
aliases: [data-poisoning, training-data-poisoning]
---

# Poisoned Development Data

## Why It Is a Threat

Untrusted test fixtures, seed data, or ML training and fine-tuning data is tampered with, embedding malicious payloads or skewing behavior. It can plant stored attacks (XSS, deserialization) when the data is used, or corrupt models and their decisions (data poisoning, including hidden backdoor triggers).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review provenance and integrity of test, seed, and training datasets; check whether untrusted data flows into builds, fixtures, or models without validation, and whether dataset versions are tracked.

## How to Fix

- Source datasets from trusted, integrity-verified locations; validate, sanitize, and version them; isolate processing of untrusted data.
- For ML, record data provenance, restrict who can change training data, and evaluate models for poisoning and backdoors before release. Map to A08:2025 and LLM04:2025.

## References

- [OWASP LLM04:2025 Data and Model Poisoning](https://genai.owasp.org/llmrisk/llm042025-data-and-model-poisoning/)
- [NIST AI 100-2 E2025: Adversarial Machine Learning](https://csrc.nist.gov/pubs/ai/100/2/e2025/final)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
