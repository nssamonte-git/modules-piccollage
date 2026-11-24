---
id: attacker-in-the-middle
title: Attacker-in-the-Middle
category: network
platforms: [shared, web, backend, android, ios]
owasp_refs: [A04:2025, MASVS-NETWORK]
cwe: [CWE-300, CWE-319]
severity: high
last_verified: 2026-10-29
aliases: [mitm, rogue-wifi-access-point, certificate-substitution, traffic-interception]
---

# Attacker-in-the-Middle

## Why It Is a Threat

An attacker positioned between client and server (rogue Wi-Fi, ARP/DNS poisoning, malicious proxy, substituted certificate) reads or alters traffic. Succeeds when transport is cleartext or certificate validation is weak (`unencrypted-data-in-transit`, `incorrect-certificate-validation`).

## How to Find or Test

Authorized testing only, on an owner-authorized network/device (follow the `smx-ai-vapt` authorization gate).

- Route client traffic through an intercepting proxy with a substituted cert; confirm whether the client detects/refuses; check for any cleartext, HTTP-to-HTTPS redirect without HSTS, or pinning gaps.
- On mobile, review Android Network Security Configuration (cleartext is off by default from Android 9) and iOS App Transport Security exceptions.

## How to Fix

- Enforce TLS everywhere with strict certificate + hostname validation, with no fallback to cleartext; consider pinning for high-risk mobile clients.
- Send HSTS (consider preload) and detect downgrade (`cryptographic-downgrade`). Map to ASVS 5.0 V12 Secure Communication (12.2.1, 12.3.1, 12.3.2), V3 (3.4.1 HSTS), and MASVS-NETWORK-1.

## References

- [OWASP Transport Layer Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Transport_Layer_Security_Cheat_Sheet.html)
- [OWASP MASVS-NETWORK](https://mas.owasp.org/MASVS/08-MASVS-NETWORK/)
- [CAPEC-94: Adversary in the Middle (AiTM)](https://capec.mitre.org/data/definitions/94.html)
- [CWE-300: Channel Accessible by Non-Endpoint](https://cwe.mitre.org/data/definitions/300.html)
