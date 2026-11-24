---
id: excessive-data-collection
title: Excessive Data Collection / Missing Consent
category: data-privacy
platforms: [shared, web, backend, android, ios]
owasp_refs: [A06:2025, MASVS-PRIVACY]
cwe: [CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [data-collection-without-consent]
---

# Excessive Data Collection / Missing Consent

## Why It Is a Threat

The app collects more personal data than needed, requests broader permissions than a feature requires, or collects/processes data without a lawful basis or user consent, increasing breach blast radius and creating regulatory liability (GDPR/CCPA and platform privacy rules). Embedded SDKs often collect data the developer did not intend or disclose.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Map collected data against the stated purpose; review permission requests, the iOS privacy manifest and App Store privacy details, the Google Play Data safety form, and SDK data collection.
- Confirm that consent gating, opt-in, and opt-out are honored end-to-end, including before SDKs initialize.

## How to Fix

- Apply data minimization: collect only what a feature needs, request permissions in context and at the lowest scope, and prefer system pickers over broad permissions (MASVS-PRIVACY-1; ASVS 5.0 V14.1.1 and V14.2.6).
- Gate collection behind valid consent and a clear purpose; document a lawful basis and keep privacy disclosures and platform declarations accurate (MASVS-PRIVACY-3).

## References

- [OWASP MASVS-PRIVACY](https://mas.owasp.org/MASVS/12-MASVS-PRIVACY/)
- [OWASP Top 10 Privacy Risks](https://owasp.org/www-project-top-10-privacy-risks/)
- [NIST Privacy Framework](https://www.nist.gov/privacy-framework)
- [Android Developers: Minimize your permission requests](https://developer.android.com/privacy-and-security/minimize-permission-requests)
