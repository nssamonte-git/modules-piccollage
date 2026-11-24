---
id: cookie-token-theft
title: Cookie or Token Theft
category: auth
platforms: [shared, web, backend, android, ios]
owasp_refs: [WSTG-SESS-02, A07:2025, A04:2025]
cwe: [CWE-522, CWE-1004, CWE-614, CWE-539, CWE-315]
severity: high
last_verified: 2026-10-29
---

# Cookie or Token Theft

## Why It Is a Threat

Session cookies or bearer tokens are exposed through insecure transport, JS-readable storage, URLs, logs, or client-side storage, then stolen (XSS, MITM, infostealer malware, device access) and replayed for account takeover, often after MFA has already been satisfied.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check cookie attributes (`HttpOnly`, `Secure`, `SameSite`, `__Host-` prefix) and whether tokens live in `localStorage` or other JS-readable stores; inspect logs, URLs, and mobile storage for tokens.
- Confirm tokens are never sent over cleartext or leaked via `Referer`, and whether a replayed token from a new device or network is detected.
- Source review by stack (`source-code-vapt`): Laravel `config/session.php` with `secure`, `http_only`, or `same_site` off; PHP or CodeIgniter cookies without `cookie_secure`, `cookie_httponly`, or `session.cookie_samesite`.

## How to Fix

- Use `__Host-`-prefixed `HttpOnly`/`Secure`/`SameSite` cookies; keep OAuth tokens off the browser with a backend-for-frontend; on mobile use Keychain/Keystore.
- Enforce TLS and HSTS, short lifetimes, and sender-constrained tokens (DPoP or mTLS) where possible; detect replay from new contexts; mitigate XSS (`xss`). Map to ASVS 5.0 V3 Web Frontend Security (3.3), V10 OAuth and OIDC (10.1.1), and V14 Data Protection (14.2.1, 14.3.3).

## References

- [OWASP Cookie Theft Mitigation Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cookie_Theft_Mitigation_Cheat_Sheet.html)
- [OWASP WSTG-SESS-02: Testing for Cookies Attributes](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/06-Session_Management/02-Cookies_Attributes)
- [MITRE ATT&CK T1539: Steal Web Session Cookie](https://attack.mitre.org/techniques/T1539/)
- [RFC 9449: OAuth 2.0 Demonstrating Proof of Possession (DPoP)](https://www.rfc-editor.org/rfc/rfc9449.html)
- [CWE-1004: Sensitive Cookie Without 'HttpOnly' Flag](https://cwe.mitre.org/data/definitions/1004.html)
