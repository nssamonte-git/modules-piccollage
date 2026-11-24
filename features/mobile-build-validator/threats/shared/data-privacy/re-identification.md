---
id: re-identification
title: Re-Identification of Anonymized Data
category: data-privacy
platforms: [shared, web, backend]
owasp_refs: [A06:2025]
cwe: [CWE-359]
severity: medium
last_verified: 2026-10-29
---

# Re-Identification of Anonymized Data

## Why It Is a Threat

"Anonymized" datasets retain quasi-identifiers (zip, birthdate, device id, rare attributes, precise timestamps or locations) that can be combined or linked with other data to re-identify individuals, turning a supposedly safe release, analytics export, or ML training set into a privacy breach. Hashing an identifier is pseudonymization, not anonymization.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review de-identification of exports, analytics, and training data for residual direct and quasi-identifiers and linkage keys.
- Assess linkage risk against plausible outside data, and check k-anonymity or aggregation thresholds and differential-privacy parameters where used.

## How to Fix

- Apply robust de-identification (generalization, suppression, aggregation, differential privacy with a documented privacy budget) and minimum cohort sizes; assess re-identification risk before each release.
- Treat pseudonymized data as still-personal; control and separate linkage keys; restrict access to detailed datasets (ASVS 5.0 V14.2.4 privacy-enhancing technologies).

## References

- [NIST SP 800-188: De-Identifying Government Datasets](https://csrc.nist.gov/pubs/sp/800/188/final)
- [NIST SP 800-226: Guidelines for Evaluating Differential Privacy Guarantees](https://csrc.nist.gov/pubs/sp/800/226/final)
- [CWE-359: Exposure of Private Personal Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/359.html)
