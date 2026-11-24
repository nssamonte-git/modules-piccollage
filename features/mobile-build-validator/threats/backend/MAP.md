# Backend Threats

Backend-specific threats; cross-platform threats live in [Shared](../shared/MAP.md). Pick a cluster, then load exactly one card by id.

- [api-auth](api-auth/MAP.md): 4 threats, e.g. missing-api-authentication, jwt-kid-handling, oauth-scope-escalation
- [api-authz](api-authz/MAP.md): 2 threats, e.g. hidden-admin-parameter, insecure-pagination
- [api-inventory](api-inventory/MAP.md): 3 threats, e.g. api-versioning-security-drift, unsafe-api-consumption, exposed-api-docs
- [cloud](cloud/MAP.md): 8 threats, e.g. cloud-account-takeover, container-image-compromise, k8s-rbac-misconfig
- [database](database/MAP.md): 7 threats, e.g. db-credential-theft, weak-row-level-security, application-cache-security
- [exploitation](exploitation/MAP.md): 4 threats, e.g. remote-code-execution, arbitrary-file-write, file-inclusion
- [graphql](graphql/MAP.md): 4 threats, e.g. graphql-resolver-authorization, graphql-batching-abuse, graphql-introspection-exposure
- [messaging](messaging/MAP.md): 4 threats, e.g. event-spoofing, message-queue-security, unauthorized-message-publishing
- [post-exploitation](post-exploitation/MAP.md): 5 threats, e.g. attacker-persistence, data-exfiltration, lateral-movement
- [backend-mass-assignment](mass-assignment.md): Mass Assignment / Over-Posting, high

Up: [Threat Library](../MAP.md)
