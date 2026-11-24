---
id: phishing-social-engineering
title: Phishing and Social Engineering
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-1390, CWE-451]
severity: high
last_verified: 2026-10-29
---

# Phishing and Social Engineering

## Why It Is a Threat

Attackers trick users into revealing credentials or approving access via fake login pages, real-time adversary-in-the-middle proxies (which relay passwords and OTP/push MFA and capture the resulting session cookie), OAuth consent phishing, and help-desk manipulation. A leading cause of account compromise; passwords, OTPs, and push approvals are not phishing-resistant.

## How to Find or Test

Authorized testing only, with explicit approval for any user-directed simulation (follow the `smx-ai-vapt` authorization gate).

- Assess resistance to credential-relay proxies (is the authenticator bound to the origin?), look-alike domains, and OAuth consent phishing.
- Review user-facing anti-phishing cues, admin consent controls, and help-desk identity verification.

## How to Fix

- Offer, and for staff require, phishing-resistant authentication (FIDO2/WebAuthn passkeys or PKI) that binds to the origin; restrict which OAuth apps users can consent to (see `oauth-device-code-phishing`).
- Limit what a stolen session can do (short sessions, re-auth for sensitive actions, anomaly detection); train users; publish SPF/DKIM/DMARC for your domains. Map to ASVS 5.0 V6 Authentication (6.3.3) and V10 OAuth and OIDC (10.7).

## References

- [NIST SP 800-63B-4: Authentication and Authenticator Management](https://csrc.nist.gov/pubs/sp/800/63/b/4/final)
- [W3C Web Authentication Level 3](https://www.w3.org/TR/webauthn-3/)
- [MITRE ATT&CK T1566: Phishing](https://attack.mitre.org/techniques/T1566/)
- [CWE-1390: Weak Authentication](https://cwe.mitre.org/data/definitions/1390.html)
