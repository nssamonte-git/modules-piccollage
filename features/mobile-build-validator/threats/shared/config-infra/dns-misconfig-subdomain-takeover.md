---
id: dns-misconfig-subdomain-takeover
title: Insecure DNS Config / Subdomain Takeover
category: config-infra
platforms: [shared, web, backend]
owasp_refs: [WSTG-CONF-10, A02:2025]
cwe: [CWE-672]
severity: high
last_verified: 2026-10-29
aliases: [insecure-dns-configuration, subdomain-takeover]
---

# Insecure DNS Config / Subdomain Takeover

## Why It Is a Threat

Dangling DNS records (CNAME, A/AAAA, or NS delegations) that still point to deprovisioned cloud or SaaS resources let an attacker claim that resource and serve content from your subdomain, enabling phishing, cookie theft, and bypass of domain-based trust such as CORS or CSP allowlists. Missing SPF/DKIM/DMARC and CAA records also weaken email and certificate-issuance security.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory DNS records and check each target still exists and is owned by you; look for provider "not found" responses and dangling NS delegations.
- Review SPF, DKIM, DMARC, and CAA records.

## How to Fix

- Delete DNS records before or when deprovisioning the target; create and verify the resource before pointing DNS at it; monitor zones for dangling entries.
- Publish SPF, DKIM, an enforcing DMARC policy, and CAA; protect DNS and registrar accounts with MFA. Map to A02:2025; see `dns-attacks`.

## References

- [OWASP WSTG: Test for Subdomain Takeover (WSTG-CONF-10)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management/10-Subdomain_Takeover)
- [Microsoft Learn: Prevent dangling DNS entries and avoid subdomain takeover](https://learn.microsoft.com/en-us/azure/security/fundamentals/subdomain-takeover)
- [AWS: Protection from dangling delegation records in Route 53](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/protection-from-dangling-dns.html)
- [CWE-672: Operation on a Resource after Expiration or Release](https://cwe.mitre.org/data/definitions/672.html)
