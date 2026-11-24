---
id: idn-homograph
title: Internationalized-Domain Homograph Attack
category: web-url
platforms: [web]
owasp_refs: [A06:2025, A07:2025]
cwe: [CWE-1007, CWE-451]
severity: medium
last_verified: 2026-10-29
aliases: [homoglyph-domain, idn-spoofing]
---

# Internationalized-Domain Homograph Attack

## Why It Is a Threat

Attackers register lookalike domains using Unicode characters that visually resemble ASCII (e.g. Cyrillic "а" for Latin "a"), so phishing links and spoofed origins appear legitimate, defeating users' visual URL checks and enabling credential theft. The same confusion breaks app-side domain allowlists that compare display forms instead of canonical forms.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Where the app displays or trusts user-supplied domains or links, test rendering of mixed-script and punycode (`xn--`) domains, whether they are flagged, and whether allowlist checks compare canonical ASCII (A-label) forms.

## How to Fix

- Display punycode for mixed-script or confusable domains and warn on lookalikes; convert domains to their IDNA A-label form before any comparison or allowlist check.
- Register key lookalike variants, monitor new registrations and certificate issuance for your brand, and prefer phishing-resistant authentication (passkeys/WebAuthn are bound to the real origin).
- Map to ASVS 5.0 V6 Authentication and V1.1.1 (canonicalize before validation); see `phishing-social-engineering`.

## References

- [CAPEC-632: Homograph Attack via Homoglyphs](https://capec.mitre.org/data/definitions/632.html)
- [CWE-1007: Insufficient Visual Distinction of Homoglyphs Presented to User](https://cwe.mitre.org/data/definitions/1007.html)
- [RFC 5890: Internationalized Domain Names for Applications (IDNA): Definitions and Document Framework](https://www.rfc-editor.org/rfc/rfc5890.html)
