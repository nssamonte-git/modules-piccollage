---
id: artifact-tampering
title: Build Artifact Substitution / Tampering
category: supply-chain
platforms: [shared, web, backend, android, ios]
owasp_refs: [CICD-SEC-9, A08:2025, A03:2025]
cwe: [CWE-494, CWE-353, CWE-345]
severity: high
last_verified: 2026-10-29
aliases: [artifact-substitution, build-artifact-tampering]
---

# Build Artifact Substitution / Tampering

## Why It Is a Threat

Between build and deployment, artifacts (container images, packages, binaries, mobile app bundles) are swapped or modified in registries, storage, or transit, or uploaded by an account other than the trusted build, so a tampered artifact reaches production despite clean source.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review whether artifacts are signed and carry build provenance, and whether deploy steps verify signature, digest, and provenance before use; check who can write to registries and artifact storage.

## How to Fix

- Sign artifacts at build and verify signatures, digests, and provenance at deploy (`signature-verification-failure`); deploy by immutable digest, not mutable tags; promote the same artifact across environments instead of rebuilding.
- Restrict registry and storage write access to the build identity and generate SLSA build provenance. Map to CICD-SEC-9, A08:2025, and SLSA Build track.

## References

- [OWASP CICD-SEC-9: Improper Artifact Integrity Validation](https://owasp.org/www-project-top-10-ci-cd-security-risks/CICD-SEC-09-Improper-Artifact-Integrity-Validation)
- [SLSA v1.2: Verifying artifacts](https://slsa.dev/spec/v1.2/verifying-artifacts)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [CWE-494: Download of Code Without Integrity Check](https://cwe.mitre.org/data/definitions/494.html)
