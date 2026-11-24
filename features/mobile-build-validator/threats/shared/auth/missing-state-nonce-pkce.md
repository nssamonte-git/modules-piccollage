---
id: missing-state-nonce-pkce
title: Missing OAuth State, Nonce, or PKCE
category: auth
platforms: [shared, web, backend, android, ios]
owasp_refs: [WSTG-ATHZ-05, A07:2025, A01:2025]
cwe: [CWE-352, CWE-287]
severity: high
last_verified: 2026-10-29
---

# Missing OAuth State, Nonce, or PKCE

## Why It Is a Threat

Without CSRF protection (`state`, PKCE, or the OIDC `nonce`), an attacker can complete a victim's flow with the attacker's code (login CSRF, linking the victim to the attacker's account). Omitting `nonce` allows ID-token replay and code injection in OIDC; omitting PKCE allows authorization-code interception and injection, especially on public/mobile clients. Each can lead to account takeover.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Initiate auth flows without or with tampered `state` and `nonce`; confirm they are required, single-use, and validated against the user's session.
- Confirm the authorization server requires PKCE with `S256`, rejects `plain`, and rejects a `code_verifier` when no `code_challenge` was sent (PKCE downgrade).

## How to Fix

- Use PKCE (`S256`) for all clients (mandatory for public and mobile clients); also send a one-time `state` bound to the user agent, and `nonce` for OIDC, checking it in the ID token.
- Bind these values to the user session; when a client talks to several authorization servers, defend against mix-up (for example with the `iss` response parameter). Map to ASVS 5.0 V10 OAuth and OIDC (10.1.2, 10.2.1, 10.2.2, 10.4.6, 10.5.1) and RFC 9700.

## References

- [RFC 9700: Best Current Practice for OAuth 2.0 Security](https://www.rfc-editor.org/rfc/rfc9700.html)
- [RFC 7636: Proof Key for Code Exchange by OAuth Public Clients](https://www.rfc-editor.org/rfc/rfc7636.html)
- [OWASP WSTG-ATHZ-05: Testing for OAuth Weaknesses](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/05-Authorization/05-OAuth_Weaknesses)
- [OWASP OAuth2 Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/OAuth2_Cheat_Sheet.html)
- [CWE-352: Cross-Site Request Forgery (CSRF)](https://cwe.mitre.org/data/definitions/352.html)
