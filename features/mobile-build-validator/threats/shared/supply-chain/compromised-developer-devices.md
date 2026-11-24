---
id: compromised-developer-devices
title: Compromised Developer Devices
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025, A08:2025]
cwe: [CWE-506, CWE-522]
severity: high
last_verified: 2026-10-29
---

# Compromised Developer Devices

## Why It Is a Threat

A developer's machine holds source, signing keys, package-publishing tokens, cloud and CI credentials, and push access. If compromised (malware, a malicious package or IDE extension, a stolen device), attackers inject code, steal secrets, and push trusted changes, a direct path into the supply chain. Device-theft variant: `stolen-privileged-devices`.

## How to Find or Test

Authorized testing only (posture review; follow the `smx-ai-vapt` authorization gate).

- Review device hardening (disk encryption, patching, MDM, EDR), where credentials and tokens are stored, and what a single developer machine can access, publish, or push.

## How to Fix

- Enforce device encryption, patching, MDM, and EDR; keep secrets in managed stores and use short-lived, scoped credentials instead of long-lived tokens.
- Require phishing-resistant MFA and signed commits; keep release signing and publishing in controlled CI rather than on developer machines; require review so no single machine can ship to production. Map to A03:2025 and NIST SSDF (PO.5).

## References

- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [NIST SP 800-218: Secure Software Development Framework (SSDF)](https://csrc.nist.gov/pubs/sp/800/218/final)
- [CAPEC-511: Infiltration of Software Development Environment](https://capec.mitre.org/data/definitions/511.html)
- [CISA: Widespread Supply Chain Compromise Impacting npm Ecosystem](https://www.cisa.gov/news-events/alerts/2025/09/23/widespread-supply-chain-compromise-impacting-npm-ecosystem)
