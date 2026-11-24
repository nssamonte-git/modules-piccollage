---
id: unsafe-api-consumption
title: Unsafe Consumption of Third-Party APIs
category: backend-api-inventory
platforms: [backend]
owasp_refs: [API10:2023]
cwe: [CWE-20, CWE-200, CWE-319, CWE-400]
severity: high
last_verified: 2026-10-29
aliases: [unsafe-third-party-api-consumption, trusting-third-party-api-data, third-party-integration-trust]
---

# Unsafe Consumption of Third-Party APIs

## Why It Is a Threat

Unsafe API consumption happens when a backend trusts data from third-party APIs and integrations more than user input: it skips validation, follows their redirects blindly, talks to them over unencrypted channels, or accepts responses of any size and duration. An attacker who controls or compromises the upstream service, or who can plant data in it, turns that trust into injection in the consuming API, sensitive data sent to an attacker host via redirect, or denial of service through huge or slow responses. This card covers data and responses your API pulls from other services; forged inbound webhook events are in `webhook-security`, attacker-chosen fetch destinations are in `ssrf`, and compromised client code is in `compromised-third-party-sdk`.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Inventory every outbound integration (enrichment, payment, identity, maps, AI, SCM, partner APIs) and record transport, authentication, and how each response is used downstream.
- Review code paths where upstream fields reach SQL, shell, templates, file paths, or HTML without validation or encoding (see `sql-injection`).
- Check HTTP client settings: redirect following, TLS verification, connect and read timeouts, maximum response size, and retry limits.
- With an owner-approved mock of the upstream, return malformed, oversized, slow, and redirecting responses and confirm the API rejects or bounds them without leaking the original request to a new host.

## How to Fix

- Treat third-party responses as untrusted input: validate them against a strict schema and encode them for each sink (ASVS 5.0 V2 Validation and Business Logic, V1 Encoding and Sanitization).
- Use TLS with certificate validation for every integration (ASVS 5.0 V12 Secure Communication).
- Disable automatic redirects, or follow only to an allowlist of known hosts, and never resend credentials or sensitive bodies to a new origin.
- Set timeouts, response size caps, and retry and concurrency limits; isolate failures with circuit breakers (ASVS 5.0 V4 API and Web Service).
- Assess providers' security posture, scope their credentials, and monitor integrations for anomalous responses.

## References

- [OWASP API Security Top 10 2023 API10 Unsafe Consumption of APIs](https://owasp.org/API-Security/editions/2023/en/0xaa-unsafe-consumption-of-apis/)
- [CWE-20: Improper Input Validation](https://cwe.mitre.org/data/definitions/20.html)
- [CWE-400: Uncontrolled Resource Consumption](https://cwe.mitre.org/data/definitions/400.html)
