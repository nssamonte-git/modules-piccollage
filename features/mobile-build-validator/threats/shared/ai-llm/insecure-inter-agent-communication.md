---
id: insecure-inter-agent-communication
title: Insecure Inter-Agent Communication
category: ai-llm
platforms: [shared, web, backend]
owasp_refs: [ASI07:2026, LLM01:2025]
cwe: [CWE-345, CWE-306, CWE-294, CWE-319]
severity: high
last_verified: 2026-10-29
aliases: [agent-message-spoofing, a2a-spoofing, agent-in-the-middle, inter-agent-message-tampering]
---

# Insecure Inter-Agent Communication

## Why It Is a Threat

Messages between AI agents (over A2A, MCP, APIs, message buses, or shared memory) lack authentication, integrity, encryption, or replay protection, so an attacker can intercept, spoof, replay, or rewrite them to inject instructions and steer what cooperating agents do. Multi-agent systems are decentralized and trust each other unevenly, so perimeter controls do not protect these exchanges.

Patterns include an attacker in the middle adding hidden instructions to cleartext traffic; tampered reputation or coordination messages; replayed delegation messages that grant stale authority; forced downgrade to a weaker, unencrypted protocol mode; fake peers registered in a discovery service with cloned schemas or spoofed agent cards; and traffic-pattern analysis that reveals agent roles and decision cycles. The same instruction can also be parsed differently by two agents, producing conflicting actions. This card covers live messages; credential misuse is `agent-identity-privilege-abuse` and stored-context corruption is `agent-memory-context-poisoning`. For non-agent services see `unauthenticated-service-to-service`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map agent-to-agent channels, discovery services, and registries, noting transport security, peer authentication, and message signing for each.
- Check whether messages carry nonces, timestamps, and session or task ids, and whether replays outside the task window are rejected.
- Confirm that legacy or unencrypted protocol modes and unknown schema versions are refused.
- Check whether a test agent can register in discovery or the registry without attestation.
- Validate that receiving agents check message schema and audience before acting.

## How to Fix

- Use mutually authenticated, encrypted channels with per-agent credentials (mTLS, certificate pinning, forward secrecy) (ASVS 5.0 V12 Secure Communication).
- Sign messages over payload and context, and verify them before acting; add nonces, timestamps, and task-window binding against replay.
- Pin allowed protocol versions (MCP, A2A, gRPC) and reject downgrades and unrecognized schemas.
- Authenticate discovery and routing messages; use attested registries with signed agent cards and no open registration.
- Use typed, versioned message schemas with explicit audiences (ASVS 5.0 V4 API and Web Service); pad or smooth traffic where metadata inference matters.

## References

- [OWASP Top 10 for Agentic Applications 2026 (ASI07 Insecure Inter-Agent Communication)](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/)
- [OWASP AI Agent Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/AI_Agent_Security_Cheat_Sheet.html)
- [CWE-345: Insufficient Verification of Data Authenticity](https://cwe.mitre.org/data/definitions/345.html)
- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
