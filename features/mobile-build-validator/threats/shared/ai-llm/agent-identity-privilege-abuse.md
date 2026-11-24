---
id: agent-identity-privilege-abuse
title: Agent Identity and Privilege Abuse
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI03:2026, LLM06:2025, LLM01:2025, LLM02:2025]
cwe: [CWE-269, CWE-441, CWE-367]
severity: high
last_verified: 2026-10-29
aliases: [agent-privilege-escalation, agent-delegation-abuse, agent-credential-inheritance, agent-confused-deputy]
---

# Agent Identity and Privilege Abuse

## Why It Is a Threat

An AI agent's delegated identity, inherited credentials, or cached tokens are abused to escalate privilege, because agents pass full access down delegation chains, reuse secrets across tasks, and trust other agents without re-checking the original user's intent. Agents often run without a distinct, governed identity of their own, so least privilege and attribution break down.

Typical patterns: a manager agent hands its whole permission set to a narrow worker; credentials or retrieved data cached in agent memory leak into a later, less-privileged session; a compromised low-privilege agent relays a valid-looking request to a high-privilege agent (confused deputy, see `confused-deputy`); authorization checked at workflow start is still honored after the user's rights changed (TOCTOU); a forged agent persona in an agent registry inherits internal trust; one user's identity is silently shared through an agent's tools. This is the agentic evolution of `llm-excessive-agency` and is often triggered by `llm-prompt-injection`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory every agent identity, credential (API keys, OAuth tokens, delegated sessions), scope, and lifetime; flag shared, long-lived, or user-borrowed identities.
- Trace delegation: confirm whether sub-agents receive scoped-down tokens or the parent's full context.
- In a test tenant, check whether a second user's session can reach credentials or data cached by a previous task.
- Revoke or reduce a test user's rights mid-workflow and verify the agent stops.
- Check whether agents accept requests from peers or registry entries without verifying identity and the originating user's intent.

## How to Fix

- Give each agent its own managed non-human identity with short-lived, task-scoped credentials bound to subject, resource, purpose, and duration (ASVS 5.0 V8 Authorization, V9 Self-contained Tokens, V10 OAuth and OIDC).
- Never pass full context to sub-agents; block privilege inheritance unless the original intent is re-validated.
- Authorize every privileged action at execution time through a central policy engine, and require human approval for high-privilege or irreversible steps.
- Isolate memory and sessions per user and task; wipe cached secrets between tasks.
- Alert on new scopes gained through delegation, token reuse outside the bound intent, and device-code style flows (ASVS 5.0 V16 Security Logging and Error Handling).

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI03 Identity and Privilege Abuse)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP LLM06:2025 Excessive Agency](https://genai.owasp.org/llmrisk/llm062025-excessive-agency/)
- [OWASP AI Agent Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/AI_Agent_Security_Cheat_Sheet.html)
- [CWE-269: Improper Privilege Management](https://cwe.mitre.org/data/definitions/269.html)
- [CWE-441: Unintended Proxy or Intermediary](https://cwe.mitre.org/data/definitions/441.html)
