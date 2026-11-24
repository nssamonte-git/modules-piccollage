---
id: open-redirect
title: Open Redirect
category: web-browser
platforms: [web]
owasp_refs: [A01:2025, WSTG-CLNT-04]
cwe: [CWE-601]
severity: medium
last_verified: 2026-10-29
aliases: [unsafe-redirect-uri-validation]
---

# Open Redirect

## Why It Is a Threat

A redirect parameter (server-side or client-side) accepts arbitrary external URLs, so attackers craft trusted-looking links on your domain that bounce victims to phishing or malware sites. It is also a building block for OAuth code and token theft (`oauth-oidc-misconfiguration`) and, in client-side redirects, for `javascript:` URL XSS.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Supply external, protocol-relative, backslash, encoded, and userinfo-style URLs to redirect, return-url, and OAuth `redirect_uri` parameters, and check whether off-site redirects are honored.
- For client-side redirects, trace `location`/`window.open` sinks and check whether non-HTTP schemes are accepted.
- Source review by stack (`source-code-vapt`): Laravel `redirect($request->input('next'))` and `redirect()->away(...)`; PHP `header('Location: ' . $_GET[...])`; React Native `Linking.openURL(value)` with server or user values.

## How to Fix

- Redirect only to relative paths or to an allowlist of hosts, comparing the origin parsed with a standard URL parser; prefer server-side ids mapped to destinations over raw URLs; allow only `https:` (and `http:` if needed) schemes.
- Show an interstitial warning for any redirect leaving your control, and require exact `redirect_uri` matching for OAuth.
- Map to ASVS 5.0 V3.7.2 and V3.7.3, V10 OAuth and OIDC, and the Unvalidated Redirects and Forwards Cheat Sheet.

## References

- [OWASP Unvalidated Redirects and Forwards Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Unvalidated_Redirects_and_Forwards_Cheat_Sheet.html)
- [CWE-601: URL Redirection to Untrusted Site ('Open Redirect')](https://cwe.mitre.org/data/definitions/601.html)
- [OWASP WSTG: Testing for Client-side URL Redirect (WSTG-CLNT-04)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/04-Client-side_URL_Redirect/)
