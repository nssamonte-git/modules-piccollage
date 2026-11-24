#!/bin/sh
# checks-common.sh - generic structural checks for rules of type "check".
# Every check_<name> receives: $1 = path to a file list (one relative path per
# line), $2 = the rule's argument string (parts separated by ";;"). It prints
# one finding per line: file<TAB>line<TAB>excerpt. Patterns are POSIX EREs.
#
# Parts that start with "!" are "must NOT match"; others are "must match".
# Patterns are handed to awk through the environment (never -v) so that
# backslashes survive untouched.

_split_parts() {
  # splits $1 on ";;" and prints one part per line
  rest=$1
  while :; do
    case "$rest" in
      *';;'*) part=${rest%%;;*}; rest=${rest#*;;} ;;
      *) part=$rest; rest="" ;;
    esac
    printf '%s\n' "$part"
    [ -n "$rest" ] || break
  done
}

_musts_absents() {
  # from the newline-separated parts on stdin, fill MUSTS / ABSENTS joined by \001
  MUSTS=""; ABSENTS=""
  while IFS= read -r p; do
    [ -n "$p" ] || continue
    case "$p" in
      '!'*) ABSENTS="${ABSENTS}${ABSENTS:+$(printf '\001')}${p#!}" ;;
      *)    MUSTS="${MUSTS}${MUSTS:+$(printf '\001')}${p}" ;;
    esac
  done
}

# cooccur;;MUST_RE[;;MUST_RE...][;;!ABSENT_RE...]
# File-level: every MUST matches somewhere in the file and no ABSENT does.
# The finding points at the first line matching the first MUST.
check_cooccur() {
  _musts_absents <<EOS
$(_split_parts "$2")
EOS
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    MUSTS="$MUSTS" ABSENTS="$ABSENTS" awk '
      BEGIN { nm = split(ENVIRON["MUSTS"], m, "\001"); na = split(ENVIRON["ABSENTS"], a, "\001") }
      { for (i = 1; i <= nm; i++) if (!hit[i] && m[i] != "" && $0 ~ m[i]) { hit[i] = NR; txt[i] = $0 }
        for (i = 1; i <= na; i++) if (a[i] != "" && $0 ~ a[i]) { absent = 1; exit } }
      END { if (absent) exit
            for (i = 1; i <= nm; i++) if (m[i] != "" && !hit[i]) exit
            print ENVIRON["BV_FILE"] "\t" hit[1] "\t" txt[1] }' < "$f"
  done < "$1"
}

# near;;MATCH_RE;;WINDOW;;[MUST_RE|!ABSENT_RE]...
# Line-level: for each line matching MATCH_RE, look at that line plus the next
# WINDOW-1 lines; report when every MUST matches in the window and no ABSENT does.
check_near() {
  parts=$(_split_parts "$2")
  MATCH=$(printf '%s\n' "$parts" | sed -n '1p')
  WINDOW=$(printf '%s\n' "$parts" | sed -n '2p')
  _musts_absents <<EOS
$(printf '%s\n' "$parts" | sed '1,2d')
EOS
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    MATCH="$MATCH" WINDOW="${WINDOW:-1}" MUSTS="$MUSTS" ABSENTS="$ABSENTS" awk '
      BEGIN { m = ENVIRON["MATCH"]; w = ENVIRON["WINDOW"] + 0; if (w < 1) w = 1
              nm = split(ENVIRON["MUSTS"], mu, "\001"); na = split(ENVIRON["ABSENTS"], ab, "\001") }
      { L[NR] = $0 }
      END {
        for (i = 1; i <= NR; i++) if (L[i] ~ m) {
          blk = ""
          for (j = i; j < i + w && j <= NR; j++) blk = blk L[j] "\n"
          ok = 1
          for (k = 1; k <= nm; k++) if (mu[k] != "" && blk !~ mu[k]) ok = 0
          for (k = 1; k <= na; k++) if (ab[k] != "" && blk ~ ab[k]) ok = 0
          if (ok) print ENVIRON["BV_FILE"] "\t" i "\t" L[i]
        }
      }' < "$f"
  done < "$1"
}

# within;;START_RE;;BODY_RE[;;!BLOCK_SKIP_RE][;;file!FILE_SKIP_RE]
# Brace-scoped: from a line matching START_RE, follow {...} nesting to the end
# of that block; report every block line matching BODY_RE. Blocks containing
# BLOCK_SKIP_RE and files containing FILE_SKIP_RE are ignored.
# within-absent (same arguments) reports the START line when BODY_RE never
# matches inside the block.
_within() {
  mode=$1; list=$2; args=$3
  parts=$(_split_parts "$args")
  START=$(printf '%s\n' "$parts" | sed -n '1p')
  BODY=$(printf '%s\n' "$parts" | sed -n '2p')
  BSKIP=$(printf '%s\n' "$parts" | sed '1,2d' | sed -n 's/^!//p' | head -n 1)
  FSKIP=$(printf '%s\n' "$parts" | sed '1,2d' | sed -n 's/^file!//p' | head -n 1)
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    START="$START" BODY="$BODY" MODE="$mode" BSKIP="$BSKIP" FSKIP="$FSKIP" awk '
      BEGIN { s = ENVIRON["START"]; b = ENVIRON["BODY"]; mode = ENVIRON["MODE"]; bs = ENVIRON["BSKIP"]; fs = ENVIRON["FSKIP"] }
      { L[NR] = $0; if (fs != "" && $0 ~ fs) fileskip = 1 }
      END {
        if (fileskip) exit
        for (i = 1; i <= NR; i++) if (L[i] ~ s) {
          depth = 0; opened = 0; found = 0; skip = 0; nh = 0; j = i
          while (j <= NR) {
            t = L[j]; o = gsub(/\{/, "", t); t = L[j]; cl = gsub(/\}/, "", t)
            if (!opened && o > 0) opened = 1
            depth += o - cl
            if (bs != "" && L[j] ~ bs) skip = 1
            if (L[j] ~ b) { found = 1; nh++; hl[nh] = j }
            if (opened && depth <= 0) break
            if (!opened && j - i > 5) break
            j++
          }
          if (!skip) {
            if (mode == "present") for (k = 1; k <= nh; k++) print ENVIRON["BV_FILE"] "\t" hl[k] "\t" L[hl[k]]
            if (mode == "absent" && !found) print ENVIRON["BV_FILE"] "\t" i "\t" L[i]
          }
          if (opened) i = j
        }
      }' < "$f"
  done < "$list"
}
check_within() { _within present "$1" "$2"; }
check_within_absent() { _within absent "$1" "$2"; }

# xml_element;;ELEMENT_RE[;;MUST_RE|!ABSENT_RE]...
# For every XML start tag whose element name matches ELEMENT_RE, test the
# whole start tag (attributes joined on one line) plus, for non-empty
# elements, its body up to the closing tag: report when every MUST matches
# and no ABSENT does.
check_xml_element() {
  parts=$(_split_parts "$2")
  ELEM=$(printf '%s\n' "$parts" | sed -n '1p')
  _musts_absents <<EOS
$(printf '%s\n' "$parts" | sed '1d')
EOS
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    ELEM="$ELEM" MUSTS="$MUSTS" ABSENTS="$ABSENTS" awk '
      BEGIN { el = ENVIRON["ELEM"]; nm = split(ENVIRON["MUSTS"], mu, "\001"); na = split(ENVIRON["ABSENTS"], ab, "\001") }
      { buf = buf $0 "\n" }
      function lineof(pos,   t) { t = substr(buf, 1, pos - 1); return gsub(/\n/, "", t) + 1 }
      END {
        pos = 1
        while (match(substr(buf, pos), /<[A-Za-z][A-Za-z0-9_.:-]*/)) {
          s = pos + RSTART - 1
          name = substr(buf, s + 1, RLENGTH - 1)
          tagend = index(substr(buf, s), ">")
          if (tagend == 0) break
          tag = substr(buf, s, tagend)
          pos = s + tagend
          if (name !~ ("^(" el ")$")) continue
          body = ""
          if (tag !~ /\/>$/) {
            ce = index(substr(buf, pos), "</" name ">")
            if (ce > 0) body = substr(buf, pos, ce - 1)
          }
          t = tag " " body; gsub(/[[:space:]]+/, " ", t)
          ok = 1
          for (k = 1; k <= nm; k++) if (mu[k] != "" && t !~ mu[k]) ok = 0
          for (k = 1; k <= na; k++) if (ab[k] != "" && t ~ ab[k]) ok = 0
          if (ok) { x = tag; gsub(/[[:space:]]+/, " ", x); print ENVIRON["BV_FILE"] "\t" lineof(s) "\t" substr(x, 1, 150) }
        }
      }' < "$f"
  done < "$1"
}

# call;;MATCH_RE[;;MUST_RE|!ABSENT_RE]...
# Call-scoped: for each line matching MATCH_RE, take that line plus following
# lines until the parentheses opened from the match position are balanced (at
# most 8 lines), and report when every MUST matches that text and no ABSENT does.
check_call() {
  parts=$(_split_parts "$2")
  MATCH=$(printf '%s\n' "$parts" | sed -n '1p')
  _musts_absents <<EOS
$(printf '%s\n' "$parts" | sed '1d')
EOS
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    MATCH="$MATCH" MUSTS="$MUSTS" ABSENTS="$ABSENTS" awk '
      BEGIN { m = ENVIRON["MATCH"]; nm = split(ENVIRON["MUSTS"], mu, "\001"); na = split(ENVIRON["ABSENTS"], ab, "\001") }
      { L[NR] = $0 }
      function opens(x,   t) { t = x; return gsub(/\(/, "", t) }
      function closes(x,   t) { t = x; return gsub(/\)/, "", t) }
      END {
        for (i = 1; i <= NR; i++) {
          if (!match(L[i], m)) continue
          seg = substr(L[i], RSTART)
          txt = L[i]; depth = opens(seg) - closes(seg); j = i
          while (depth > 0 && j < NR && j < i + 8) { j++; txt = txt "\n" L[j]; depth += opens(L[j]) - closes(L[j]) }
          ok = 1
          for (k = 1; k <= nm; k++) if (mu[k] != "" && txt !~ mu[k]) ok = 0
          for (k = 1; k <= na; k++) if (ab[k] != "" && txt ~ ab[k]) ok = 0
          if (ok) print ENVIRON["BV_FILE"] "\t" i "\t" L[i]
        }
      }' < "$f"
  done < "$1"
}
