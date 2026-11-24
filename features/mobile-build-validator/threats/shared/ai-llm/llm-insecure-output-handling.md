---
id: llm-insecure-output-handling
title: Insecure Model Output Handling
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [LLM05:2025]
cwe: [CWE-1426, CWE-79, CWE-94]
severity: high
last_verified: 2026-10-29
aliases: [improper-output-handling]
---

# Insecure Model Output Handling

## Why It Is a Threat

Model output is passed unvalidated to downstream systems — rendered as HTML or Markdown (XSS, auto-loaded images or links that carry data to an attacker URL), executed as code/SQL/commands, used in file paths or email templates, or sent to another API — so a prompt-injected or hallucinated response becomes `xss`, `sql-injection`, path traversal, or `remote-code-execution`. The model is an untrusted source, and impact grows when the model has more privileges than the end user.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trace where model output flows (HTML/Markdown render, DB query, code exec, shell, file system, downstream API).
- Using benign markers, test whether model-produced output reaches a dangerous sink without encoding or validation.

## How to Fix

- Treat model output as untrusted input: encode for the render context, parameterize queries, never `eval`/shell it, and validate against expected schemas (ASVS 5.0 V1.2 Injection Prevention and V1.3.2).
- Apply a strict Content Security Policy, restrict which image and link domains the UI loads, and sandbox any code the model produces. Log and monitor unusual output patterns. See `xss`, `code-injection`.

## References

- [OWASP LLM05:2025 Improper Output Handling](https://genai.owasp.org/llmrisk/llm052025-improper-output-handling/)
- [CWE-1426: Improper Validation of Generative AI Output](https://cwe.mitre.org/data/definitions/1426.html)
- [OWASP Cross Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
- [CWE-94: Improper Control of Generation of Code](https://cwe.mitre.org/data/definitions/94.html)
