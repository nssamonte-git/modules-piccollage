---
id: email-phone-change-abuse
title: Email-Change or Phone-Change Abuse
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-620, CWE-640]
severity: high
last_verified: 2026-10-29
---

# Email-Change or Phone-Change Abuse

## Why It Is a Threat

If changing the account email or phone does not require re-authentication and verification of the new and old contact, an attacker with a hijacked session can redirect recovery and OTPs to their own contact and lock out the owner, a common takeover pivot.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Attempt to change email/phone without re-auth; check whether the new contact is verified before it takes effect, the old contact is notified, and sessions/MFA are re-validated.

## How to Fix

- Require full re-authentication (including MFA where enrolled) before changing email, phone, or other recovery data; treat a new phone number as binding a new authenticator.
- Confirm the new contact before switching, notify the old contact with a way to reverse the change, and offer to end other sessions. Map to ASVS 5.0 V7 Session Management (7.5.1, 7.4.3) and V6 Authentication (6.3.7).

## References

- [OWASP Authentication Cheat Sheet: Changing a User's Registered Email Address](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [CWE-620: Unverified Password Change](https://cwe.mitre.org/data/definitions/620.html)
- [CWE-640: Weak Password Recovery Mechanism for Forgotten Password](https://cwe.mitre.org/data/definitions/640.html)
