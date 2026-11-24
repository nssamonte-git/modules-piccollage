---
id: password-reset-abuse
title: Password-Reset Abuse
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHN-09, A07:2025]
cwe: [CWE-640]
severity: high
last_verified: 2026-10-29
---

# Password-Reset Abuse

## Why It Is a Threat

Weak reset flows enable takeover: predictable, long-lived, or reusable reset tokens, Host-header poisoning of reset links, tokens leaked via `Referer` or logs, resets that skip enrolled MFA, or resets that do not invalidate sessions. An attacker resets the victim's password and takes the account.

## How to Find or Test

Authorized testing only, with synthetic accounts (follow the `smx-ai-vapt` authorization gate).

- Inspect token entropy, single use, and expiry; test Host-header/`X-Forwarded-Host` poisoning of the reset link and token leakage via `Referer`/logs.
- Confirm the response is the same for unknown accounts, reset does not bypass MFA, and reset invalidates existing sessions/tokens.

## How to Fix

- Use CSPRNG, single-use, short-lived tokens stored hashed and bound to the account; build links from a configured host, never the request Host header; set `Referrer-Policy: no-referrer` on the reset page.
- Keep MFA in the reset flow, do not log the user in automatically, end other sessions, and notify the user. Map to ASVS 5.0 V6 Authentication (6.4.3, 6.3.7) and V7 Session Management (7.4.3).

## References

- [OWASP Forgot Password Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html)
- [OWASP WSTG-ATHN-09: Testing for Weak Password Change or Reset Functionalities](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/09-Weak_Password_Change_or_Reset_Functionalities)
- [CWE-640: Weak Password Recovery Mechanism for Forgotten Password](https://cwe.mitre.org/data/definitions/640.html)
