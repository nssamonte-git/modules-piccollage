---
id: otp-interception
title: OTP Interception
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-287, CWE-319]
severity: high
last_verified: 2026-10-29
---

# OTP Interception

## Why It Is a Threat

One-time passcodes sent via SMS, voice, or email are intercepted (SS7 abuse, SIM swap, number porting, malware reading SMS or notifications, mailbox compromise) or relayed by real-time phishing proxies, letting an attacker complete MFA. NIST treats SMS/voice OTP as a restricted authenticator and does not allow email for out-of-band authentication.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review OTP delivery channels and whether OTPs are reusable, long-lived, bound to the login attempt, or shown in lock-screen notifications/logs; assess real-time phishing-proxy resistance.

## How to Fix

- Prefer phishing-resistant factors (FIDO2/WebAuthn passkeys), then authenticator-app TOTP, over SMS or email; if SMS remains, offer a stronger alternative and warn users of the risk.
- Keep OTPs short-lived, single-use, rate-limited, and bound to the transaction; never log or echo them; consider SIM-change or porting signals before sending. See `sim-swapping`. Map to ASVS 5.0 V6 Authentication (6.6.1, 6.6.2, 6.3.6).

## References

- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [MITRE ATT&CK T1111: Multi-Factor Authentication Interception](https://attack.mitre.org/techniques/T1111/)
- [OWASP Multifactor Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)
- [CWE-287: Improper Authentication](https://cwe.mitre.org/data/definitions/287.html)
