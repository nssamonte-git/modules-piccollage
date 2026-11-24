---
id: account-recovery-takeover
title: Account-Recovery Takeover
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025, WSTG-ATHN-08]
cwe: [CWE-640]
severity: high
last_verified: 2026-10-29
---

# Account-Recovery Takeover

## Why It Is a Threat

Recovery mechanisms (security questions, backup email/phone, help-desk overrides, recovery codes) are often weaker than primary authentication. Attackers exploit guessable or researchable answers, attacker-controlled recovery contacts, or social-engineered support staff to seize accounts, often bypassing enrolled MFA.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review every recovery path for knowledge-based questions, the ability to add or swap recovery contacts without re-authentication, and manual support-override procedures.
- Confirm recovery cannot skip enrolled MFA, recovery codes are single-use, and the user is notified on all bound channels.

## How to Fix

- Remove security questions and password hints; recover through pre-enrolled factors or single-use, randomly generated recovery codes stored hashed. If an MFA factor is lost, re-verify identity at the same level as at enrollment.
- Never let recovery bypass enabled MFA; notify the user, offer to end other sessions, and script help-desk identity checks against social engineering. Map to ASVS 5.0 V6 Authentication (6.4) and V7 Session Management (7.4.3).

## References

- [OWASP Forgot Password Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html)
- [OWASP WSTG-ATHN-08: Testing for Weak Security Question Answer](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/04-Authentication/08-Weak_Security_Question_Answer)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [CWE-640: Weak Password Recovery Mechanism for Forgotten Password](https://cwe.mitre.org/data/definitions/640.html)
