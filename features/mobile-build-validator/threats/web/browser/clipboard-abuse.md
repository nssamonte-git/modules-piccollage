---
id: clipboard-abuse
title: Clipboard Abuse
category: web-browser
platforms: [web]
owasp_refs: [A06:2025]
cwe: [CWE-200, CWE-668]
severity: low
last_verified: 2026-10-29
---

# Clipboard Abuse

## Why It Is a Threat

Malicious or compromised script reads the clipboard (copied passwords, 2FA codes, crypto addresses) or writes to it (pastejacking: swapping a copied command or address for a malicious one). Modern browsers gate async clipboard reads behind a permission or a user gesture, but writes during a user-initiated copy are easy, so swapped content and tricked transactions remain the main risk.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review use of the Async Clipboard API (`navigator.clipboard.readText`/`read`/`writeText`) and `copy`/`paste` event handlers, including in third-party scripts and embedded frames.
- Check whether the app reads the clipboard without a clear user action, or alters copied text (hidden characters, replaced commands or addresses).
- Source review by stack (`source-code-vapt`): Jetpack Compose `LocalClipboardManager.setText` with secrets; SwiftUI `UIPasteboard.general.string =` or `.textSelection(.enabled)` on one-time codes.

## How to Fix

- Use clipboard APIs only on explicit user action; never read the clipboard silently, and never modify what the user copied.
- Restrict clipboard access for embedded third-party frames (do not grant `clipboard-read`/`clipboard-write` in iframe `allow` or `Permissions-Policy` unless needed); for high-risk values (crypto addresses, shell commands), show the pasted value for confirmation.
- Map to ASVS 5.0 V14.3 Client-side Data Protection; see `sensitive-data-in-client-storage`.

## References

- [MDN: Clipboard API](https://developer.mozilla.org/en-US/docs/Web/API/Clipboard_API)
- [W3C: Clipboard API and events](https://www.w3.org/TR/clipboard-apis/)
- [MITRE ATT&CK T1115: Clipboard Data](https://attack.mitre.org/techniques/T1115/)
- [CWE-200: Exposure of Sensitive Information to an Unauthorized Actor](https://cwe.mitre.org/data/definitions/200.html)
