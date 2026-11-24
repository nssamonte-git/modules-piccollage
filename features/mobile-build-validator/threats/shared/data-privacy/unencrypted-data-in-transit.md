---
id: unencrypted-data-in-transit
title: Unencrypted Data in Transit
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-NETWORK, WSTG-CRYP-03]
cwe: [CWE-319]
severity: high
last_verified: 2026-10-29
---

# Unencrypted Data in Transit

## Why It Is a Threat

Sensitive data travels over cleartext or weak TLS, letting a network attacker read or modify it (`attacker-in-the-middle`). Includes HTTP, mixed content, downgrade or fallback to cleartext, internal service-to-service and database traffic, and disabled certificate validation.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Capture traffic and confirm TLS everywhere (including internal/API and mobile); check for cleartext endpoints, mixed content, missing HSTS, and old TLS versions or weak cipher suites.
- Review Android Network Security Config and iOS App Transport Security exceptions that allow cleartext; verify certificate validation is not disabled (`incorrect-certificate-validation` in `crypto`).
- Source review by stack (`source-code-vapt`): Android `usesCleartextTraffic="true"` or `cleartextTrafficPermitted="true"`; iOS `NSAllowsArbitraryLoads`; React Native and Flutter `http://` production base URLs.

## How to Fix

- Enforce TLS 1.2 or 1.3 with recommended cipher suites and no cleartext fallback for all external traffic, with HSTS on the web (ASVS 5.0 V12.1 and V12.2); encrypt internal service and database traffic too (ASVS 5.0 V12.3).
- Validate certificates strictly; remove cleartext exceptions from mobile network configs (MASVS-NETWORK-1); consider pinning for high-risk mobile clients only with backup pins and an update plan (MASVS-NETWORK-2).

## References

- [OWASP Transport Layer Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Transport_Layer_Security_Cheat_Sheet.html)
- [CWE-319: Cleartext Transmission of Sensitive Information](https://cwe.mitre.org/data/definitions/319.html)
- [OWASP WSTG v4.2 WSTG-CRYP-03: Testing for Sensitive Information Sent via Unencrypted Channels](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/09-Testing_for_Weak_Cryptography/03-Testing_for_Sensitive_Information_Sent_via_Unencrypted_Channels)
- [Android Developers: Cleartext communications](https://developer.android.com/privacy-and-security/risks/cleartext-communications)
