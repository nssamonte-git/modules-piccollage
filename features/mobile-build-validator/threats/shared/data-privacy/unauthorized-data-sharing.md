---
id: unauthorized-data-sharing
title: Unauthorized Data Sharing / Third-Party Tracking
category: data-privacy
platforms: [shared, web, android, ios]
owasp_refs: [A06:2025, MASVS-PRIVACY]
cwe: [CWE-359]
severity: medium
last_verified: 2026-10-29
aliases: [third-party-tracking]
---

# Unauthorized Data Sharing / Third-Party Tracking

## Why It Is a Threat

Personal data is shared with third parties (analytics, ad networks, SDKs, tag managers, partners, AI providers) without disclosure or consent, or beyond the stated purpose — a privacy violation and frequent source of regulatory and platform-policy breaches. Third-party scripts and SDKs can also collect data on their own.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory outbound data flows, embedded SDKs, and third-party scripts; capture traffic to identify third-party recipients and the data sent.
- Compare against the privacy policy, platform declarations (iOS privacy manifest, Play Data safety), and consent state, including before consent is given.

## How to Fix

- Do not send defined sensitive data to untrusted parties such as trackers (ASVS 5.0 V14.2.3); disclose and gate third-party sharing behind consent; minimize and contractually constrain shared data.
- Remove unnecessary trackers and SDKs; honor opt-out signals such as Global Privacy Control (`Sec-GPC`; the older Do-Not-Track header is deprecated); keep platform privacy declarations accurate (MASVS-PRIVACY-3).

## References

- [OWASP MASVS-PRIVACY](https://mas.owasp.org/MASVS/12-MASVS-PRIVACY/)
- [OWASP Third Party JavaScript Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Third_Party_Javascript_Management_Cheat_Sheet.html)
- [Apple Developer: Privacy manifest files](https://developer.apple.com/documentation/bundleresources/privacy-manifest-files)
- [MDN: Sec-GPC header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Sec-GPC)
