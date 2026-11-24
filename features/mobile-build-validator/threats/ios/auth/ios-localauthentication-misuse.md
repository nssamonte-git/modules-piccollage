---
id: ios-localauthentication-misuse
title: Incorrect LocalAuthentication Use
category: ios-auth
platforms: [ios]
owasp_refs: [MASVS-AUTH, MASTG-TEST-0266, MASTG-TEST-0268, M3:2024]
cwe: [CWE-287, CWE-303]
severity: high
last_verified: 2026-10-29
aliases: [reusable-authentication-context, insecure-passcode-fallback, missing-user-presence, biometric-without-server-authorization]
---

# Incorrect LocalAuthentication Use

## Why It Is a Threat

Misusing LocalAuthentication weakens biometric gating. Common mistakes:

- **Event-bound checks.** The app gates a secret on the boolean result of `LAContext.evaluatePolicy`. A runtime hook can force that result to true.
- **Context reuse.** One `LAContext` (or a long `touchIDAuthenticationAllowableReuseDuration`) is reused, so one authentication covers later actions.
- **Passcode fallback.** `.deviceOwnerAuthentication` is used where only biometrics should be allowed.
- **Enrollment changes.** The app does not notice that someone enrolled a new face or fingerprint.

Biometrics only control local access. They do not authorize anything on the server. This card extends `biometric-fallback-abuse`.

## How to Find or Test

Authorized testing only, on an owner-authorized device (follow the `smx-ai-vapt` authorization gate).

- Look for `evaluatePolicy` used as the only gate, with no `SecAccessControl`-protected Keychain item or Secure Enclave key behind it (MASTG-TEST-0266).
- Review the policy and access-control flags: `LAPolicy.deviceOwnerAuthentication` allows passcode fallback, and so do the Keychain flags `.userPresence` and `.devicePasscode` (the latter two are what MASTG-TEST-0268 checks). Also check `LAContext` reuse.
- Check whether the app detects enrollment changes: `.biometryCurrentSet`, or `domainState` (iOS 18+; `evaluatedPolicyDomainState` is deprecated).

## How to Fix

- Bind sensitive actions to a Keychain item or Secure Enclave key protected by `SecAccessControl`. Use `.biometryCurrentSet` so new enrollments invalidate the item, and pass the evaluated `LAContext` through `kSecUseAuthenticationContext`.
- Use a fresh `LAContext` for each sensitive action and keep the reuse duration at 0.
- Allow passcode fallback only where the risk accepts it.
- Keep server-side authorization independent of biometric success. Map to MASVS-AUTH-2 and MASVS-AUTH-3. See `ios-secure-enclave-misuse`.

## References

- [MASTG-TEST-0266: References to APIs for Event-Bound Biometric Authentication](https://mas.owasp.org/MASTG/tests/ios/MASVS-AUTH/MASTG-TEST-0266/)
- [MASTG-TEST-0268: References to APIs Allowing Fallback to Non-Biometric Authentication](https://mas.owasp.org/MASTG/tests/ios/MASVS-AUTH/MASTG-TEST-0268/)
- [Apple: Accessing Keychain Items with Face ID or Touch ID](https://developer.apple.com/documentation/localauthentication/accessing-keychain-items-with-face-id-or-touch-id)
- [CWE-287: Improper Authentication](https://cwe.mitre.org/data/definitions/287.html)
