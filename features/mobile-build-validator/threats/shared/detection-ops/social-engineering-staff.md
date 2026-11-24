---
id: social-engineering-staff
title: Social Engineering of Staff
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-640, CWE-308]
severity: high
last_verified: 2026-10-29
aliases: [help-desk-social-engineering, vishing]
---

# Social Engineering of Staff

## Why It Is a Threat

Attackers manipulate employees and help desks (phishing, vishing, pretexting, SIM-swap and MFA-reset requests, MFA push fatigue) to gain access or trigger privileged actions; help-desk impersonation is behind many major breaches. Targets internal staff rather than end users (`phishing-social-engineering`).

## How to Find or Test

Authorized testing only, with explicit approval for any staff-directed simulation (follow the `smx-ai-vapt` authorization gate).

- Review help-desk identity-verification procedures for password, MFA, and device resets; assess phishing resistance of staff MFA and approval controls for sensitive internal actions.

## How to Fix

- Harden help-desk verification (no resets on knowledge factors or caller ID alone; verify through a trusted channel or manager approval); require strong verification and out-of-band approval for sensitive changes, and alert on MFA resets.
- Use phishing-resistant MFA (FIDO2/WebAuthn passkeys or PKI) for staff and run awareness training. Map to A07:2025 and ASVS 5.0 V6.4 Authentication Factor Lifecycle and Recovery.

## References

- [CISA AA23-320A: Scattered Spider](https://www.cisa.gov/news-events/cybersecurity-advisories/aa23-320a)
- [CAPEC-407: Pretexting](https://capec.mitre.org/data/definitions/407.html)
- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [OWASP Multifactor Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Multifactor_Authentication_Cheat_Sheet.html)
