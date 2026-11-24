# Shared Threats: crypto

Load exactly one card by id; never bulk-read this folder.

- [cryptographic-downgrade](cryptographic-downgrade.md): Cryptographic Downgrade Attack, medium
- [hardcoded-encryption-keys](hardcoded-encryption-keys.md): Hard-Coded Encryption Keys, high
- [improper-encryption-mode](improper-encryption-mode.md): Improper Encryption Mode, high
- [improper-key-derivation](improper-key-derivation.md): Improper Key Derivation, medium
- [improper-key-management](improper-key-management.md): Improper Key Management, high, aka keys-stored-with-encrypted-data, improper-key-rotation, missing-key-revocation
- [improper-secrets-management](improper-secrets-management.md): Improper Secrets Management, high
- [incorrect-certificate-validation](incorrect-certificate-validation.md): Incorrect Certificate Validation, high, aka trusting-all-certificates, disabled-hostname-verification, expired-certificate-handling
- [iv-nonce-reuse](iv-nonce-reuse.md): IV / Nonce Reuse, high, aka static-iv, reused-iv, nonce-reuse
- [padding-oracle](padding-oracle.md): Padding-Oracle Attack, high
- [signature-verification-failure](signature-verification-failure.md): Signature-Verification Failure, high, aka unsigned-updates, improperly-signed-updates
- [timing-side-channel](timing-side-channel.md): Timing and Side-Channel Attacks, medium, aka timing-attacks, side-channel-attacks
- [weak-obsolete-algorithms](weak-obsolete-algorithms.md): Weak or Obsolete Cryptographic Algorithms, high, aka weak-cipher-suites
- [weak-password-hashing](weak-password-hashing.md): Weak or Unsalted Password Hashing, high, aka unsalted-password-hashes
- [weak-random-number-generation](weak-random-number-generation.md): Weak Random-Number Generation / Predictable Tokens, high, aka predictable-tokens, insecure-randomness

Up: [Shared Threats](../MAP.md)
