---
id: debug-mode-in-production
title: Debug Mode / Dev Tools in Production
category: config-infra
platforms: [shared, web, backend]
owasp_refs: [A02:2025]
cwe: [CWE-489, CWE-215]
severity: high
last_verified: 2026-10-29
aliases: [exposed-development-tools, sample-test-endpoints-enabled, exposed-source-maps]
---

# Debug Mode / Dev Tools in Production

## Why It Is a Threat

Debug mode, dev/profiling tools, sample/test endpoints, unauthenticated framework management endpoints (health, metrics, env, heap dumps), or published source maps in production expose stack traces, internal config, secrets, and extra functionality, handing attackers reconnaissance and sometimes RCE (e.g. interactive debug consoles).

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Check for debug flags, framework debug pages, profilers, management/actuator endpoints, `/test` or sample endpoints, `TRACE` support, and reachable `.map` files in production.
- Source review by stack (`source-code-vapt`): Android `android:debuggable="true"` in release; Laravel `APP_DEBUG=true`; CodeIgniter `ENVIRONMENT` or `CI_ENVIRONMENT` set to `development`; Django `DEBUG = True`; Flask `app.run(debug=True)`; React Native Flipper or `__DEV__` branches reaching release builds.

## How to Fix

- Disable debug and dev tooling and remove sample/test code in production builds; do not serve source maps publicly; disable `TRACE`.
- Expose management and diagnostic endpoints only on an internal interface behind authentication. Map to A02:2025 and ASVS 5.0 V13 Configuration (V13.4 Unintended Information Leakage) and V15.2 (no extraneous functionality in production).

## References

- [OWASP Top 10:2025 A02 Security Misconfiguration](https://top10.owasp.org/2025/A02_2025-Security_Misconfiguration/)
- [CWE-489: Active Debug Code](https://cwe.mitre.org/data/definitions/489.html)
- [CWE-215: Insertion of Sensitive Information Into Debugging Code](https://cwe.mitre.org/data/definitions/215.html)
