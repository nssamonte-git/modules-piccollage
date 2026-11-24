---
id: cost-exhaustion
title: Cost Exhaustion (Denial of Wallet)
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023]
cwe: [CWE-770, CWE-400]
severity: high
last_verified: 2026-10-29
aliases: [autoscaling-cost-attack, paid-api-cost-exhaustion, sms-email-cost-exhaustion, denial-of-wallet]
---

# Cost Exhaustion (Denial of Wallet)

## Why It Is a Threat

Attackers drive up operating cost instead of taking you offline: triggering autoscaling, paid third-party or LLM API calls (`llm-model-dos`), SMS/email sends, or egress and storage. Unrestricted resource consumption becomes a financial attack (denial of wallet).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify endpoints that incur per-call cost (paid APIs, SMS, compute, egress, storage) and who can reach them unauthenticated.
- Check per-user and global quotas, provider spending limits or billing alerts, and autoscaling maximums.

## How to Fix

- Authenticate and rate-limit cost-incurring actions; enforce per-user and global quotas; cap autoscaling.
- Configure spending limits on every provider integration, or billing alerts where limits are not available, and alert on spend anomalies. Map to API4:2023 and ASVS 5.0 V2 Validation and Business Logic (2.4.1).

## References

- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [OWASP LLM10:2025 Unbounded Consumption](https://genai.owasp.org/llmrisk/llm102025-unbounded-consumption/)
- [CWE-770: Allocation of Resources Without Limits or Throttling](https://cwe.mitre.org/data/definitions/770.html)
- [AWS: Managing your costs with AWS Budgets](https://docs.aws.amazon.com/cost-management/latest/userguide/budgets-managing-costs.html)
