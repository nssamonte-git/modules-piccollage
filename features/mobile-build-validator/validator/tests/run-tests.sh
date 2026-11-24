#!/bin/sh
# run-tests.sh - fixture tests for the mobile build validator.
#
# For every folder under tests/fixtures/<name>/:
#   PLATFORM        optional file naming the platform (default: <name>)
#   bad/            must exit 1 and fire exactly the rule ids in EXPECTED.txt
#                   (one id per line, sorted; suppressed ones as SUPPRESSED:<id>)
#   clean/          must exit 0 with no findings at all (the clean fixture also
#                   contains the defenses the tree-absent rules look for)
# After all fixtures: every rule id in core/rules/*.rules must have fired in at
# least one bad fixture (coverage), and the JSON report must parse.
set -u
here=$(cd "$(dirname "$0")" && pwd)
repo=$(cd "$here/.." && pwd)
engine=$repo/core/threat-scan.sh
tmp=$(mktemp -d "${TMPDIR:-/tmp}/validator-tests.XXXXXX")
trap 'rm -rf "$tmp"' EXIT INT TERM
fail=0; pass=0
: > "$tmp/fired"

ok()   { pass=$((pass + 1)); printf 'PASS: %s\n' "$1"; }
bad()  { fail=$((fail + 1)); printf 'FAIL: %s\n' "$1"; }

for src in "$here"/fixtures/*/; do
  name=$(basename "$src")
  platform=$name
  [ -f "$src/PLATFORM" ] && platform=$(tr -d '[:space:]' < "$src/PLATFORM")
  # scan a copy outside any git work tree; bad/ also gets generated fake secrets
  dir=$tmp/work/$name
  mkdir -p "$dir"
  [ -d "$src/bad" ] && cp -R "$src/bad" "$dir/bad" && sh "$here/fixtures/make-secrets.sh" "$dir/bad" "$platform"
  [ -d "$src/clean" ] && cp -R "$src/clean" "$dir/clean"

  if [ -d "$dir/bad" ]; then
    sh "$engine" --platform "$platform" --root "$dir/bad" --format ids > "$tmp/$name.ids" 2> "$tmp/$name.err"; rc=$?
    if [ "$rc" -eq 1 ]; then ok "$name/bad exits 1"; else bad "$name/bad exit code $rc (expected 1)"; sed 's/^/    /' "$tmp/$name.err"; fi
    [ -s "$tmp/$name.err" ] && { bad "$name/bad wrote to stderr"; sed 's/^/    /' "$tmp/$name.err"; }
    cut -f1 "$tmp/$name.ids" | LC_ALL=C sort -u > "$tmp/$name.got"
    grep -v '^SUPPRESSED:' "$tmp/$name.got" >> "$tmp/fired"
    if [ -f "$src/EXPECTED.txt" ]; then
      tr -d '\r' < "$src/EXPECTED.txt" | grep -v '^#' | grep -v '^$' | LC_ALL=C sort -u > "$tmp/$name.exp"
      if diff "$tmp/$name.exp" "$tmp/$name.got" > "$tmp/$name.diff"; then
        ok "$name/bad fires exactly the expected rules ($(grep -c '' "$tmp/$name.exp"))"
      else
        bad "$name/bad rule set differs from EXPECTED.txt (< expected, > got)"
        sed 's/^/    /' "$tmp/$name.diff"
      fi
    else
      bad "$name has no EXPECTED.txt"
    fi
    sh "$engine" --platform "$platform" --root "$dir/bad" --format json > "$tmp/$name.json" 2>/dev/null
    if command -v python3 >/dev/null 2>&1; then
      if python3 -c 'import json,sys; json.load(open(sys.argv[1]))' "$tmp/$name.json" 2>/dev/null; then ok "$name/bad JSON report parses"; else bad "$name/bad JSON report does not parse"; fi
    fi
    if sh "$engine" --platform "$platform" --root "$dir/bad" --threshold none --format ids > /dev/null 2>&1; then ok "$name/bad passes with --threshold none"; else bad "$name/bad still fails with --threshold none"; fi
  fi

  if [ -d "$dir/clean" ]; then
    sh "$engine" --platform "$platform" --root "$dir/clean" --format ids > "$tmp/$name.clean" 2> "$tmp/$name.cerr"; rc=$?
    if [ "$rc" -eq 0 ] && [ ! -s "$tmp/$name.clean" ]; then
      ok "$name/clean exits 0 with no findings"
    else
      bad "$name/clean exit $rc with findings:"; sed 's/^/    /' "$tmp/$name.clean" "$tmp/$name.cerr"
    fi
  fi
done

# coverage: every rule must fire somewhere
LC_ALL=C sort -u "$tmp/fired" > "$tmp/fired.u"
cat "$repo"/core/rules/*.rules | tr -d '\r' | grep -v '^[[:space:]]*#' | grep -v '^[[:space:]]*$' | cut -f1 | LC_ALL=C sort -u > "$tmp/all"
if comm -23 "$tmp/all" "$tmp/fired.u" > "$tmp/uncovered" && [ ! -s "$tmp/uncovered" ]; then
  ok "every rule ($(grep -c '' "$tmp/all")) fires in at least one bad fixture"
else
  bad "rules with no bad fixture coverage:"; sed 's/^/    /' "$tmp/uncovered"
fi

# suppression rules: each case builds a one-file Android project and checks the fate
# of one finding (active = still reported and blocking, suppressed = accepted).
#   sup_case <name> <active|suppressed> <rule id[@file:line]> <file> <file content> [ignore-file content]
sup_case() {
  d=$tmp/sup/$1
  mkdir -p "$d/$(dirname "$4")"
  printf '%b' "$5" > "$d/$4"
  [ -n "${6:-}" ] && printf '%b' "$6" > "$d/.build-validator-ignore"
  sh "$engine" --platform android --root "$d" --format ids > "$d.ids" 2> "$d.err"
  rid=${3%%@*}; at="[^	]*"; case "$3" in *@*) at=${3#*@} ;; esac
  if grep -q "^SUPPRESSED:$rid	$at	" "$d.ids"; then got=suppressed
  elif grep -q "^$rid	$at	" "$d.ids"; then got=active
  else got=none; fi
  if [ "$got" = "$2" ]; then ok "suppression: $1 ($got)"
  else bad "suppression: $1 is $got, expected $2"; sed 's/^/    /' "$d.ids" "$d.err"; fi
}
kt=app/src/main/java/T.kt
call='    w.addJavascriptInterface(B(), "B")\n'
mf=app/src/main/AndroidManifest.xml
dbg='    <application android:debuggable="true" />\n</manifest>\n'
sup_case inline-valid suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: bundled help pages only, SEC-1\n$call}\n"
sup_case inline-same-line suppressed AND-WV-001 "$kt" "class T {\n    w.addJavascriptInterface(B(), \"B\") // build-validator:ignore AND-WV-001 reason: bundled pages\n}\n"
sup_case inline-id-list suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-007, AND-WV-001 reason: bundled help pages only\n$call}\n"
sup_case inline-all-with-reason suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore all reason: generated code, SEC-7\n$call}\n"
sup_case inline-no-reason active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001\n$call}\n"
sup_case inline-empty-block-reason active AND-WV-001 "$kt" "class T {\n    /* build-validator:ignore AND-WV-001 reason: */\n$call}\n"
sup_case inline-blank-block-reason active AND-WV-001 "$kt" "class T {\n    /* build-validator:ignore AND-WV-001 reason:    */\n$call}\n"
sup_case inline-punctuation-reason active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: -\n$call}\n"
sup_case inline-xml-empty-reason active AND-DBG-001 "$mf" "<manifest>\n    <!-- build-validator:ignore AND-DBG-001 reason: -->\n$dbg"
sup_case inline-xml-valid suppressed AND-DBG-001 "$mf" "<manifest>\n    <!-- build-validator:ignore AND-DBG-001 reason: QA flavor only, SEC-9 -->\n$dbg"
sup_case inline-other-id-in-reason active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-007 reason: unlike AND-WV-001 this is safe\n$call}\n"
sup_case inline-word-all-in-reason active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-007 reason: we call all endpoints\n$call}\n"
sup_case inline-id-prefix-only active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-0011 reason: different rule\n$call}\n"
sup_case inline-reason-before-marker active AND-WV-001 "$kt" "class T {\n    // reason: legacy build-validator:ignore AND-WV-001\n$call}\n"
sup_case inline-two-lines-above active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: too far away\n\n$call}\n"
sup_case file-valid suppressed AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tapp/*\tlegacy bridge, removal tracked in SEC-1\n'
sup_case file-empty-reason active AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tapp/*\t   \n'
sup_case file-punctuation-reason active AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tapp/*\t-\n'
sup_case file-blanket-all active AND-WV-001 "$kt" "class T {\n$call}\n" 'all\t*\tlegacy\n'
sup_case file-all-narrow-glob active AND-WV-001 "$kt" "class T {\n$call}\n" 'all\tapp/src/main/java/T.kt\tlegacy\n'
sup_case file-all-uppercase active AND-WV-001 "$kt" "class T {\n$call}\n" 'ALL\t*\tlegacy\n'
# a comment closer ends the reason; code after it is not a reason
sup_case inline-closer-then-code active AND-WV-001 "$kt" "class T {\n    /* build-validator:ignore AND-WV-001 reason: */ w.addJavascriptInterface(B(), \"B\")\n}\n"
sup_case inline-closer-then-code-above active AND-WV-001 "$kt" "class T {\n    /* build-validator:ignore AND-WV-001 reason: */ val x = 1\n$call}\n"
sup_case inline-xml-closer-then-tag active AND-DBG-001 "$mf" "<manifest>\n    <!-- build-validator:ignore AND-DBG-001 reason: --><application android:debuggable=\"true\" />\n</manifest>\n"
# the marker must open a comment, stand alone as a word, and a trailing comment covers only its own line
sup_case inline-marker-in-string active AND-WV-001 "$kt" "class T {\n    log(\"build-validator:ignore all reason:\", tag)\n$call}\n"
sup_case inline-marker-glued-all active AND-WV-001 "$kt" "class T {\n    // build-validator:ignoreall reason: bundled pages\n$call}\n"
sup_case inline-marker-prefixed active AND-WV-001 "$kt" "class T {\n    // xbuild-validator:ignore all reason: bundled pages\n$call}\n"
sup_case inline-trailing-next-line active "AND-WV-001@$kt:3" "$kt" "class T {\n    w.addJavascriptInterface(A(), \"A\") // build-validator:ignore AND-WV-001 reason: A serves bundled pages\n$call}\n"
sup_case inline-trailing-own-line suppressed "AND-WV-001@$kt:2" "$kt" "class T {\n    w.addJavascriptInterface(A(), \"A\") // build-validator:ignore AND-WV-001 reason: A serves bundled pages\n$call}\n"
sup_case inline-no-id-list active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore reason: bundled pages\n$call}\n"
# legitimate forms keep working
sup_case inline-jsx-comment suppressed AND-WV-001 "$kt" "class T {\n    {/* build-validator:ignore AND-WV-001 reason: bundled pages */}\n$call}\n"
sup_case inline-javadoc-line suppressed AND-WV-001 "$kt" "class T {\n     * build-validator:ignore AND-WV-001 reason: bundled pages\n$call}\n"
sup_case inline-non-ascii-reason suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: только встроенные страницы\n$call}\n"
sup_case inline-nbsp-after-marker suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore\0302\0240AND-WV-001 reason: bundled pages\n$call}\n"
sup_case inline-semicolon-list suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-007; AND-WV-001 reason: bundled pages\n$call}\n"
sup_case inline-id-colon suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001: reason: bundled pages\n$call}\n"
sup_case inline-crlf suppressed AND-WV-001 "$kt" "class T {\r\n    // build-validator:ignore AND-WV-001 reason: bundled pages\r\n    w.addJavascriptInterface(B(), \"B\")\r\n}\r\n"
sup_case inline-nbsp-only-reason active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: \0302\0240\0302\0240\n$call}\n"
sup_case inline-zero-width-reason active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: \0342\0200\0213\0342\0200\0213\n$call}\n"
# project-file globs: only * and ? are special
sup_case file-pipe-glob active AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tdocs/legacy.kt|\tlegacy bridge, SEC-1\n'
sup_case file-regex-glob active AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\t^app.*$\tlegacy bridge, SEC-1\n'
sup_case file-plus-in-path suppressed AND-WV-001 "app/src/main/java/c++/T.kt" "class T {\n$call}\n" 'AND-WV-001\tapp/src/main/java/c++/*\tvendored, SEC-2\n'
sup_case file-bad-glob-then-valid suppressed AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tlib/(old\tvendored, SEC-2\nAND-WV-001\tapp/*\tlegacy bridge, SEC-1\n'
sup_case file-non-ascii-reason suppressed AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tapp/*\tустаревший мост\n'
sup_case file-bom suppressed AND-WV-001 "$kt" "class T {\n$call}\n" '\0357\0273\0277AND-WV-001\tapp/*\tlegacy bridge, SEC-1\n'
for f in json markdown xcode; do
  if sh "$engine" --platform android --root "$tmp/sup/file-blanket-all" --format "$f" 2>/dev/null | grep -q 'not accepted'; then
    ok "suppression: blanket all is reported as not accepted ($f)"
  else
    bad "suppression: blanket all is not reported in $f output"
  fi
done

# file names must not change what is scanned or which findings survive
d=$tmp/names; mkdir -p "$d/app/src/main/java"
printf 'class Z {\n    w.addJavascriptInterface(B(), "B")\n}\n' > "$d/zz=1.kt"
printf 'class T {\n    w.addJavascriptInterface(B(), "B")\n}\n' > "$d/app/src/main/java/T.kt"
printf 'class S {\n  override fun onReceivedSslError(v: WebView, h: SslErrorHandler, e: SslError) {\n    h.proceed()\n  }\n}\n' > "$d/a=b.kt"
cp "$d/a=b.kt" "$d/app/src/main/java/S.kt"
sh "$engine" --platform android --root "$d" --format ids > "$d.ids" 2>/dev/null
if [ "$(grep -c '^AND-WV-001	' "$d.ids")" -eq 2 ] && [ "$(grep -c '^AND-WV-007	' "$d.ids")" -eq 2 ]; then
  ok "file names with \"=\" do not hide findings"
else
  bad "file names with \"=\" hide findings:"; sed 's/^/    /' "$d.ids"
fi
d=$tmp/colon; mkdir -p "$d/app/src/main/java/pkg:1:x"
printf 'class T {\n    w.addJavascriptInterface(B(), "B")\n}\n' > "$d/app/src/main/java/pkg:1:x/T.kt"
printf '// build-validator:ignore all reason: generated\n' > "$d/app/src/main/java/pkg"
sh "$engine" --platform android --root "$d" --format ids > "$d.ids" 2>/dev/null
if grep -q '^AND-WV-001	app/src/main/java/pkg:1:x/T.kt:2	' "$d.ids"; then
  ok "a \":\" in a path keeps the finding on its own file"
else
  bad "a \":\" in a path moves the finding:"; sed 's/^/    /' "$d.ids"
fi
d=$tmp/symlink; mkdir -p "$d/app/src/main/java"
printf 'class T {\n    w.addJavascriptInterface(B(), "B")\n}\n' > "$d/app/src/main/java/T.kt"
ln -s /dev/stdin "$d/.build-validator-ignore"
if printf 'AND-WV-001\t*\tfrom stdin\n' | sh "$engine" --platform android --root "$d" --format ids 2>/dev/null | grep -q '^AND-WV-001	'; then
  ok "a symlinked .build-validator-ignore is not read"
else
  bad "a symlinked .build-validator-ignore was read"
fi

# a marker inside a string literal is not a directive; quotes in code before a real
# trailing comment still work
sup_case inline-marker-in-string-slashes active AND-WV-001 "$kt" "class T {\n    w.addJavascriptInterface(B(), \"// build-validator:ignore AND-WV-001 reason: x\")\n}\n"
sup_case inline-marker-in-string-hash active AND-WV-001 "$kt" "class T {\n    w.addJavascriptInterface(B(), \"# build-validator:ignore AND-WV-001 reason: x\")\n}\n"
sup_case inline-trailing-after-apostrophe suppressed AND-WV-001 "$kt" "class T {\n    w.addJavascriptInterface(B(), \"it's\") // build-validator:ignore AND-WV-001 reason: bundled pages\n}\n"
sup_case inline-trailing-after-char-literal suppressed AND-WV-001 "$kt" "class T {\n    w.addJavascriptInterface(B('a'), \"B\") // build-validator:ignore AND-WV-001 reason: bundled pages\n}\n"
sup_case inline-trailing-after-escaped-quote suppressed AND-WV-001 "$kt" "class T {\n    w.addJavascriptInterface(B(), \"a\\\\\"\") // build-validator:ignore AND-WV-001 reason: bundled pages\n}\n"
# a reason needs a letter or digit: spaces, symbols, emoji and invisible characters do not count
for inv in 'em-space:\0342\0200\0203' 'em-dash:\0342\0200\0224' 'lrm:\0342\0200\0216' 'hangul-filler:\0343\0205\0244' \
           'braille-blank:\0342\0240\0200' 'soft-hyphen:\0302\0255' 'byte-ff:\0377' 'emoji:\0360\0237\0221\0215'; do
  sup_case "inline-reason-${inv%%:*}" active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: ${inv#*:}\n$call}\n"
done
sup_case file-reason-nbsp active AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tapp/*\t\0302\0240\n'
sup_case file-reason-em-space active AND-WV-001 "$kt" "class T {\n$call}\n" 'AND-WV-001\tapp/*\t\0342\0200\0203\n'
sup_case inline-reason-vietnamese suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: chỉ trang nội bộ\n$call}\n"
sup_case inline-reason-arabic suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: صفحات مضمنة فقط\n$call}\n"
sup_case inline-reason-greek suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: μόνο ενσωματωμένες σελίδες\n$call}\n"
sup_case inline-reason-japanese suppressed AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason: 同梱ヘルプページのみ\n$call}\n"
# a lone carriage return or a Unicode line break: the compiler sees a new line there
sup_case inline-lone-cr active AND-WV-001 "$kt" "class T {\r    // build-validator:ignore AND-WV-001 reason: bundled pages\r    w.addJavascriptInterface(B(), \"B\")\r}\r"
sup_case inline-line-separator-code active AND-WV-001 "$kt" "class T {\n    // build-validator:ignore AND-WV-001 reason:\0342\0200\0250w.addJavascriptInterface(B(), \"B\")\n}\n"

# a file whose name embeds another file's path and line cannot take over its finding
d=$tmp/decoy; mkdir -p "$d/app/src/main/java/T.kt:2:"
printf 'class T {\n/*x.kt:1:*/ w.addJavascriptInterface(B(), "B")\n}\n' > "$d/app/src/main/java/T.kt"
printf '// build-validator:ignore all reason: generated\n' > "$d/app/src/main/java/T.kt:2:/*x.kt"
d2=$tmp/decoy2; mkdir -p "$d2/app/src/main/AndroidManifest.xml:2:<!--/src/debug"
printf '<manifest>\n<!--/src/debug/AndroidManifest.xml:1:--><application android:debuggable="true" />\n</manifest>\n' > "$d2/app/src/main/AndroidManifest.xml"
printf '<manifest/>\n' > "$d2/app/src/main/AndroidManifest.xml:2:<!--/src/debug/AndroidManifest.xml"
if sh "$engine" --platform android --root "$d" --format ids 2>/dev/null | grep -q '^AND-WV-001	app/src/main/java/T.kt:2	' && \
   sh "$engine" --platform android --root "$d2" --format ids 2>/dev/null | grep -q '^AND-DBG-001	app/src/main/AndroidManifest.xml:2	'; then
  ok "a decoy file name cannot take over another file's finding"
else
  bad "a decoy file name took over another file's finding"
fi

# invalid UTF-8 (a Latin-1 source file) in a UTF-8 locale must not lose findings
d=$tmp/latin1; mkdir -p "$d/app/src/main/java"
printf '// Auteur: Ren\351\nclass S : WebViewClient() {\n  override fun onReceivedSslError(v: WebView, h: SslErrorHandler, e: SslError) {\n    h.proceed()\n  }\n}\n' > "$d/app/src/main/java/S.kt"
printf 'class A {\n  // Copyright \251 ACME\n  fun a(w: WebView) { w.loadUrl("file:///sdcard/a.html") } // r\351sum\351\n}\n' > "$d/app/src/main/java/A.kt"
printf 'class B {\n  fun b(w: WebView) { w.loadUrl("file:///sdcard/b.html") }\n}\n' > "$d/app/src/main/java/B.kt"
LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8 sh "$engine" --platform android --root "$d" --format ids > "$d.ids" 2>/dev/null
if grep -q '^AND-WV-007	app/src/main/java/S.kt:' "$d.ids" && [ "$(grep -c '^AND-WV-006	' "$d.ids")" -eq 2 ]; then
  ok "Latin-1 source files keep their findings in a UTF-8 locale"
else
  bad "Latin-1 source files lose findings in a UTF-8 locale:"; sed 's/^/    /' "$d.ids"
fi

# a tab or newline in a scanned file name stops the scan instead of hiding findings
for nm in 'tab:a\tb.kt' 'newline:a\nb.kt'; do
  d=$tmp/name-${nm%%:*}; mkdir -p "$d/app/src/main/java"
  printf 'class T {\n    w.addJavascriptInterface(B(), "B")\n}\n' > "$d/app/src/main/java/$(printf "${nm#*:}")"
  sh "$engine" --platform android --root "$d" --format ids > /dev/null 2> "$d.err"; rc=$?
  if [ "$rc" -eq 2 ] && grep -q 'not supported' "$d.err"; then
    ok "a ${nm%%:*} in a file name stops the scan (exit 2)"
  else
    bad "a ${nm%%:*} in a file name: exit $rc"; sed 's/^/    /' "$d.err"
  fi
done

# the committed threat index matches the threat library it ships beside (it once drifted
# to another branch's cards, so findings cited references the vendored cards did not)
if [ -d "$repo/../threats" ]; then
  THREAT_INDEX_OUT="$tmp/threat-index.rebuilt.tsv" sh "$repo/core/tools/build-threat-index.sh" "$repo/../threats" > /dev/null 2>&1
  if cmp -s "$tmp/threat-index.rebuilt.tsv" "$repo/core/rules/threat-index.tsv"; then
    ok "threat-index.tsv matches the cards in ../threats"
  else
    bad "threat-index.tsv is out of date with ../threats: run sh core/tools/build-threat-index.sh"
    diff "$repo/core/rules/threat-index.tsv" "$tmp/threat-index.rebuilt.tsv" | head -6 | sed 's/^/    /'
  fi
fi

# every rule references a card that exists in the threat index
cut -f6 "$repo/core/rules/threat-index.tsv" | sed 1d | LC_ALL=C sort -u > "$tmp/cards"
cat "$repo"/core/rules/*.rules | tr -d '\r' | grep -v '^[[:space:]]*#' | grep -v '^[[:space:]]*$' | cut -f8 | LC_ALL=C sort -u > "$tmp/rulecards"
if comm -23 "$tmp/rulecards" "$tmp/cards" > "$tmp/badcards" && [ ! -s "$tmp/badcards" ]; then
  ok "every rule points at a card in threat-index.tsv"
else
  bad "rules point at unknown cards:"; sed 's/^/    /' "$tmp/badcards"
fi
cat "$repo"/core/rules/*.rules | tr -d '\r' | grep -v '^[[:space:]]*#' | grep -v '^[[:space:]]*$' | cut -f2 | LC_ALL=C sort -u > "$tmp/ruleids"
cut -f1 "$repo/core/rules/threat-index.tsv" | sed 1d | LC_ALL=C sort -u > "$tmp/ids"
if comm -23 "$tmp/ruleids" "$tmp/ids" > "$tmp/badids" && [ ! -s "$tmp/badids" ]; then
  ok "every rule's threat id exists in threat-index.tsv"
else
  bad "rules use unknown threat ids:"; sed 's/^/    /' "$tmp/badids"
fi
# rule lines must have 8 to 10 tab-separated fields and no empty field
awk -F '\t' '/^[[:space:]]*(#|$)/ { next } NF < 8 || NF > 10 { print FILENAME ":" FNR ": " NF " fields" } { for (i = 1; i <= NF; i++) if ($i == "") print FILENAME ":" FNR ": empty field " i }' "$repo"/core/rules/*.rules > "$tmp/shape"
if [ -s "$tmp/shape" ]; then bad "malformed rule lines:"; sed 's/^/    /' "$tmp/shape"; else ok "all rule lines are well formed"; fi

for s in "$repo"/core/*.sh "$repo"/core/lib/*.sh "$repo"/core/tools/*.sh "$repo"/*/validate-build.sh "$repo"/*/install.sh "$repo"/*/*.sh; do
  [ -f "$s" ] || continue
  if sh -n "$s" 2>/dev/null; then :; else bad "syntax error: $s"; fi
done
ok "shell syntax check on all scripts"

printf '\n%s passed, %s failed\n' "$pass" "$fail"
[ "$fail" -eq 0 ]
