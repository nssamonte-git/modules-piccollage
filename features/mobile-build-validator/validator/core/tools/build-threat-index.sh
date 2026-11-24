#!/bin/sh
# Builds core/rules/threat-index.tsv from a threat library folder
# (one markdown card per threat with YAML front matter).
#
# Usage: sh core/tools/build-threat-index.sh [threats-dir]
# Default threats-dir: $THREATS_DIR, else "$AI_KNOWLEDGE_REPO/threats", else the
# threats/ folder beside mobile-build-validator/, else
# "$HOME/Projects/AI Knowledge and Skills Repo/threats".
set -u

here=$(cd "$(dirname "$0")" && pwd)
# THREAT_INDEX_OUT writes elsewhere (the tests use it to compare with the committed index).
out=${THREAT_INDEX_OUT:-"$here/../rules/threat-index.tsv"}
if [ -n "${THREATS_DIR:-}" ]; then
  src=$THREATS_DIR
elif [ -n "${AI_KNOWLEDGE_REPO:-}" ]; then
  src=$AI_KNOWLEDGE_REPO/threats
elif [ -d "$(dirname "$0")/../../../threats" ]; then
  src=$(cd "$(dirname "$0")/../../../threats" && pwd)
else
  src="$HOME/Projects/AI Knowledge and Skills Repo/threats"
fi
[ -n "${1:-}" ] && src=$1

if [ ! -d "$src" ]; then
  printf 'ERROR: threats directory not found: %s\n' "$src" >&2
  exit 1
fi

tab=$(printf '\t')
{
  printf 'id\ttitle\tseverity\tcwe\towasp\tcard\n'
  find "$src" -type f -name '*.md' ! -name 'MAP.md' ! -name 'README.md' | LC_ALL=C sort | while IFS= read -r f; do
    rel=${f#"$src"/}
    awk -v card="$rel" -v tab="$tab" '
      NR == 1 && $0 != "---" { exit }
      NR > 1 && $0 == "---" { exit }
      /^id:/       { id = $2 }
      /^title:/    { t = $0; sub(/^title:[ \t]*/, "", t); title = t }
      /^severity:/ { sev = $2 }
      /^cwe:/      { c = $0; sub(/^cwe:[ \t]*/, "", c); gsub(/[\[\]]/, "", c); gsub(/[ \t]+/, "", c); cwe = c }
      /^owasp_refs:/ { o = $0; sub(/^owasp_refs:[ \t]*/, "", o); gsub(/[\[\]]/, "", o); gsub(/[ \t]+/, "", o); owasp = o }
      END { if (id != "") printf "%s%s%s%s%s%s%s%s%s%s%s\n", id, tab, title, tab, sev, tab, cwe, tab, owasp, tab, card }
    ' "$f"
  done
} > "$out.tmp" && mv "$out.tmp" "$out"

n=$(($(wc -l < "$out") - 1))
printf 'wrote %s (%s threats) from %s\n' "$out" "$n" "$src"
