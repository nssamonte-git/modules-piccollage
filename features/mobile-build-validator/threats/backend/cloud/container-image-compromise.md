---
id: container-image-compromise
title: Container Image Compromise
category: backend-cloud
platforms: [backend]
owasp_refs: [A08:2025, A03:2025]
cwe: [CWE-494, CWE-1395]
severity: high
last_verified: 2026-10-29
---

# Container Image Compromise

## Why It Is a Threat

A base or application image contains vulnerable or malicious layers or embedded secrets, or is swapped or tampered with in the registry, so every deployment runs attacker-influenced code. Attackers also plant backdoored images for persistence (MITRE ATT&CK T1525 Implant Internal Image). Closely tied to `artifact-tampering`, `vulnerable-dependencies`, and `poisoned-pipeline-execution`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Scan images for known vulnerabilities, embedded secrets, and untrusted base layers; check that image signatures and digest pinning are verified at deploy and who can push to the registry.

## How to Fix

- Use minimal, trusted base images; scan in CI and in the registry; sign images and verify signatures and digests at admission; use private, access-controlled registries.
- Generate SBOMs for images and rebuild regularly to pick up patches. Map to A08:2025, SLSA, and ASVS 5.0 V15.2 Security Architecture and Dependencies; see `artifact-tampering`.

## References

- [NIST SP 800-190: Application Container Security Guide](https://csrc.nist.gov/pubs/sp/800/190/final)
- [OWASP Docker Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Docker_Security_Cheat_Sheet.html)
- [OWASP Software Supply Chain Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Software_Supply_Chain_Security_Cheat_Sheet.html)
- [MITRE ATT&CK T1525: Implant Internal Image](https://attack.mitre.org/techniques/T1525/)
- [CWE-494: Download of Code Without Integrity Check](https://cwe.mitre.org/data/definitions/494.html)
