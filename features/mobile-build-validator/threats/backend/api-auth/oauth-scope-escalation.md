---
id: oauth-scope-escalation
title: OAuth Scope Escalation
category: backend-api-auth
platforms: [backend]
owasp_refs: [API5:2023, A01:2025]
cwe: [CWE-269, CWE-285]
severity: high
last_verified: 2026-10-29
---

# OAuth Scope Escalation

## Why It Is a Threat

A client obtains or uses scopes beyond what it should, through scope upgrade on refresh, unvalidated scope parameters, over-broad client registration or consent, or resource servers that do not enforce token scopes and audience. It then reaches data and actions it was never authorized for. OAuth 2.0 forbids a refresh request from adding scopes that were not originally granted. Related: `oauth-oidc-misconfiguration`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Request or alter scopes during authorization and refresh; call APIs with a token whose scopes or audience should not permit the action and confirm the resource server denies it.

## How to Fix

- Authorization server: assign each client only the scopes it needs, validate requested scopes against client policy, and never issue broader scopes on refresh than were originally granted.
- Resource server: check audience and scopes on every request; use least-privilege scopes, short-lived tokens, and clear consent. Map to API5:2023 and ASVS 5.0 V10 OAuth and OIDC (10.3.1, 10.3.2, 10.4.11, 10.7.2).

## References

- [RFC 9700: Best Current Practice for OAuth 2.0 Security](https://www.rfc-editor.org/rfc/rfc9700.html)
- [RFC 6749: The OAuth 2.0 Authorization Framework (section 6, refreshing an access token)](https://www.rfc-editor.org/rfc/rfc6749.html)
- [OWASP OAuth 2.0 Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/OAuth2_Cheat_Sheet.html)
- [OWASP API5:2023 Broken Function Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa5-broken-function-level-authorization/)
