---
id: ssrf
title: Server-Side Request Forgery
category: injection
platforms: [shared, web, backend]
owasp_refs: [A01:2025, API7:2023, WSTG-INPV-19]
cwe: [CWE-918]
severity: high
last_verified: 2026-10-29
---

# Server-Side Request Forgery

## Why It Is a Threat

An attacker coerces the server into making requests to a destination of their choosing. Because the request originates from inside the trust boundary, it can reach internal services, cloud metadata endpoints, and link-local addresses that are unreachable from outside — leading to credential theft (e.g. cloud instance credentials), internal port scanning, and pivoting.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify parameters that cause the server to fetch a URL: webhooks, link previews, PDF/image fetchers, import-by-URL, SSO metadata, file imports.
- Point the parameter at an owner-approved out-of-band collector and confirm the server-side fetch (DNS/HTTP callback).
- Treat probing of cloud metadata (link-local `169.254.169.254` and provider equivalents) or internal ranges as high-impact — require explicit approval and document each target. Do not pivot to out-of-scope hosts.
- Source review by stack (`source-code-vapt`): Laravel `Http::get($request->url)`; PHP `file_get_contents($url)`; Python `requests.get(user_url)` without an allowlist.

## How to Fix

- Validate and allowlist destinations (scheme, host, port, path) rather than blocklisting; resolve the host, check the resolved IP, and connect to that same IP to defeat DNS rebinding (ASVS 5.0 V1.3 Sanitization, requirement 1.3.6).
- Block link-local, loopback, and private/internal ranges (IPv4 and IPv6, including alternate encodings); disable unused URL schemes (`file://`, `gopher://`); do not follow redirects unless required, and re-validate each hop if you do (ASVS 5.0 V15.3 requirement 15.3.2).
- Send outbound fetches through an egress proxy or network policy that enforces a destination allowlist (ASVS 5.0 V13.2 requirements 13.2.4 and 13.2.5); do not return raw upstream responses to the client.
- Remove or scope instance-metadata access (on AWS require IMDSv2 and keep the response hop limit as low as the workload allows; use the equivalent controls on other clouds). See also API7:2023.

## References

- [OWASP Server Side Request Forgery Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html)
- [CWE-918: Server-Side Request Forgery (SSRF)](https://cwe.mitre.org/data/definitions/918.html)
- [OWASP Top 10:2025 A01 Broken Access Control](https://top10.owasp.org/2025/A01_2025-Broken_Access_Control/)
- [OWASP API Security Top 10 2023 API7: Server Side Request Forgery](https://owasp.org/API-Security/editions/2023/en/0xa7-server-side-request-forgery/)
- [OWASP WSTG v4.2 WSTG-INPV-19: Testing for Server-Side Request Forgery](https://wstg.owasp.org/v4.2/4-Web_Application_Security_Testing/07-Input_Validation_Testing/19-Testing_for_Server-Side_Request_Forgery/)
