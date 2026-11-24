---
id: otp-brute-force
title: OTP Brute Forcing
category: auth
platforms: [shared, web, backend]
owasp_refs: [WSTG-ATHN-11, A07:2025]
cwe: [CWE-307, CWE-330]
severity: high
last_verified: 2026-10-29
---

# OTP Brute Forcing

## Why It Is a Threat

Short numeric OTPs (often 4-6 digits) with weak rate limiting can be guessed by trying many values, bypassing MFA or phone/email verification. Resending a fresh code that also resets the failure counter, or racing parallel requests, multiplies the attacker's guesses.

## How to Find or Test

Authorized testing only, with a synthetic account (follow the `smx-ai-vapt` authorization gate).

- Submit many OTP guesses and confirm attempts are capped, the code is invalidated after N failures, the counter survives a resend, and parallel submissions are counted.
- Check code length, randomness, single use, and validity window.

## How to Fix

- Limit failed attempts per account and authenticator (never reset the count when a new code is sent); invalidate a code after a few failures; generate codes with a CSPRNG (at least 6 digits) and make them single-use.
- Keep lifetimes short (at most 10 minutes for out-of-band codes; one TOTP step); add per-IP throttling and anomaly alerts. Map to ASVS 5.0 V6 Authentication (6.5.1, 6.5.3-6.5.5, 6.6.3).

## References

- [OWASP Multifactor Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)
- [OWASP WSTG-ATHN-11: Testing Multi-Factor Authentication](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/11-Multi-Factor_Authentication)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [CWE-307: Improper Restriction of Excessive Authentication Attempts](https://cwe.mitre.org/data/definitions/307.html)
