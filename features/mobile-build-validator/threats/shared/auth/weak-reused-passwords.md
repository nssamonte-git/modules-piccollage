---
id: weak-reused-passwords
title: Weak or Reused Passwords
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHN-07, A07:2025]
cwe: [CWE-521, CWE-1391]
severity: medium
last_verified: 2026-10-29
aliases: [reused-passwords, weak-passwords]
---

# Weak or Reused Passwords

## Why It Is a Threat

Users choose weak or previously-breached passwords, or reuse them across sites, making brute-force, spraying, and credential stuffing effective. The root enabler for many account takeovers.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the password policy: minimum and maximum length, blocklist and breached-password rejection, and absence of composition, hint, and forced-rotation rules.
- Attempt to set weak, common, context-specific, and breached passwords and confirm rejection; confirm paste and password managers work.

## How to Fix

- Follow NIST SP 800-63B-4: at least 15 characters when the password is the only factor (8 with MFA), allow at least 64, no composition rules or periodic rotation (force a change only on evidence of compromise), screen against common, context-specific, and breached lists, and allow password managers.
- Store passwords with a slow, salted hash (Argon2id, scrypt, bcrypt, or PBKDF2); encourage or require MFA. Map to ASVS 5.0 V6 Authentication (6.2).

## References

- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [OWASP WSTG-ATHN-07: Testing for Weak Authentication Methods](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/07-Weak_Authentication_Methods)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
- [CWE-521: Weak Password Requirements](https://cwe.mitre.org/data/definitions/521.html)
