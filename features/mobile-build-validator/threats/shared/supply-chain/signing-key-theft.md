---
id: signing-key-theft
title: Signing-Key or Certificate Theft
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, A08:2025]
cwe: [CWE-321, CWE-522]
severity: critical
last_verified: 2026-10-29
aliases: [certificate-theft, code-signing-key-theft]
---

# Signing-Key or Certificate Theft

## Why It Is a Threat

Theft of code-signing keys or certificates (including mobile app signing and upload keys) lets attackers sign malware or updates that operating systems, clients, and stores trust, defeating signature verification and enabling wide distribution of trusted-looking malicious software.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review where signing keys live, who and what can use them, and whether they are in an HSM or KMS with audit logging; check for keys or keystores in CI variables, repos, and developer machines.

## How to Fix

- Keep signing keys in an HSM or KMS (or a managed store such as Play App Signing) with strict access and audit; never store them on developer machines or as plaintext CI secrets; have a tested revocation and rotation plan.
- Prefer short-lived signing identities with transparency logs, and sign only from a controlled release pipeline. Map to A04:2025, A08:2025, and ASVS 5.0 V11 Cryptography and V13.3 Secret Management.

## References

- [OWASP Key Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Key_Management_Cheat_Sheet.html)
- [MITRE ATT&CK T1553.002: Subvert Trust Controls: Code Signing](https://attack.mitre.org/techniques/T1553/002/)
- [CAPEC-206: Signing Malicious Code](https://capec.mitre.org/data/definitions/206.html)
- [Android Developers: Sign your app](https://developer.android.com/studio/publish/app-signing)
