---
id: unsafe-deserialization
title: Unsafe Deserialization
category: injection
platforms: [shared, web, backend]
owasp_refs: [A08:2025, WSTG-INJT-23]
cwe: [CWE-502]
severity: critical
last_verified: 2026-10-29
aliases: [insecure-deserialization, object-injection]
---

# Unsafe Deserialization

## Why It Is a Threat

Untrusted serialized data is deserialized into objects, letting an attacker abuse gadget chains to achieve remote code execution, or tamper with object state to escalate privileges. Affects native object serialization in Java, .NET, PHP (`unserialize`), Python (`pickle`), and Ruby (`Marshal`), plus unsafe YAML/XML loaders and polymorphic JSON type handling. CWE-502 is in the 2025 CWE Top 25.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Identify endpoints that accept serialized objects (cookies, tokens, caches, message queues, view state, file imports); look for format signatures and review code for unsafe loader calls.
- Confirm with a benign, owner-approved gadget or an out-of-band callback; full RCE escalation requires explicit approval.
- Source review by stack (`source-code-vapt`): Android `ObjectInputStream.readObject`; iOS `NSKeyedUnarchiver` with `requiresSecureCoding = false`; Laravel and PHP `unserialize(` on request, cookie, or cache data (and `phar://` before PHP 8.0); Python `pickle.loads`, `yaml.load` without `SafeLoader`, `joblib.load`, `numpy.load(allow_pickle=True)`.

## How to Fix

- Do not deserialize untrusted data with native object serializers; use data-only formats (JSON) with explicit schemas, and disable polymorphic type handling or restrict it to an allowlist (ASVS 5.0 V1.5 Safe Deserialization, requirement 1.5.2).
- Where a native format cannot be removed, enforce a class allowlist (e.g. Java serialization filters via `ObjectInputFilter`); use safe loaders such as `yaml.safe_load`, and never use `BinaryFormatter`, which Microsoft states cannot be made secure.
- Sign and integrity-check any serialized state you must accept, and keep libraries that carry known gadgets patched.

## References

- [OWASP Deserialization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Deserialization_Cheat_Sheet.html)
- [CWE-502: Deserialization of Untrusted Data](https://cwe.mitre.org/data/definitions/502.html)
- [OWASP Top 10:2025 A08 Software or Data Integrity Failures](https://top10.owasp.org/2025/A08_2025-Software_or_Data_Integrity_Failures/)
- [OWASP WSTG (latest) WSTG-INJT-23: Insecure Deserialization](https://wstg.owasp.org/latest/4-Web_Application_Security_Testing/07-Injection/23-Insecure_Deserialization/)
