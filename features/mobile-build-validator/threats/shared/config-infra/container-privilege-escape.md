---
id: container-privilege-escape
title: Unsafe Container Privileges / Escape
category: config-infra
platforms: [shared, backend]
owasp_refs: [A02:2025]
cwe: [CWE-250, CWE-269]
severity: high
last_verified: 2026-10-29
aliases: [unsafe-container-privileges, container-escape]
---

# Unsafe Container Privileges / Escape

## Why It Is a Threat

Containers that run privileged, as root, with host PID/network namespaces, host-path or container-runtime socket mounts, or extra capabilities (e.g. `CAP_SYS_ADMIN`) let a compromised workload escape to the node and reach other workloads, turning an app bug into node or cluster compromise. Unpatched kernels and container runtimes add escape paths even for well-configured pods.

## How to Find or Test

Authorized testing only (follow the `smx-ai-vapt` authorization gate).

- Review pod and container specs: `privileged`, `runAsNonRoot`, `allowPrivilegeEscalation`, added capabilities, `hostPID`/`hostNetwork`/`hostPath`, runtime-socket mounts, and seccomp/AppArmor profiles.
- Check which Pod Security Admission level each namespace enforces, and the patch level of node kernels and runtimes.

## How to Fix

- Run as non-root with `allowPrivilegeEscalation: false`, drop all capabilities and add back only what is needed, use the `RuntimeDefault` seccomp profile, a read-only root filesystem, and no host namespaces, host mounts, or runtime sockets.
- Enforce the Pod Security Standards `restricted` profile through Pod Security Admission (PodSecurityPolicy was removed in Kubernetes 1.25) or an equivalent admission policy; keep nodes and runtimes patched. Map to A02:2025 and NIST SP 800-190.

## References

- [Kubernetes: Pod Security Standards](https://kubernetes.io/docs/concepts/security/pod-security-standards/)
- [OWASP Docker Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Docker_Security_Cheat_Sheet.html)
- [NIST SP 800-190: Application Container Security Guide](https://csrc.nist.gov/pubs/sp/800/190/final)
- [MITRE ATT&CK T1611: Escape to Host](https://attack.mitre.org/techniques/T1611/)
