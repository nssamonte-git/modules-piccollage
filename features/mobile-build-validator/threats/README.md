# Threat Library

Atomic, retrieval-only knowledge: one threat per file, each with **why it is a threat**, **how to find or test it**, and **how to fix it**. Grouped by platform, with cross-platform threats in `shared/` sub-grouped by category.

This folder is **not default startup context**. It is excluded from auto-loading (`default_excludes` and `never_auto_read` in `manifest.yaml`) and is meant to be loaded **one file at a time on demand**, or queried through the retrieval corpus. Skills reference threat files by `id`; they do not inline threat content.

## Layout

- `shared/<category>/`: cross-platform threats grouped by category — `auth`, `authz`, `injection`, `data-privacy`, `crypto`, `network`, `business-logic`, `availability`, `supply-chain`, `config-infra`, `detection-ops`. A shared threat lives here **once**.
- `web/`, `android/`, `ios/`, `backend/`: platform-specific threats only (optionally sub-grouped by category as they grow).

Dedup rule: if a threat applies to two or more platforms, it goes in `shared/<category>/` and platform files reference it by `id`. Do not copy a shared threat into a platform folder. Consolidate synonyms into one canonical file using `aliases` (e.g. IDOR and BOLA are one file).

## File Format

Front matter:

```
---
id: <kebab-case-unique-id>
title: <human title>
category: <auth | authz | injection | data-privacy | crypto | network | business-logic | availability | supply-chain | config-infra | detection-ops>
platforms: [shared | web | android | ios | backend ...]
owasp_refs: [<most specific id first; formats below>]
cwe: [CWE-XXX]
severity: low | medium | high | critical
last_verified: YYYY-MM-DD
aliases: [<synonym-ids>]      # optional
---
```

`owasp_refs` id formats (current editions only; the first id is the most specific because search weights it highest):

- OWASP Top 10:2025 `A01:2025`…`A10:2025`; API Security Top 10 `API1:2023`…`API10:2023`; LLM Top 10 `LLM01:2025`…`LLM10:2025`; Agentic Top 10 `ASI01:2026`…`ASI10:2026`; Mobile Top 10 `M1:2024`…`M10:2024`; IoT Top 10 `IoT-I1:2018`…; CI/CD Top 10 `CICD-SEC-1`…`CICD-SEC-10`.
- `MASVS-<GROUP>` and current `MASTG-TEST-02xx+` ids (the old `MASTG-TEST-00xx` ids are deprecated); WSTG stable v4.2 ids (`WSTG-INPV-19`) linked to their `wstg.owasp.org/v4.2/` pages; a test that exists only in the latest WSTG uses its latest id (`WSTG-INJT-23`) and `wstg.owasp.org/latest/` page.
- CWE ids must exist in the current CWE release and must not be deprecated or prohibited for mapping (CWE 5.0 deprecated CWE-613; use CWE-1462…1466; categories such as CWE-840 and CWE-1059 are prohibited). Fraud and operational threats with no fitting software weakness use `cwe: []` rather than a prohibited or misleading id.

OWASP Top 10 2021 ids were remapped to 2025 on 2026-10-29: A01→A01, A02→A04, A03→A05, A04→A06, A05→A02, A06→A03 (Software Supply Chain Failures), A07→A07, A08→A08, A09→A09, A10 (SSRF)→A01. A10:2025 Mishandling of Exceptional Conditions is new.

Body sections (in order):

1. `## Why It Is a Threat` — impact and attacker goal. Its first sentence is the card's search summary.
2. `## How to Find or Test` — detection and **authorized** validation only. Starts with "Authorized testing only (follow the `smx-ai-vapt` authorization gate)."; never test systems you are not authorized to test. Cards with stack-specific code patterns end this section with one "Source review by stack (`source-code-vapt`)" bullet naming each stack and its dangerous APIs or settings; keep it in step with the `source-code-vapt` references.
3. `## How to Fix` — concrete remediation, mapped to ASVS 5.0 chapters/requirements (for example "ASVS 5.0 V8 Authorization") or MASVS controls.
4. `## References` — 2–5 links to official or primary sources (OWASP, MITRE CWE/ATT&CK/CAPEC, NIST, CISA, IETF RFCs, platform and cloud vendor documentation, W3C/MDN), each checked to resolve to the right page. No blogs or vendor marketing. `last_verified` is the date the card was checked against those sources.

## Boundaries

- Anchor every threat to an OWASP catalog entry, and to a CWE id wherever a software weakness fits, to stay bounded and dedup-able.
- The threat library is the **deep per-threat detail**; the `smx-ai-vapt` and `security-review` skills hold methodology, workflow, and routing, and point into this library.
- Grow it from real, repeated needs — not as an open-ended encyclopedia.

## Index

Routed like `skills/`: a tree of small generated maps, then one card.

- `MAP.md` → `<platform>/MAP.md` → `<platform>/<cluster>/MAP.md` → one card. Each map stays under the 360-token map budget. Never hand-edit a map; change card front matter and regenerate.
- `sh scripts/query-threats.sh <query>`: ranked search over `retrieval/threats-index.tsv` (id, title, aliases, OWASP, CWE, and each card's first "Why" sentence), without reading any map. Scored by `sh scripts/score-threat-retrieval.sh`.
- `sh scripts/build-threat-index.sh`: validate + table. `--maps` regenerates the map tree and `retrieval/threats-index.tsv`; `--check` fails when they are stale; `--json` emits the retrieval corpus; `--markdown` prints the full flat table to stdout.
- `sh scripts/validate-skill-maps.sh threats/MAP.md`: links resolve, every card reachable, no orphan maps, map budgets held.

## Threat-Modeling Priorities

For most web, Android, iOS, and backend projects, model these first (load linked threats on demand):

1. Broken authentication / account takeover — `mfa-bypass`, `credential-stuffing`, `session-hijacking`, `password-reset-abuse`
2. Broken authorization, IDOR, tenant isolation — `idor-bola`, `broken-function-level-authorization`, `tenant-isolation-failure`
3. Injection and unsafe input — `sql-injection`, `os-command-injection`, `xss`, `unsafe-deserialization`
4. Sensitive-data leakage — `sensitive-data-exposure`, `secrets-leakage`
5. Insecure local mobile storage — `android-insecure-data-storage`, `ios-insecure-data-storage`, `ios-keychain-accessibility`
6. API abuse and business-logic fraud — `workflow-bypass`, `payment-abuse`, `race-condition`, backend `api-*` clusters
7. DDoS and resource exhaustion — `http-request-flood`, `resource-exhaustion`, `cost-exhaustion`
8. Supply-chain and dependency compromise — `vulnerable-dependencies`, `ci-cd-pipeline-compromise`, `poisoned-pipeline-execution`
9. Cloud and security misconfiguration — `public-data-store`, `over-permissive-iam`, `exposed-admin-consoles`, `fail-open-security-controls`
10. Token, key, and secret theft — `secrets-leakage`, `api-key-theft`, `cookie-token-theft`, `hardcoded-encryption-keys`
11. Mobile app reverse engineering and tampering — `android-apk-tampering-repackaging`, `ios-app-tampering-repackaging`, `android-integrity-bypass`, `ios-app-attest-bypass`
12. Missing logs, monitoring, incident response — `missing-security-logging`, `missing-monitoring`, `cloud-audit-logging-gaps`, `delayed-incident-response`

Also model the conditional clusters when the technology is used: `payments/`, `ai-llm/` (including the agentic cards `agentic-supply-chain`, `agent-identity-privilege-abuse`, `agent-memory-context-poisoning` for tool-using agents and MCP), `iot-devices/`, `offline-sync/`.
