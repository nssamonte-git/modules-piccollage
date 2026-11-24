# Shared Threats: availability

Load exactly one card by id; never bulk-read this folder.

- [amplification-ddos](amplification-ddos.md): Amplification / Reflection DDoS, high, aka dns-amplification, udp-amplification
- [auth-endpoint-flood](auth-endpoint-flood.md): Login / OTP Flooding, medium, aka login-flood, otp-flood, sms-pumping
- [cache-stampede](cache-stampede.md): Cache Stampede, medium, aka thundering-herd
- [connection-pool-exhaustion](connection-pool-exhaustion.md): Connection / Thread / Queue Pool Exhaustion, high, aka database-connection-exhaustion, thread-pool-exhaustion, queue-exhaustion, websocket-connection-exhaustion
- [cost-exhaustion](cost-exhaustion.md): Cost Exhaustion (Denial of Wallet), high, aka autoscaling-cost-attack, paid-api-cost-exhaustion, sms-email-cost-exhaustion, denial-of-wallet
- [dependency-outage](dependency-outage.md): Dependency or Cloud-Provider Outage, medium, aka cloud-provider-outage, third-party-outage
- [destructive-admin-actions](destructive-admin-actions.md): Destructive Administrative Actions, high
- [expensive-operations](expensive-operations.md): Expensive Operations / Large Uploads, medium, aka expensive-search-reporting, large-upload-attacks
- [graphql-complexity](graphql-complexity.md): GraphQL Complexity Attacks, medium
- [http-request-flood](http-request-flood.md): HTTP / API Request Flood, high, aka api-request-flood, layer-7-ddos
- [ransomware](ransomware.md): Ransomware, critical
- [resource-exhaustion](resource-exhaustion.md): Compute / Memory / Disk Exhaustion, high, aka cpu-exhaustion, memory-exhaustion, disk-exhaustion, file-descriptor-exhaustion
- [retry-storm](retry-storm.md): Retry Storms / Cascading Failure, medium, aka retry-amplification, cascading-failure
- [slow-request-attack](slow-request-attack.md): Slow-Request Attacks, medium, aka slowloris, slow-read, slow-post
- [volumetric-ddos](volumetric-ddos.md): Volumetric (Network/Transport) DDoS, high, aka network-layer-ddos, transport-layer-ddos, syn-flood, bandwidth-exhaustion

Up: [Shared Threats](../MAP.md)
