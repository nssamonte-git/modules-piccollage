---
id: third-party-script-compromise
title: Third-Party Script / Tag-Manager Compromise
category: web-browser
platforms: [web]
owasp_refs: [A08:2025, A03:2025]
cwe: [CWE-829, CWE-494]
severity: high
last_verified: 2026-10-29
aliases: [tag-manager-compromise, magecart, supply-chain-js]
---

# Third-Party Script / Tag-Manager Compromise

## Why It Is a Threat

Analytics, ads, chat, and tag-manager scripts run with full page privileges. If the provider, its CDN, or your tag-manager account is compromised, attackers inject skimmers (Magecart) that steal form and payment data from every page that loads the script, with no change to your own code.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory third-party scripts and tag-manager containers (including scripts they load dynamically), who can publish to them, and which pages load them; check for SRI, CSP containment, and script change monitoring, especially on payment and login pages.

## How to Fix

- Minimize third-party scripts; pin static ones with SRI and constrain all of them with a strict CSP; keep payment and credential pages free of third-party JS or isolate it in sandboxed frames.
- Lock down tag-manager and vendor accounts (MFA, publishing approval, least privilege) and monitor deployed scripts for unauthorized changes. For card payment pages, PCI DSS v4 requires a script inventory with integrity assurance (req. 6.4.3) and change and tamper detection (req. 11.6.1).
- Map to A08:2025 and ASVS 5.0 V3.6.1 and V15.1.2 (component inventory); see `missing-sri`, `compromised-third-party-sdk`.

## References

- [OWASP Third Party JavaScript Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Third_Party_Javascript_Management_Cheat_Sheet.html)
- [OWASP Top 10 Client-Side Security Risks](https://owasp.org/www-project-top-10-client-side-security-risks/)
- [CWE-829: Inclusion of Functionality from Untrusted Control Sphere](https://cwe.mitre.org/data/definitions/829.html)
- [MDN: Subresource Integrity](https://developer.mozilla.org/en-US/docs/Web/Security/Defenses/Subresource_Integrity)
