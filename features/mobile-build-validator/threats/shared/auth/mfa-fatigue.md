---
id: mfa-fatigue
title: MFA Fatigue / Push Bombing
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-799, CWE-287]
severity: high
last_verified: 2026-10-29
aliases: [push-bombing, mfa-spamming]
---

# MFA Fatigue / Push Bombing

## Why It Is a Threat

With a valid password, an attacker triggers a flood of push approvals (sometimes paired with a call pretending to be IT support) until the user accepts one out of annoyance or confusion, defeating simple approve/deny push MFA. MITRE ATT&CK records real intrusion groups using this technique.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trigger repeated push prompts for a synthetic account; confirm whether rate limits, number matching, and anomaly detection prevent approval spam, and whether users can report an unexpected prompt.

## How to Fix

- Use number matching or another challenge that requires the user to see the login screen; rate-limit and cap pending prompts since the last successful login.
- Show context (app, location) in prompts, let users report and block suspicious requests, alert on rapid repeats, and prefer phishing-resistant factors (FIDO2/WebAuthn passkeys). Map to ASVS 5.0 V6 Authentication (6.6.4).

## References

- [MITRE ATT&CK T1621: Multi-Factor Authentication Request Generation](https://attack.mitre.org/techniques/T1621/)
- [CISA: Implementing Number Matching in MFA Applications](https://www.cisa.gov/sites/default/files/publications/fact-sheet-implement-number-matching-in-mfa-applications-508c.pdf)
- [OWASP Multifactor Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
