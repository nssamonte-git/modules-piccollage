# Shared Threats: config-infra

Load exactly one card by id; never bulk-read this folder.

- [browser-security-misconfiguration](browser-security-misconfiguration.md): CORS and Security-Header Misconfiguration, medium, aka misconfigured-cors, missing-browser-security-headers
- [container-privilege-escape](container-privilege-escape.md): Unsafe Container Privileges / Escape, high, aka unsafe-container-privileges, container-escape
- [debug-mode-in-production](debug-mode-in-production.md): Debug Mode / Dev Tools in Production, high, aka exposed-development-tools, sample-test-endpoints-enabled, exposed-source-maps
- [default-credentials](default-credentials.md): Default Credentials (Infrastructure), high
- [dns-misconfig-subdomain-takeover](dns-misconfig-subdomain-takeover.md): Insecure DNS Config / Subdomain Takeover, high, aka insecure-dns-configuration, subdomain-takeover
- [exposed-admin-consoles](exposed-admin-consoles.md): Exposed Admin Consoles and Dashboards, high, aka exposed-kubernetes-dashboards, public-container-registries
- [exposed-env-files](exposed-env-files.md): Exposed Environment / Config Files, high
- [fail-open-security-controls](fail-open-security-controls.md): Fail-Open Security Controls, high, aka failing-open, fail-open, not-failing-securely, fail-open-authorization
- [improper-exception-handling](improper-exception-handling.md): Improper Exception Handling and Inconsistent State, medium, aka swallowed-exceptions, unhandled-exceptions, partial-transactions, mishandled-exceptional-conditions
- [insecure-defaults](insecure-defaults.md): Insecure Defaults / Prod-Test Mixing, medium, aka shared-production-credentials, improper-production-test-separation, incorrect-default-permissions
- [metadata-service-exposure](metadata-service-exposure.md): Cloud Metadata-Service Exposure, high
- [missing-network-segmentation](missing-network-segmentation.md): Missing Network Segmentation, medium
- [open-ports-firewall](open-ports-firewall.md): Unnecessary Open Ports / Over-Permissive Firewall, high, aka unnecessary-open-ports, over-permissive-firewall-rules
- [orphaned-cloud-resources](orphaned-cloud-resources.md): Orphaned Cloud Resources / Shadow Infrastructure, medium, aka shadow-infrastructure
- [over-permissive-iam](over-permissive-iam.md): Over-Permissive Cloud IAM, high, aka wildcard-iam, excessive-cloud-permissions
- [public-data-store](public-data-store.md): Publicly Exposed Databases and Buckets, critical, aka public-databases, public-object-storage-buckets, backup-files-in-public-directories
- [serverless-permission-abuse](serverless-permission-abuse.md): Serverless Permission Abuse, medium
- [verbose-errors](verbose-errors.md): Verbose Errors / Directory Listing, medium, aka directory-listing, information-disclosure-errors

Up: [Shared Threats](../MAP.md)
