---
id: llm-supply-chain
title: Malicious Model / Dataset Supply Chain
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM03:2025]
cwe: [CWE-1395, CWE-506, CWE-502]
severity: high
last_verified: 2026-10-29
aliases: [malicious-model, malicious-dataset, model-supply-chain]
---

# Malicious Model / Dataset Supply Chain

## Why It Is a Threat

Third-party models, adapters (LoRA), merged models, datasets, and ML tooling from hubs can be backdoored, trojanized (malicious deserialization in model files such as pickle), outdated, or carry hidden behavior — running with app privileges or skewing outputs. Compromised conversion or merge services, unclear licenses, and tampered on-device models widen the risk. Extends software `vulnerable-dependencies`/`malicious-packages` to the model layer; runtime MCP servers and tools are `agentic-supply-chain`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review provenance, signing, and integrity of models, datasets, and adapters, plus the supplier's terms and data practices.
- Scan model files for unsafe serialization (e.g. pickle); check ML library versions and tooling sources; evaluate third-party models for unexpected behavior before use.

## How to Fix

- Source models, datasets, and adapters from trusted, verified origins; prefer safe formats (e.g. safetensors) over pickle; pin versions and verify hashes/signatures; scan and sandbox model loading (ASVS 5.0 V1.5.2 safe deserialization).
- Maintain an inventory or SBOM/AI BOM for ML components and pull them only from expected repositories (ASVS 5.0 V15.1.2 and V15.2.4); patch ML frameworks; red-team models before deployment. See `malicious-packages`, `unsafe-deserialization`.

## References

- [OWASP LLM03:2025 Supply Chain](https://genai.owasp.org/llmrisk/llm032025-supply-chain/)
- [OWASP Software Supply Chain Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Software_Supply_Chain_Security_Cheat_Sheet.html)
- [CWE-502: Deserialization of Untrusted Data](https://cwe.mitre.org/data/definitions/502.html)
- [CWE-1395: Dependency on Vulnerable Third-Party Component](https://cwe.mitre.org/data/definitions/1395.html)
