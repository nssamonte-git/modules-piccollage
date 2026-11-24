---
id: llm-model-extraction
title: Model Extraction / Theft
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM10:2025]
cwe: [CWE-200, CWE-799]
severity: medium
last_verified: 2026-10-29
aliases: [model-theft, model-replication]
---

# Model Extraction / Theft

## Why It Is a Threat

Attackers query the model at scale to replicate its behavior (functional replication: using API outputs as synthetic training data for a knock-off model), extract parameters or decision boundaries through crafted queries and exposed logits/log-probabilities, reconstruct proprietary prompts or fine-tuning, or infer training data — stealing IP and competitive value, and potentially leaking sensitive training content. Direct theft of weights from storage or a model registry has the same impact.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Assess authentication, rate limits, and abuse detection on the inference API; check whether logits or log-probabilities are exposed.
- Review logs for systematic high-volume or boundary-probing query patterns, and check access control on model artifacts and registries.

## How to Fix

- Authenticate and rate-limit inference APIs; detect and throttle systematic querying; avoid exposing logits/log-probabilities unless needed; watermark and monitor outputs where feasible.
- Protect weights and adapters with access control and an inventory of deployed models; limit exposure of proprietary prompts and fine-tuned behavior. See `scraping`.

## References

- [OWASP LLM10:2025 Unbounded Consumption (model extraction and functional replication)](https://genai.owasp.org/llmrisk/llm102025-unbounded-consumption/)
- [NIST AI 100-2 E2025: Adversarial Machine Learning Taxonomy](https://csrc.nist.gov/pubs/ai/100/2/e2025/final)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
