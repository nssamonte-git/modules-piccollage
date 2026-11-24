---
id: agentic-supply-chain
title: Agentic Supply Chain (Malicious MCP Servers, Tools, and Agent Registries)
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI04:2026, LLM03:2025, LLM01:2025]
cwe: [CWE-829, CWE-1357, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [mcp-tool-poisoning, tool-description-poisoning, malicious-mcp-server, agent-registry-compromise, agent-card-spoofing]
---

# Agentic Supply Chain (Malicious MCP Servers, Tools, and Agent Registries)

## Why It Is a Threat

A malicious or compromised MCP server, tool, plugin, prompt template, or third-party agent loaded at runtime injects hidden instructions or code into an AI agent, for example through poisoned tool descriptions (MCP tool poisoning), typosquatted MCP packages, or spoofed agent cards in an Agent2Agent registry. Agents compose these components dynamically and trust their metadata as guidance, so one tampered component can exfiltrate data or steer actions across every agent that uses it.

Scope: this card covers the runtime agent layer (MCP and A2A servers, tool and plugin manifests, remote prompt templates, agent and package registries, update channels). Model weights, adapters, and datasets are covered by `llm-supply-chain`; ordinary package compromise by `malicious-packages` and `compromised-build-tools`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory every MCP server, tool, plugin, remote prompt, and peer agent the agent can load, with its source, version pin, and signer.
- Review tool descriptions, schemas, and agent cards for hidden or instruction-like text, and diff them across versions to detect silent changes.
- Check whether agents auto-install or auto-discover tools and peers, and whether registration in the agent or MCP registry is open.
- Look for look-alike server or package names and unpinned or unsigned dependencies.
- In a sandbox, confirm that a benign test tool with altered metadata is rejected or flagged before use.

## How to Fix

- Allowlist and pin MCP servers, tools, prompts, and agent configs by content hash or commit; reject unsigned or unverified components and use curated registries only (ASVS 5.0 V15 Secure Coding and Architecture).
- Sign and attest manifests, tool definitions, and agent cards; keep an SBOM or AIBOM for agent components and re-verify at runtime.
- Require mutual authentication for agent and MCP connections and close open registration.
- Run tools and MCP servers in sandboxes with restricted network and filesystem access; show users tool descriptions and ask consent at install.
- Keep a kill switch that revokes a tool, prompt, or connection everywhere at once; roll out changes in stages with auto-rollback on hash drift.

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI04 Agentic Supply Chain Vulnerabilities)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP MCP Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/MCP_Security_Cheat_Sheet.html)
- [OWASP LLM03:2025 Supply Chain](https://genai.owasp.org/llmrisk/llm032025-supply-chain/)
- [CWE-829: Inclusion of Functionality from Untrusted Control Sphere](https://cwe.mitre.org/data/definitions/829.html)
