---
id: jwt-signature-validation
title: JWT Signature-Validation Errors
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-SESS-10, A07:2025, A04:2025]
cwe: [CWE-347]
severity: high
last_verified: 2026-10-29
---

# JWT Signature-Validation Errors

## Why It Is a Threat

The application fails to verify a JWT correctly: accepting unsigned tokens, calling a decode function that skips verification, trusting claims before verifying, or skipping `exp`/`nbf`/`aud`/`iss`/type checks. Attackers then forge tokens or reuse tokens minted for another service (for example an ID token used as an access token) to bypass authentication and authorization.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Tamper with claims and resend; remove or alter the signature; submit expired, not-yet-valid, wrong-issuer, wrong-audience, or wrong-type tokens and confirm rejection.
- Source review by stack (`source-code-vapt`): Python `jwt.decode(..., options={"verify_signature": False})`; Rust `jsonwebtoken` `insecure_disable_signature_validation()` or `validate_exp = false`; PostgREST short or committed `jwt-secret`.

## How to Fix

- Verify the signature with the expected key before reading claims; enforce `exp`, `nbf`, `iss`, and `aud` against an allowlist, and check the token type (for example `typ: at+jwt` for access tokens) on every request.
- Use a vetted JWT library with a pinned algorithm. See `jwt-algorithm-confusion`. Map to ASVS 5.0 V9 Self-contained Tokens (9.1.1, 9.2.1-9.2.3).

## References

- [RFC 8725: JSON Web Token Best Current Practices](https://www.rfc-editor.org/rfc/rfc8725.html)
- [OWASP WSTG-SESS-10: Testing JSON Web Tokens](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/10-JSON_Web_Tokens)
- [RFC 9068: JSON Web Token (JWT) Profile for OAuth 2.0 Access Tokens](https://www.rfc-editor.org/rfc/rfc9068.html)
- [CWE-347: Improper Verification of Cryptographic Signature](https://cwe.mitre.org/data/definitions/347.html)
