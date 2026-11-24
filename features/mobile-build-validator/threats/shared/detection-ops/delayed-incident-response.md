---
id: delayed-incident-response
title: Delayed or Missing Incident Response
category: detection-ops
platforms: [shared, web, backend]
owasp_refs: [A09:2025]
cwe: []
severity: medium
last_verified: 2026-10-29
---

# Delayed or Missing Incident Response

## Why It Is a Threat

Without a tested incident-response plan, defined roles, and playbooks, detected incidents are handled slowly and inconsistently, increasing dwell time, damage, data loss, and regulatory exposure (including missed breach-notification deadlines).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review the IR plan, on-call coverage, severity classification, communication paths, and playbooks; run a tabletop exercise and measure time to detect, respond, and contain.

## How to Fix

- Maintain an IR plan with defined roles, severities, legal and customer communications, and playbooks; rehearse with tabletops and game days.
- Integrate with alerting, and feed post-incident lessons back into detections and controls. Map to A09:2025, NIST SP 800-61r3, and the `security-review` incident-response-review reference.

## References

- [NIST SP 800-61 Rev. 3: Incident Response Recommendations and Considerations](https://csrc.nist.gov/pubs/sp/800/61/r3/final)
- [CISA: Incident Response Plan (IRP) Basics](https://www.cisa.gov/resources-tools/resources/incident-response-plan-irp-basics)
- [OWASP Top 10:2025 A09 Security Logging and Alerting Failures](https://top10.owasp.org/2025/A09_2025-Security_Logging_and_Alerting_Failures/)
