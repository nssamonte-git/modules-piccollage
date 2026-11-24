---
id: proxy-manipulation
title: Proxy Manipulation
category: network
platforms: [shared, web, backend, android, ios]
owasp_refs: [A01:2025, A04:2025]
cwe: [CWE-441, CWE-348]
severity: medium
last_verified: 2026-10-29
---

# Proxy Manipulation

## Why It Is a Threat

An attacker forces traffic through a malicious proxy (rogue system/PAC proxy settings, `HTTP_PROXY` env, WPAD) to intercept or alter requests. Also abuses trusted forward/reverse proxies and header trust (`X-Forwarded-*`, `Forwarded`) to spoof identity or client IP, or reach internal services.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check whether the client honors untrusted proxy configuration (auto-discovery, environment variables) and whether the server trusts client-supplied forwarding headers from the edge.

## How to Fix

- Rely on TLS validation regardless of proxy; disable proxy auto-discovery where it is not needed and ignore untrusted proxy auto-config.
- Have the edge overwrite or strip `X-Forwarded-*`/`Forwarded`, and trust them only from known proxy addresses. Map to ASVS 5.0 V4 API and Web Service (4.1.3) and V15 (15.3.4); see `confused-deputy`.

## References

- [CWE-348: Use of Less Trusted Source](https://cwe.mitre.org/data/definitions/348.html)
- [CWE-441: Unintended Proxy or Intermediary](https://cwe.mitre.org/data/definitions/441.html)
- [RFC 7239: Forwarded HTTP Extension](https://www.rfc-editor.org/rfc/rfc7239)
