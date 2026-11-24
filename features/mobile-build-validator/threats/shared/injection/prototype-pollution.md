---
id: prototype-pollution
title: Prototype Pollution
category: injection
platforms: [shared, web, backend]
owasp_refs: [A05:2025, A08:2025]
cwe: [CWE-1321]
severity: high
last_verified: 2026-10-29
---

# Prototype Pollution

## Why It Is a Threat

In JavaScript, attacker-controlled keys like `__proto__` or `constructor.prototype` reach a recursive merge/clone, path-based setter, or query-string parser, polluting `Object.prototype`. This corrupts application logic globally and can escalate to XSS (client side), authorization bypass, denial of service, or RCE (server-side Node.js) via affected gadgets.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Send payloads with `__proto__`/`constructor`/`prototype` keys in JSON bodies, query strings, and URL fragments that feed merge, clone, or `set`-by-path utilities.
- Confirm only that a benign, uniquely named property appears on unrelated objects; check dependencies against known pollution advisories.

## How to Fix

- Reject or strip `__proto__`, `constructor`, and `prototype` keys; use `Map`/`Set` or null-prototype objects (`Object.create(null)`) for untrusted dictionaries (ASVS 5.0 V15.3 Defensive Coding, requirement 15.3.6).
- Validate input against a schema that disallows unexpected keys; check own properties (`Object.hasOwn`) rather than inherited ones.
- As defence in depth, freeze `Object.prototype` and, in Node.js, run with `--disable-proto=delete`. Keep merge/clone libraries patched.

## References

- [OWASP Prototype Pollution Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Prototype_Pollution_Prevention_Cheat_Sheet.html)
- [OWASP WSTG (latest) WSTG-INJT-22: Prototype Pollution](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/07-Injection/22-Prototype_Pollution/)
- [CWE-1321: Improperly Controlled Modification of Object Prototype Attributes ('Prototype Pollution')](https://cwe.mitre.org/data/definitions/1321.html)
