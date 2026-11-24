---
id: metadata-service-exposure
title: Cloud Metadata-Service Exposure
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025, A01:2025]
cwe: [CWE-918, CWE-441]
severity: high
last_verified: 2026-10-29
---

# Cloud Metadata-Service Exposure

## Why It Is a Threat

The cloud instance metadata endpoint (link-local `169.254.169.254`) returns temporary role credentials and configuration. If an app SSRF (`ssrf`), open proxy, or compromised container can reach it, an attacker steals instance or workload credentials and pivots into the cloud account. AWS IMDSv1 is the easiest target because it needs no session token.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check whether SSRF or proxy paths can reach the metadata endpoint and whether they can set the custom headers some providers require.
- Verify the instance metadata options (IMDSv2 required, PUT response hop limit) and whether containers or pods can reach the node's metadata service.

## How to Fix

- On AWS, require IMDSv2 and keep the hop limit at 1 unless containers need 2; disable the metadata service where unused; block app and pod egress to link-local addresses and give pods their own workload identity instead of the node role.
- Scope instance roles to least privilege so stolen credentials are limited. Map to A01:2025 (SSRF), ASVS 5.0 V13.2 (outbound allowlists), and `ssrf`.

## References

- [AWS: Use the Instance Metadata Service to access instance metadata](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/configuring-instance-metadata-service.html)
- [MITRE ATT&CK T1552.005: Cloud Instance Metadata API](https://attack.mitre.org/techniques/T1552/005/)
- [OWASP Server Side Request Forgery Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html)
- [CWE-918: Server-Side Request Forgery (SSRF)](https://cwe.mitre.org/data/definitions/918.html)
