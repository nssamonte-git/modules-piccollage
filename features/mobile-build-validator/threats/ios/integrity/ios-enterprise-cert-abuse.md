---
id: ios-enterprise-cert-abuse
title: Enterprise-Certificate / Provisioning Abuse
category: ios-integrity
platforms: [ios]
owasp_refs: [MASVS-RESILIENCE, A08:2025, M2:2024]
cwe: [CWE-522, CWE-732]
severity: high
last_verified: 2026-10-29
aliases: [provisioning-profile-compromise, signing-certificate-theft-ios, enterprise-certificate-abuse]
---

# Enterprise-Certificate / Provisioning Abuse

## Why It Is a Threat

Stolen or misused Apple Developer Enterprise Program certificates, distribution certificates, and provisioning profiles let attackers sign apps that devices trust and sideload them outside the App Store. Attackers use this to spread trojanized or cloned apps at scale. The enterprise program covers only in-house distribution to an organization's own employees, so public distribution breaks Apple's terms. Apple can then revoke the certificate, which breaks every legitimate internal app signed with it. Signing-key theft is what makes this possible (`signing-key-theft`).

## How to Find or Test

Authorized testing only (posture review; follow the `smx-ai-vapt` authorization gate).

- Review where signing certificates, private keys, and provisioning profiles are stored (developer Macs, CI secrets, shared drives) and who can export or use them.
- Check whether enterprise-signed builds are reachable from public links, and whether there is governance over enterprise certificate use.

## How to Fix

- Keep signing keys in a protected store (an HSM, cloud signing, or a locked-down CI keychain) with least-privilege access and audit logs. Never keep exportable `.p12` files on developer machines or in plaintext CI variables.
- Revoke and reissue keys quickly after any exposure.
- Use the enterprise program only for internal apps, distributed through MDM to managed devices. Use the App Store, custom apps, or TestFlight for everyone else.
- Map to A08:2025, M2:2024, and `signing-key-theft`.

## References

- [Apple Developer Enterprise Program](https://developer.apple.com/programs/enterprise/)
- [Apple Platform Security: App code signing process in iOS, iPadOS, tvOS, visionOS, and watchOS](https://support.apple.com/guide/security/app-code-signing-process-sec7c917bf14/web)
- [OWASP Mobile Top 10 2024 M2: Inadequate Supply Chain Security](https://owasp.org/www-project-mobile-top-10/2023-risks/m2-inadequate-supply-chain-security)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
