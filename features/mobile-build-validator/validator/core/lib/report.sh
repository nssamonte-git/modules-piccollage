#!/bin/sh
# report.sh - output formats for threat-scan.sh (sourced, not executed).
# Expects: tmp, root, platform, threshold, threshold_rank, blocking, warnings,
# suppressed, files_scanned, rules_count, rules_dir, threats_dir, fix_detail, tab.

lookup_threat() {
  # sets t_title t_sev t_cwe t_owasp t_card for threat id $1
  t_title=""; t_sev=""; t_cwe=""; t_owasp=""; t_card=""
  [ -f "$rules_dir/threat-index.tsv" ] || return 0
  line=$(awk -F '\t' -v id="$1" '$1 == id { print; exit }' "$rules_dir/threat-index.tsv")
  [ -n "$line" ] || return 0
  t_title=$(printf '%s' "$line" | cut -f2)
  t_sev=$(printf '%s' "$line" | cut -f3)
  t_cwe=$(printf '%s' "$line" | cut -f4)
  t_owasp=$(printf '%s' "$line" | cut -f5)
  t_card=$(printf '%s' "$line" | cut -f6)
}

card_fix() {
  # prints the "How to Fix" section of card $1 (relative to threats_dir), max 14 lines
  [ -n "$threats_dir" ] && [ -f "$threats_dir/$1" ] || return 1
  awk '/^## How to Fix/ { f = 1; next } /^## / { f = 0 } f && NF' "$threats_dir/$1" | head -n 14
}

is_blocking() { [ "$(sev_rank "$1")" -ge "$threshold_rank" ]; }

upper() { printf '%s' "$1" | tr '[:lower:]' '[:upper:]'; }

print_finding_console() {
  # $1 sev $2 rule $3 threat $4 file $5 line $6 excerpt $7 message $8 card
  lookup_threat "$3"
  loc=$4; [ "$5" -gt 0 ] 2>/dev/null && loc="$4:$5"
  printf '[%s] %s  %s\n' "$(upper "$1")" "$2" "$loc"
  printf '    found:  %s\n' "$6"
  printf '    threat: %s  [%s | card severity: %s | %s | %s]\n' "${t_title:-$3}" "$3" "${t_sev:-?}" "${t_cwe:--}" "${t_owasp:--}"
  printf '    fix:    %s\n' "$7"
  printf '    card:   %s\n' "$8"
}

report_console() {
  printf 'Mobile build validator %s  (platform: %s, blocking threshold: %s)\n' "$VERSION" "$platform" "$threshold"
  printf 'root: %s\n' "$root"
  printf 'files scanned: %s   rules: %s   threat library: %s\n' "$files_scanned" "$rules_count" "${threats_dir:-not found (fix detail off)}"
  printf '\n'

  if [ "$blocking" -gt 0 ]; then
    printf 'BLOCKING FINDINGS (%s) - the build stops until these are fixed or suppressed with a reason\n' "$blocking"
    printf '%s\n' "------------------------------------------------------------------------------"
    seen_cards=""
    while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags; do
      [ -n "$sev" ] || continue
      is_blocking "$sev" || continue
      print_finding_console "$sev" "$rule_id" "$threat_id" "$file" "$line" "$excerpt" "$message" "$card"
      if [ "$fix_detail" -eq 1 ]; then
        case " $seen_cards " in
          *" $card "*) printf '    how to fix: see the earlier finding for this card\n' ;;
          *) fix=$(card_fix "$card")
             if [ -n "$fix" ]; then
               printf '    how to fix (from the threat card):\n'
               printf '%s\n' "$fix" | sed 's/^/        /'
             fi
             seen_cards="$seen_cards $card" ;;
        esac
      fi
      printf '\n'
    done < "$tmp/sorted.tsv"
  fi

  if [ "$warnings" -gt 0 ]; then
    printf 'WARNINGS (%s) - below the blocking threshold, review before release\n' "$warnings"
    printf '%s\n' "------------------------------------------------------------------------------"
    while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags; do
      [ -n "$sev" ] || continue
      is_blocking "$sev" && continue
      print_finding_console "$sev" "$rule_id" "$threat_id" "$file" "$line" "$excerpt" "$message" "$card"
      printf '\n'
    done < "$tmp/sorted.tsv"
  fi

  if [ "$suppressed" -gt 0 ]; then
    printf 'SUPPRESSED (%s) - accepted with a reason; kept visible on purpose\n' "$suppressed"
    printf '%s\n' "------------------------------------------------------------------------------"
    while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags reason; do
      [ -n "$sev" ] || continue
      loc=$file; [ "$line" -gt 0 ] 2>/dev/null && loc="$file:$line"
      printf '[%s] %s  %s\n    reason: %s\n' "$(upper "$sev")" "$rule_id" "$loc" "$reason"
    done < "$tmp/suppressed.tsv"
    printf '\n'
  fi

  if [ -s "$tmp/badsuppress.tsv" ]; then
    printf 'IGNORED SUPPRESSIONS - not accepted, these findings stay active\n'
    printf '%s\n' "------------------------------------------------------------------------------"
    while IFS="$tab" read -r rule_id loc why; do
      printf '  %s  %s  (%s)\n' "$rule_id" "$loc" "${why:-no reason text}"
    done < "$tmp/badsuppress.tsv"
    printf '\n'
  fi

  if [ "$blocking" -gt 0 ]; then
    printf 'Result: FAIL - %s blocking finding(s) at or above "%s", %s warning(s), %s suppressed.\n' "$blocking" "$threshold" "$warnings" "$suppressed"
    printf 'Fix the findings above (or add a reasoned suppression) and build again.\n'
  else
    printf 'Result: PASS - no blocking findings (%s warning(s), %s suppressed).\n' "$warnings" "$suppressed"
  fi
}

report_xcode() {
  # Xcode parses "<abs path>:<line>: error: <msg>" / "warning:" into the issue navigator.
  while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags; do
    [ -n "$sev" ] || continue
    abs="$root/$file"; [ "$file" = "." ] && abs="$root"
    l=$line; [ "$l" -gt 0 ] 2>/dev/null || l=1
    kind=warning; is_blocking "$sev" && kind=error
    printf '%s:%s: %s: [%s %s] %s (threat: %s, card: %s)\n' "$abs" "$l" "$kind" "$rule_id" "$sev" "$message" "$threat_id" "$card"
  done < "$tmp/sorted.tsv"
  while IFS="$tab" read -r rule_id loc why; do
    [ -n "$rule_id" ] || continue
    f=${loc%:*}; l=${loc##*:}; abs="$root/$f"; [ "$f" = "." ] && abs="$root"
    [ "$l" -gt 0 ] 2>/dev/null || l=1
    printf '%s:%s: warning: [%s] suppression not accepted: %s\n' "$abs" "$l" "$rule_id" "${why:-no reason text}"
  done < "$tmp/badsuppress.tsv"
  if [ "$blocking" -gt 0 ]; then
    printf '%s:1: error: Mobile build validator: %s blocking security finding(s). Fix them or suppress with a reason, then build again.\n' "$root" "$blocking"
  else
    printf 'Mobile build validator: PASS (%s warning(s), %s suppressed)\n' "$warnings" "$suppressed"
  fi
}

report_ids() {
  while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags; do
    [ -n "$sev" ] || continue
    printf '%s\t%s:%s\t%s\n' "$rule_id" "$file" "$line" "$sev"
  done < "$tmp/sorted.tsv"
  while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags reason; do
    [ -n "$sev" ] || continue
    printf 'SUPPRESSED:%s\t%s:%s\t%s\n' "$rule_id" "$file" "$line" "$sev"
  done < "$tmp/suppressed.tsv"
}

report_markdown() {
  status=PASS; [ "$blocking" -gt 0 ] && status=FAIL
  printf '# Mobile build validator report\n\n'
  printf -- '- platform: `%s`\n- root: `%s`\n- blocking threshold: `%s`\n- result: **%s** (%s blocking, %s warnings, %s suppressed)\n\n' "$platform" "$root" "$threshold" "$status" "$blocking" "$warnings" "$suppressed"
  if [ -s "$tmp/sorted.tsv" ]; then
    printf '| severity | rule | location | threat | fix | card |\n|---|---|---|---|---|---|\n'
    while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags; do
      [ -n "$sev" ] || continue
      lookup_threat "$threat_id"
      loc=$file; [ "$line" -gt 0 ] 2>/dev/null && loc="$file:$line"
      printf '| %s | %s | `%s` | %s (`%s`) | %s | `%s` |\n' "$sev" "$rule_id" "$loc" "${t_title:-$threat_id}" "$threat_id" "$(printf '%s' "$message" | sed 's/|/\\|/g')" "$card"
    done < "$tmp/sorted.tsv"
    printf '\n'
  fi
  if [ -s "$tmp/suppressed.tsv" ]; then
    printf '## Suppressed\n\n| rule | location | reason |\n|---|---|---|\n'
    while IFS="$tab" read -r sev rule_id threat_id file line excerpt message card flags reason; do
      [ -n "$sev" ] || continue
      printf '| %s | `%s:%s` | %s |\n' "$rule_id" "$file" "$line" "$(printf '%s' "$reason" | sed 's/|/\\|/g')"
    done < "$tmp/suppressed.tsv"
    printf '\n'
  fi
  if [ -s "$tmp/badsuppress.tsv" ]; then
    printf '## Ignored suppressions\n\nNot accepted; these findings stay active.\n\n| rule | location | why |\n|---|---|---|\n'
    while IFS="$tab" read -r rule_id loc why; do
      [ -n "$rule_id" ] || continue
      printf '| %s | `%s` | %s |\n' "$rule_id" "$loc" "$(printf '%s' "${why:-no reason text}" | sed 's/|/\\|/g')"
    done < "$tmp/badsuppress.tsv"
    printf '\n'
  fi
}

report_json() {
  status=pass; [ "$blocking" -gt 0 ] && status=fail
  awk -F '\t' \
    -v platform="$platform" -v root="$root" -v threshold="$threshold" -v status="$status" \
    -v blocking="$blocking" -v warnings="$warnings" -v suppressed="$suppressed" \
    -v files="$files_scanned" -v rules="$rules_count" -v version="$VERSION" \
    -v trank="$threshold_rank" -v supfile="$tmp/suppressed.tsv" -v badfile="$tmp/badsuppress.tsv" -v idx="$rules_dir/threat-index.tsv" '
    function esc(s) { gsub(/\\/, "\\\\", s); gsub(/"/, "\\\"", s); gsub(/\t/, " ", s); gsub(/[\001-\037]/, "", s); return s }
    function rank(s) { return (s=="critical")?4:(s=="high")?3:(s=="medium")?2:1 }
    function finding(blockflag, reason,    o) {
      o = "    {\"severity\":\"" esc($1) "\",\"rule\":\"" esc($2) "\",\"threat\":\"" esc($3) "\""
      o = o ",\"title\":\"" esc(title[$3]) "\",\"card_severity\":\"" esc(csev[$3]) "\",\"cwe\":\"" esc(cwe[$3]) "\",\"owasp\":\"" esc(owasp[$3]) "\""
      o = o ",\"file\":\"" esc($4) "\",\"line\":" ($5+0) ",\"excerpt\":\"" esc($6) "\",\"fix\":\"" esc($7) "\",\"card\":\"" esc($8) "\""
      if (blockflag != "") o = o ",\"blocking\":" blockflag
      if (reason != "") o = o ",\"reason\":\"" esc(reason) "\""
      return o "}"
    }
    BEGIN {
      while ((getline l < idx) > 0) { n = split(l, p, "\t"); title[p[1]] = p[2]; csev[p[1]] = p[3]; cwe[p[1]] = p[4]; owasp[p[1]] = p[5] }
      printf "{\n  \"tool\":\"mobile-build-validator\",\"version\":\"%s\",\"platform\":\"%s\",\"root\":\"%s\",\"threshold\":\"%s\",\n", version, platform, esc(root), threshold
      printf "  \"status\":\"%s\",\"blocking\":%d,\"warnings\":%d,\"suppressed\":%d,\"files_scanned\":%d,\"rules\":%d,\n", status, blocking, warnings, suppressed, files, rules
      printf "  \"findings\": [\n"
      first = 1
    }
    {
      if (!first) printf ",\n"; first = 0
      printf "%s", finding((rank($1) >= trank) ? "true" : "false", "")
    }
    END {
      printf "\n  ],\n  \"suppressed_findings\": [\n"
      first = 1
      while ((getline l < supfile) > 0) {
        n = split(l, f, "\t"); $0 = l
        if (!first) printf ",\n"; first = 0
        printf "%s", finding("", f[10])
      }
      printf "\n  ],\n  \"ignored_suppressions\": [\n"
      first = 1
      while ((getline l < badfile) > 0) {
        n = split(l, b, "\t"); if (b[1] == "") continue
        if (!first) printf ",\n"; first = 0
        printf "    {\"rule\":\"%s\",\"location\":\"%s\",\"why\":\"%s\"}", esc(b[1]), esc(b[2]), esc(b[3])
      }
      printf "\n  ]\n}\n"
    }' "$tmp/sorted.tsv"
}
