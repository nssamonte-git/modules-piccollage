---
id: android-permission-redelegation
title: Permission Re-Delegation
category: android-permissions
platforms: [android]
owasp_refs: [MASVS-PLATFORM]
cwe: [CWE-441, CWE-862]
severity: medium
last_verified: 2026-10-29
aliases: [confused-deputy-android]
---

# Permission Re-Delegation

## Why It Is a Threat

A privileged app exposes functionality (through an exported component or IPC) that performs permission-protected actions for a caller without checking the caller's permissions, so an unprivileged app uses yours as a deputy to do what it cannot (`confused-deputy`). Your app's identity is what other services see when it acts on the caller's behalf.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify exported components and services that perform permission-gated actions (location, contacts, SMS, files, network calls with app credentials); test whether an app lacking the permission can trigger them.

## How to Fix

- Prefer declarative manifest permissions on the component; for IPC methods, check the caller (`checkCallingPermission()`/`enforceCallingPermission()`) before performing protected actions. Keep such components non-exported where possible.
- Do not assume callers hold your permissions, and use `clearCallingIdentity()` only after the caller has been authorized. Map to MASVS-PLATFORM.

## References

- [Android Developers: Security checklist, interprocess communication](https://developer.android.com/privacy-and-security/security-tips#interprocess-communication)
- [Android Developers: Permission-based access control to exported components](https://developer.android.com/privacy-and-security/risks/access-control-to-exported-components)
- [CWE-441: Unintended Proxy or Intermediary ('Confused Deputy')](https://cwe.mitre.org/data/definitions/441.html)
