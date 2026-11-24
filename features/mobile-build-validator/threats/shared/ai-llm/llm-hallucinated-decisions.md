---
id: llm-hallucinated-decisions
title: Hallucinated / Overrelied Security Decisions
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM09:2025]
cwe: [CWE-1426, CWE-807]
severity: high
last_verified: 2026-10-29
aliases: [llm-misinformation, llm-overreliance]
---

# Hallucinated / Overrelied Security Decisions

## Why It Is a Threat

Treating model output as authoritative for security or business decisions — authorization, validation, fraud/risk scoring, code suggestions — is dangerous: models hallucinate, can be manipulated (`llm-prompt-injection`), and provide no guarantees, so a confident wrong answer becomes an authz bypass, bad code, or financial error. Generated code can also reference insecure APIs or non-existent packages that an attacker later registers.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify where model output drives security or business decisions, or generates code, configuration, or dependency names without review.
- Test manipulation and failure modes: contradictory or out-of-domain inputs, missing context, and whether a wrong answer is caught by a deterministic check.

## How to Fix

- Never let the model be the authorization or validation boundary; enforce real checks deterministically at a trusted server layer (ASVS 5.0 V8.3.1 and V2.2.2); treat output as a suggestion requiring verification.
- Ground answers with vetted sources (RAG), cross-check high-impact outputs, and show users the limits of AI content.
- Review AI-generated code and verify suggested packages exist and are trusted (`unreviewed-ai-code`); add human oversight for high-impact decisions.

## References

- [OWASP LLM09:2025 Misinformation](https://genai.owasp.org/llmrisk/llm092025-misinformation/)
- [CWE-1426: Improper Validation of Generative AI Output](https://cwe.mitre.org/data/definitions/1426.html)
- [CWE-807: Reliance on Untrusted Inputs in a Security Decision](https://cwe.mitre.org/data/definitions/807.html)
