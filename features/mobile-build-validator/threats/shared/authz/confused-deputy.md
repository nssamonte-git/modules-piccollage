---
id: confused-deputy
title: Confused-Deputy Attack
category: authz
platforms: [shared, web, backend]
owasp_refs: [A01:2025, API1:2023]
cwe: [CWE-441]
severity: medium
last_verified: 2026-10-29
aliases: [impersonation-abuse, ssrf-trust-abuse, impersonation-feature-abuse]
---

# Confused-Deputy Attack

## Why It Is a Threat

A privileged component is tricked into using its authority on behalf of a less-privileged caller. Examples: an impersonation/"act-as" feature without re-authorization, a service that forwards a caller's request using its own elevated credentials, a cloud role assumable by any tenant's request, or trusting an internal header an attacker can set.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Exercise impersonation/delegation and "act on behalf of" features; confirm the original caller's authority is re-checked against the target action.
- Test internal trust headers (`X-User`, `X-Internal`, `X-Forwarded-*`) for attacker control from the edge.
- Check service-to-service calls and cross-account cloud roles that reuse elevated credentials for user-driven actions.

## How to Fix

- Authorize against the originating subject's permissions, not the intermediary's: propagate the caller's token or a scoped delegated token (e.g. OAuth 2.0 Token Exchange) instead of a machine-to-machine credential.
- Strip/secure internal trust headers at the edge; use least-privilege service identities, and bind cross-account role trust to the expected caller (e.g. external-id or source conditions). Map to ASVS 5.0 V8 Authorization (8.3.3).

## References

- [CWE-441: Unintended Proxy or Intermediary](https://cwe.mitre.org/data/definitions/441.html)
- [AWS IAM: The confused deputy problem](https://docs.aws.amazon.com/IAM/latest/UserGuide/confused-deputy.html)
- [RFC 8693: OAuth 2.0 Token Exchange](https://www.rfc-editor.org/rfc/rfc8693)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
