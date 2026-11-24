---
id: refresh-token-replay
title: Refresh-Token Replay
category: auth
platforms: [shared, web, backend, android, ios]
owasp_refs: [A07:2025]
cwe: [CWE-294, CWE-1464]
severity: high
last_verified: 2026-10-29
---

# Refresh-Token Replay

## Why It Is a Threat

A stolen refresh token is replayed to mint new access tokens, potentially indefinitely. Without sender-constraint or rotation with reuse detection, theft yields long-term, hard-to-detect account access.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Use a refresh token, then attempt to reuse the old (already-rotated) token; confirm reuse is detected and every refresh token in that grant is revoked.
- Check refresh-token absolute lifetime, binding to the client, and storage (browser JS, mobile storage).

## How to Fix

- For public clients, sender-constrain refresh tokens (DPoP or mTLS), or at least rotate them on every use with reuse detection that revokes the whole grant on replay.
- Give refresh tokens an absolute expiry and user-revocable grants; keep them out of browser JavaScript (backend-for-frontend) and in Keychain/Keystore on mobile. Map to ASVS 5.0 V10 OAuth and OIDC (10.1.1, 10.4.5, 10.4.8, 10.4.9) and RFC 9700.

## References

- [RFC 9700: Best Current Practice for OAuth 2.0 Security](https://www.rfc-editor.org/rfc/rfc9700.html)
- [RFC 9449: OAuth 2.0 Demonstrating Proof of Possession (DPoP)](https://www.rfc-editor.org/rfc/rfc9449.html)
- [OWASP OAuth2 Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/OAuth2_Cheat_Sheet.html)
- [CWE-294: Authentication Bypass by Capture-replay](https://cwe.mitre.org/data/definitions/294.html)
