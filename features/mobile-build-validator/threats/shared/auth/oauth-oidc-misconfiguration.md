---
id: oauth-oidc-misconfiguration
title: OAuth / OIDC Misconfiguration
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHZ-05, A07:2025, A02:2025]
cwe: [CWE-1390, CWE-287, CWE-601]
severity: high
last_verified: 2026-10-29
aliases: [oauth-misconfig, oidc-misconfig]
---

# OAuth / OIDC Misconfiguration

## Why It Is a Threat

Misconfigured OAuth/OIDC (loose `redirect_uri` matching, open redirectors, implicit or password grants, reusable or long-lived codes, missing token-audience checks, over-broad scopes, unverified ID tokens, or identifying users by a mutable email instead of `sub`) enables authorization-code theft, token leakage, and account takeover.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Test `redirect_uri` validation (open redirect, subdomain/path tricks), allowed grant and response types, scope handling, and whether ID/access tokens are validated for `aud`/`iss`/signature.
- Check authorization-code reuse and lifetime, and PKCE enforcement (see `missing-state-nonce-pkce`).

## How to Fix

- Register redirect URIs and match them exactly; use the authorization-code flow with PKCE; disable the implicit and resource-owner password grants; make codes single-use and short-lived (10 minutes at most).
- Validate ID tokens (`aud` equals `client_id`, `iss`, signature) and audience-restrict access tokens, preferably sender-constrained (DPoP/mTLS); identify users by issuer plus `sub`; grant least-privilege scopes. Map to ASVS 5.0 V10 OAuth and OIDC (10.4.1-10.4.4, 10.5.2, 10.5.4) and RFC 9700.

## References

- [RFC 9700: Best Current Practice for OAuth 2.0 Security](https://www.rfc-editor.org/rfc/rfc9700.html)
- [OWASP WSTG-ATHZ-05: Testing for OAuth Weaknesses](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/05-Authorization/05-OAuth_Weaknesses)
- [OWASP OAuth2 Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/OAuth2_Cheat_Sheet.html)
- [OpenID Connect Core 1.0](https://openid.net/specs/openid-connect-core-1_0.html)
- [CWE-1390: Weak Authentication](https://cwe.mitre.org/data/definitions/1390.html)
