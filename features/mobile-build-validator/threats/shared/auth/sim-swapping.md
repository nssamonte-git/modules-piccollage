---
id: sim-swapping
title: SIM Swapping
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-1390, CWE-287]
severity: high
last_verified: 2026-10-29
---

# SIM Swapping

## Why It Is a Threat

An attacker convinces or bribes a mobile carrier to move the victim's number to a new SIM or port it out, then receives SMS OTPs and recovery calls, defeating phone-based MFA and recovery. The carrier is outside your control, but relying on phone numbers for security creates the exposure.

## How to Find or Test

Authorized testing only (process review, not carrier attacks; follow the `smx-ai-vapt` authorization gate).

- Review where phone numbers act as an authentication or recovery factor; assess exposure if the number is attacker-controlled, including whether a phone-number change is treated as a high-risk event.

## How to Fix

- Do not use SMS/voice as the only MFA or recovery factor; offer and promote authenticator apps and FIDO2/WebAuthn passkeys, and drop phone options for high-assurance accounts.
- Where carriers expose SIM-change or porting signals, check them before sending codes; add step-up, delays, and notifications for contact changes; detect new-device plus contact-change combinations. Map to ASVS 5.0 V6 Authentication (6.6.1).

## References

- [MITRE ATT&CK T1451: SIM Card Swap](https://attack.mitre.org/techniques/T1451/)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [OWASP Multifactor Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)
- [CWE-1390: Weak Authentication](https://cwe.mitre.org/data/definitions/1390.html)
