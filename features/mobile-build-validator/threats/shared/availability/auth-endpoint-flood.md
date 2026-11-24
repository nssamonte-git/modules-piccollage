---
id: auth-endpoint-flood
title: Login / OTP Flooding
category: availability
platforms: [shared, web, backend]
owasp_refs: [API4:2023, A06:2025]
cwe: [CWE-799, CWE-400]
severity: medium
last_verified: 2026-10-29
aliases: [login-flood, otp-flood, sms-pumping]
---

# Login / OTP Flooding

## Why It Is a Threat

Flooding login, signup, password-reset, or OTP-send endpoints exhausts capacity and runs up per-message SMS, voice, and email costs. In SMS pumping, attackers drive OTP sends to number ranges that earn them revenue. Overlaps `brute-force`, `otp-brute-force`, and `cost-exhaustion`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- With a small, capped burst, check rate limits and anti-automation on login, signup, reset, and OTP-send per IP, per account, per destination number or email, and globally.
- Review messaging-provider spend limits, destination-country restrictions, resend cooldowns, and alerting on send-volume spikes.

## How to Fix

- Throttle each operation per client, per identifier, and per destination, with cooldowns between OTP resends; add bot mitigation (challenge or risk scoring) before any message is sent.
- Set provider spend limits or billing alerts, allow SMS only to countries you serve, and alert on unusual send volume or low verification-completion rates. Map to API4:2023, ASVS 5.0 V2 Validation and Business Logic (2.4.1 anti-automation) and ASVS 5.0 V6 Authentication (6.1.1); see `cost-exhaustion`.

## References

- [OWASP API4:2023 Unrestricted Resource Consumption](https://owasp.org/API-Security/editions/2023/en/0xa4-unrestricted-resource-consumption/)
- [CWE-799: Improper Control of Interaction Frequency](https://cwe.mitre.org/data/definitions/799.html)
- [OWASP Denial of Service Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Denial_of_Service_Cheat_Sheet.html)
