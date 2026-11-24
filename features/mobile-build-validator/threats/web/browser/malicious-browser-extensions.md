---
id: malicious-browser-extensions
title: Malicious Browser Extensions
category: web-browser
platforms: [web]
owasp_refs: [A08:2025]
cwe: [CWE-829]
severity: medium
last_verified: 2026-10-29
---

# Malicious Browser Extensions

## Why It Is a Threat

Extensions with broad host permissions can read and modify page content, inject scripts, and exfiltrate session tokens and form data across sites, outside your server's control. Legitimate extensions also turn malicious after an ownership change or a compromised developer account. Relevant to threat modeling for high-value apps, and to your own extension if you ship one.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- For threat modeling, assess what an extension with page access could read or change in sensitive flows (login, payment, admin).
- If you publish an extension, review its permissions and host permissions, any remotely loaded code, how its content scripts trust messages from web pages, and who can publish updates.

## How to Fix

- Minimize sensitive data in the DOM; use `HttpOnly` cookies and short-lived tokens; require step-up or out-of-band confirmation for critical actions; detect anomalous client behavior.
- For your own extension: request least permissions and host permissions, ship no remote code, validate messages from pages and content scripts, and protect the store publishing account with MFA.
- Map to A08:2025 and ASVS 5.0 V3.3.4 (`HttpOnly`); see `cookie-token-theft`.

## References

- [OWASP Browser Extension Vulnerabilities Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Browser_Extension_Vulnerabilities_Cheat_Sheet.html)
- [MITRE ATT&CK T1176.001: Software Extensions: Browser Extensions](https://attack.mitre.org/techniques/T1176/001/)
- [CWE-829: Inclusion of Functionality from Untrusted Control Sphere](https://cwe.mitre.org/data/definitions/829.html)
