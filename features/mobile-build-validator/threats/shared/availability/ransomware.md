---
id: ransomware
title: Ransomware
category: availability
platforms: [shared, backend]
owasp_refs: [A06:2025]
cwe: [CWE-506]
severity: critical
last_verified: 2026-10-29
---

# Ransomware

## Why It Is a Threat

Attackers encrypt or destroy data and systems, often after stealing data for extortion, and demand payment, halting operations. They get in through compromised credentials, exposed remote services, unpatched edge devices, phishing, or supply-chain footholds, and deliberately target backups and recovery tooling.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate); readiness review, not deployment.

- Review backup isolation, immutability, and regular restore testing; check whether backups and their credentials are reachable from production admin accounts.
- Assess exposed services (RDP, VPN, edge devices), patch status, MFA coverage, and segmentation that limits spread.

## How to Fix

- Keep offline or immutable, encrypted backups and test restores regularly; separate backup credentials from production admin roles.
- Patch internet-facing systems promptly, enforce phishing-resistant MFA and least privilege, segment networks, and maintain a rehearsed incident response and recovery plan. Map to the infrastructure runbook and `detection-ops`.

## References

- [CISA #StopRansomware Guide](https://www.cisa.gov/stopransomware/ransomware-guide)
- [NIST IR 8374 Rev. 1: Ransomware Risk Management, a Cybersecurity Framework 2.0 Community Profile](https://csrc.nist.gov/pubs/ir/8374/r1/final)
- [MITRE ATT&CK T1486: Data Encrypted for Impact](https://attack.mitre.org/techniques/T1486/)
- [MITRE ATT&CK T1490: Inhibit System Recovery](https://attack.mitre.org/techniques/T1490/)
