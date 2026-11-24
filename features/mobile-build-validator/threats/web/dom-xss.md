---
id: dom-xss
title: DOM-Based Cross-Site Scripting
category: injection
platforms: [web]
owasp_refs: [A05:2025, WSTG-CLNT-01]
cwe: [CWE-79]
severity: high
last_verified: 2026-10-29
---

# DOM-Based Cross-Site Scripting

## Why It Is a Threat

Client-side JavaScript writes attacker-controllable data into a dangerous sink without encoding, so script executes in the victim's browser. Because the injection happens entirely in the DOM, it can occur even when the server output is clean, enabling session theft, account takeover, and action-on-behalf-of-user.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Trace sources (`location`, `document.referrer`, `window.name`, `postMessage`, storage) to sinks (`innerHTML`, `outerHTML`, `insertAdjacentHTML`, `document.write`, `eval`, `setTimeout(string)`, `location`/`href` assignment with `javascript:` URLs, framework `dangerouslySetInnerHTML` / `v-html`).
- Confirm with a benign marker (a controlled element insertion), not a weaponized payload; record the exact source-to-sink path.
- Use the browser's debugger and an intercepting proxy; verify framework escaping is not bypassed via raw-HTML props.

## How to Fix

- Avoid dangerous sinks; assign text via `textContent` or framework-escaped bindings rather than HTML; allow only `https:`/`http:` schemes in dynamic URLs.
- Where HTML is required, sanitize with a vetted library (e.g. DOMPurify) and a strict allowlist.
- Enforce Trusted Types (`require-trusted-types-for 'script'`, now supported across major browsers) and a strict nonce/hash-based CSP as defense in depth. Map to ASVS 5.0 V3.2.2, V1.2.2, V1.3.1, and V3.4.3, and the DOM-based XSS Prevention Cheat Sheet.

## References

- [OWASP DOM based XSS Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html)
- [OWASP WSTG: Testing for DOM-Based Cross Site Scripting (WSTG-CLNT-01)](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/11-Client-side/01-DOM-based_Cross_Site_Scripting/)
- [CWE-79: Improper Neutralization of Input During Web Page Generation](https://cwe.mitre.org/data/definitions/79.html)
- [MDN: Trusted Types API](https://developer.mozilla.org/en-US/docs/Web/API/Trusted_Types_API)
