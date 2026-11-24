---
id: jwt-algorithm-confusion
title: JWT Algorithm Confusion
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-SESS-10, A07:2025, A04:2025]
cwe: [CWE-347, CWE-757]
severity: high
last_verified: 2026-10-29
aliases: [jwt-alg-none, jwt-key-confusion]
---

# JWT Algorithm Confusion

## Why It Is a Threat

The verifier honors the token's own `alg` header, allowing `alg:none` (no signature) or RS256-to-HS256 confusion (signing with the public key as an HMAC secret). A related flaw trusts keys named in the token itself (`jwk`, `jku`, `x5u`). Each yields attacker-forged, accepted tokens.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Submit a token with `alg:none`/empty signature; submit an HS256 token signed with the known RSA public key against an RS256-expecting endpoint; confirm rejection.
- Check whether embedded or remote key headers are accepted from untrusted sources (see backend `jwt-kid-handling`).
- Source review by stack (`source-code-vapt`): Python `jwt.decode` without an explicit `algorithms=[...]`; Rust `jsonwebtoken` validation that takes the algorithm from the token header.

## How to Fix

- Pin an allowlist of expected algorithms per token type on the server; never trust the token's `alg`; reject `none`. Avoid accepting both symmetric and asymmetric algorithms for the same issuer.
- Bind each key to one algorithm, load keys only from pre-configured trusted sources, and use a library that enforces the configured algorithm. Map to ASVS 5.0 V9 Self-contained Tokens (9.1.2, 9.1.3).

## References

- [RFC 8725: JSON Web Token Best Current Practices](https://www.rfc-editor.org/rfc/rfc8725.html)
- [OWASP WSTG-SESS-10: Testing JSON Web Tokens](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/10-JSON_Web_Tokens)
- [OWASP JSON Web Token Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_Cheat_Sheet.html)
- [CWE-347: Improper Verification of Cryptographic Signature](https://cwe.mitre.org/data/definitions/347.html)
