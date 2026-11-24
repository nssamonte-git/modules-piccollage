#!/bin/sh
# checks-ios.sh - property-list structural checks for Info.plist and
# *.entitlements (rules of type "check" with pattern "plist;;KEY;;VALUE_RE" or
# "plist_absent;;KEY"). Output: file<TAB>line<TAB>excerpt.
#
# plist;;KEY_RE;;VALUE_RE  for every <key> whose text matches KEY_RE, take the
#   value element that follows (<true/>, <string>..</string>, <integer>, or a
#   whole <array>/<dict> block) and report when its text matches VALUE_RE.
# plist_absent;;KEY_RE     report files (line 0) that have no such key.
check_plist() {
  parts=$(_split_parts "$2")
  KEY=$(printf '%s\n' "$parts" | sed -n '1p')
  VAL=$(printf '%s\n' "$parts" | sed -n '2p')
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    KEY="$KEY" VAL="$VAL" awk '
      BEGIN { k = ENVIRON["KEY"]; v = ENVIRON["VAL"] }
      { L[NR] = $0 }
      function trim(s) { sub(/^[[:space:]]+/, "", s); sub(/[[:space:]]+$/, "", s); return s }
      END {
        for (i = 1; i <= NR; i++) {
          if (!match(L[i], /<key>[^<]*<\/key>/)) continue
          key = substr(L[i], RSTART + 5, RLENGTH - 11)
          if (key !~ ("^(" k ")$")) continue
          rest = trim(substr(L[i], RSTART + RLENGTH))
          val = ""; j = i
          if (rest != "") { val = rest }
          else {
            j = i + 1
            while (j <= NR && trim(L[j]) == "") j++
            if (j > NR) continue
            t = trim(L[j])
            if (t ~ /^<(array|dict)>/ && t !~ /<\/(array|dict)>$/ && t !~ /^<(array|dict)\/>/) {
              depth = 0; val = ""
              while (j <= NR) {
                t = trim(L[j])
                o = gsub(/<(array|dict)>/, "&", t); c = gsub(/<\/(array|dict)>/, "&", t)
                depth += o - c
                val = val " " t
                if (depth <= 0) break
                j++
              }
            } else val = t
          }
          val = trim(val)
          if (val ~ v) { ex = key " -> " val; print ENVIRON["BV_FILE"] "\t" i "\t" substr(ex, 1, 150) }
        }
      }' < "$f"
  done < "$1"
}

check_plist_absent() {
  parts=$(_split_parts "$2")
  KEY=$(printf '%s\n' "$parts" | sed -n '1p')
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    KEY="$KEY" awk '
      BEGIN { k = ENVIRON["KEY"] }
      { if (match($0, /<key>[^<]*<\/key>/)) { key = substr($0, RSTART + 5, RLENGTH - 11); if (key ~ ("^(" k ")$")) found = 1 } }
      END { if (!found) print ENVIRON["BV_FILE"] "\t0\tno <key>" k "</key> in this plist" }' < "$f"
  done < "$1"
}
