# Shared Threats: supply-chain

Load exactly one card by id; never bulk-read this folder.

- [artifact-tampering](artifact-tampering.md): Build Artifact Substitution / Tampering, high, aka artifact-substitution, build-artifact-tampering
- [ci-cd-pipeline-compromise](ci-cd-pipeline-compromise.md): CI/CD Pipeline / Build-Runner Compromise, critical, aka build-runner-compromise
- [compromised-build-tools](compromised-build-tools.md): Compromised Build Tools / IDE Plugins, high, aka build-tool-compromise, ide-plugin-compromise
- [compromised-developer-devices](compromised-developer-devices.md): Compromised Developer Devices, high
- [compromised-registry](compromised-registry.md): Compromised Package Registry, high, aka compromised-package-registry, registry-mirror-compromise
- [compromised-third-party-sdk](compromised-third-party-sdk.md): Compromised Third-Party SDK, high
- [dependency-confusion](dependency-confusion.md): Dependency Confusion, high, aka substitution-attack
- [insufficient-pipeline-access-control](insufficient-pipeline-access-control.md): Insufficient Pipeline-Based Access Controls (PBAC), high, aka insufficient-pbac, pbac, over-privileged-pipeline, over-privileged-ci-token
- [insufficient-pipeline-flow-control](insufficient-pipeline-flow-control.md): Insufficient CI/CD Flow Control Mechanisms, high, aka insufficient-flow-control, missing-deployment-approval, unreviewed-code-to-production, auto-merge-abuse
- [malicious-packages](malicious-packages.md): Malicious Packages, high
- [malicious-software-updates](malicious-software-updates.md): Malicious Software Updates, critical, aka compromised-update-servers
- [missing-supply-chain-controls](missing-supply-chain-controls.md): Missing Pinning, Signatures, or SBOM, medium, aka missing-dependency-pinning, missing-artifact-signatures, missing-sbom
- [poisoned-development-data](poisoned-development-data.md): Poisoned Development Data, medium, aka data-poisoning, training-data-poisoning
- [poisoned-pipeline-execution](poisoned-pipeline-execution.md): Poisoned Pipeline Execution (PPE), critical, aka ppe, pwn-request, pull-request-target-abuse, direct-ppe, indirect-ppe, public-ppe
- [secrets-in-source-control](secrets-in-source-control.md): Secret Leakage in Source Control, high
- [signing-key-theft](signing-key-theft.md): Signing-Key or Certificate Theft, critical, aka certificate-theft, code-signing-key-theft
- [source-control-compromise](source-control-compromise.md): Source-Control Compromise, high, aka source-control-account-compromise, unauthorized-code-changes, malicious-pull-requests, branch-protection-bypass
- [typosquatting](typosquatting.md): Typosquatting Packages, medium
- [unreviewed-ai-code](unreviewed-ai-code.md): Unreviewed Generated or AI-Written Code, medium, aka unreviewed-generated-code
- [vulnerable-dependencies](vulnerable-dependencies.md): Vulnerable or Abandoned Dependencies, high, aka abandoned-dependencies, outdated-components

Up: [Shared Threats](../MAP.md)
