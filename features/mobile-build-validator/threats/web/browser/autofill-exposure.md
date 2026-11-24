---
id: autofill-exposure
title: Autofill Exposure
category: web-browser
platforms: [web]
owasp_refs: [A06:2025]
cwe: [CWE-200, CWE-359]
severity: low
last_verified: 2026-10-29
---

# Autofill Exposure

## Why It Is a Threat

Browser and password-manager autofill can drop saved data (name, address, email, phone, payment, credentials) into fields the user never sees: hidden or off-screen inputs, inputs injected through HTML injection or XSS, or forms inside third-party frames. The user approves one visible field and leaks the whole profile to whoever controls the form.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review forms for hidden, zero-size, or off-screen inputs with autofill-triggering `name`/`autocomplete` values, and for sensitive forms rendered inside third-party iframes.
- Check that `autocomplete` tokens match what each field really needs. Note that browsers may ignore `autocomplete="off"` on login fields by design, so do not report its absence on credential fields as a finding by itself.

## How to Fix

- Do not include hidden or decoy inputs that carry autofill tokens; request only the fields the flow needs and use accurate `autocomplete` tokens (`new-password`, `one-time-code`, `cc-number` only where intended).
- Keep checkout and account forms first-party, not inside third-party frames or scripts, and fix the HTML-injection/XSS paths that let an attacker add fields (`html-injection`, `xss`).
- Map to ASVS 5.0 V14 Data Protection (V14.2.6 minimum data, V14.3 Client-side Data Protection); see `metadata-leakage`.

## References

- [MDN: HTML autocomplete attribute](https://developer.mozilla.org/en-US/docs/Web/HTML/Reference/Attributes/autocomplete)
- [MDN: How to turn off form autocompletion](https://developer.mozilla.org/en-US/docs/Web/Security/Practical_implementation_guides/Turning_off_form_autocompletion)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
