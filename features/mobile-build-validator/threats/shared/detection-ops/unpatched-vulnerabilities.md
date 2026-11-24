---
id: unpatched-vulnerabilities
title: Unpatched Vulnerabilities / Unsupported Systems
category: detection-ops
platforms: [shared, web, backend, android, ios]
owasp_refs: [A03:2025]
cwe: [CWE-1104, CWE-1329, CWE-1395]
severity: high
last_verified: 2026-10-29
aliases: [unsupported-operating-systems, end-of-life-software]
---

# Unpatched Vulnerabilities / Unsupported Systems

## Why It Is a Threat

Known-vulnerable, unpatched, or end-of-life software (OS, runtimes, frameworks, appliances, firmware) is exploited with public exploits, often within days of disclosure. EOL systems receive no security fixes, so exposure only grows over time.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory versions across the stack; flag missing patches, EOL components, known-exploited vulnerabilities, and patch latency against advisories.

## How to Fix

- Run patch management with risk-based SLAs, prioritizing known-exploited and internet-facing vulnerabilities; upgrade or replace EOL systems; automate updates and verify they applied.
- Where patching must wait, apply mitigations or virtual patches and track via SBOM and asset inventory (`missing-supply-chain-controls`). Map to A03:2025 and NIST SP 800-40r4.

## References

- [CISA Known Exploited Vulnerabilities Catalog](https://www.cisa.gov/known-exploited-vulnerabilities-catalog)
- [NIST SP 800-40 Rev. 4: Guide to Enterprise Patch Management Planning](https://csrc.nist.gov/pubs/sp/800/40/r4/final)
- [OWASP Top 10:2025 A03 Software Supply Chain Failures](https://top10.owasp.org/2025/A03_2025-Software_Supply_Chain_Failures/)
- [CWE-1104: Use of Unmaintained Third Party Components](https://cwe.mitre.org/data/definitions/1104.html)
