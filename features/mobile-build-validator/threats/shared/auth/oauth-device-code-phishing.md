---
id: oauth-device-code-phishing
title: OAuth Device Code Phishing
category: auth
platforms: [shared, web, backend]
owasp_refs: [A07:2025]
cwe: [CWE-451, CWE-346]
severity: high
last_verified: 2026-10-29
aliases: [device-code-phishing, device-authorization-grant-abuse, device-code-flow-abuse]
---

# OAuth Device Code Phishing

## Why It Is a Threat

Device code phishing abuses the OAuth 2.0 device authorization grant (RFC 8628): the attacker starts the device code flow on their own machine, then persuades the victim to enter the user code at the real identity provider's verification page, so the victim's legitimate sign-in, including MFA, issues access and refresh tokens to the attacker. Because the page and the MFA prompt are genuine, look-alike-domain checks and many MFA methods do not stop it, and the resulting refresh tokens can keep access until revoked. This differs from `phishing-social-engineering`, which covers credential capture and fake login pages, and from `oauth-oidc-misconfiguration`, which covers flaws in how an app implements OAuth; here the protocol works as designed and the user is tricked into approving a device they do not hold.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate). Any user-directed simulation needs explicit written approval.

- Determine which identity providers and client applications accept the device authorization grant, and whether it is needed at all.
- In Microsoft Entra ID, check whether Conditional Access uses the authentication flows condition to block device code flow, and filter sign-in logs by the device code authentication protocol to find current use.
- Review the verification page: does it say a device is being authorized, show device and client details, and ask the user to confirm they hold the device?
- Check user code lifetime, refresh token lifetime, and whether risky or unexpected device code sign-ins raise alerts.

## How to Fix

- Block the device code flow wherever possible and allow it only for documented cases, such as specific meeting-room devices on known networks (ASVS 5.0 V10 OAuth and OIDC).
- On authorization servers you run, follow RFC 8628 security considerations: warn that a device is being authorized, display device information, ask the user to confirm possession, and keep user codes short-lived.
- Require compliant or managed devices and phishing-resistant authentication for sensitive resources, and limit scopes granted to device-flow clients.
- Alert on unusual device code sign-ins, and be ready to revoke refresh tokens and sessions quickly (see `missing-token-revocation`).

## References

- [RFC 8628: OAuth 2.0 Device Authorization Grant, Section 5.4 Remote Phishing](https://www.rfc-editor.org/rfc/rfc8628.html#section-5.4)
- [Microsoft Learn: Authentication flows as a condition in Conditional Access policy](https://learn.microsoft.com/en-us/entra/identity/conditional-access/concept-authentication-flows)
- [Microsoft Learn: Block authentication flows with Conditional Access policy](https://learn.microsoft.com/en-us/entra/identity/conditional-access/policy-block-authentication-flows)
- [OWASP Top 10:2025 A07 Authentication Failures](https://top10.owasp.org/2025/A07_2025-Authentication_Failures/)
