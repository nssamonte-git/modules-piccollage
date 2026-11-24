---
id: stolen-privileged-devices
title: Stolen Developer or Administrator Devices
category: detection-ops
platforms: [shared, web, backend, android, ios]
owasp_refs: [A07:2025, A09:2025]
cwe: [CWE-522, CWE-308]
severity: high
last_verified: 2026-10-29
---

# Stolen Developer or Administrator Devices

## Why It Is a Threat

A lost or stolen privileged laptop or phone exposes saved credentials, session tokens, source, signing keys, and cloud or CI access, a direct route to systems and the supply chain (`compromised-developer-devices`) if the device is weakly protected.

## How to Find or Test

Authorized testing only (posture review; follow the `smx-ai-vapt` authorization gate).

- Review disk encryption, screen lock, MDM and remote wipe, credential and token storage, and what a single device can access without re-authentication.

## How to Fix

- Enforce full-disk encryption, a strong lock, MDM with remote wipe, hardware-backed key storage, and short-lived credentials that require re-authentication with phishing-resistant MFA.
- Enable rapid revocation of the device's sessions, tokens, and keys on loss, and review access logs after the event. Map to A07:2025 and NIST SP 800-124r2.

## References

- [NIST SP 800-124 Rev. 2: Managing the Security of Mobile Devices in the Enterprise](https://csrc.nist.gov/pubs/sp/800/124/r2/final)
- [NIST SP 800-111: Storage Encryption Technologies for End User Devices](https://csrc.nist.gov/pubs/sp/800/111/final)
- [CWE-522: Insufficiently Protected Credentials](https://cwe.mitre.org/data/definitions/522.html)
