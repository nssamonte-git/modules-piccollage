#!/bin/sh
# Installs the build gate into a mobile project. Called by <platform>/install.sh.
#
# Usage: sh core/tools/install.sh --platform <p> --from <platform-folder> <project-dir>
#          [--with-cards] [--threats-dir <dir>] [--apply-gradle] [--force]
#
#   --with-cards     vendor only the threat cards the installed rules cite into
#                    build-validator/core/threats/, so findings print the card's
#                    "How to Fix" section on any machine
#   --threats-dir    threat library to copy cards from (default: $THREATS_DIR,
#                    then $AI_KNOWLEDGE_REPO/threats, then the threats/ folder
#                    beside mobile-build-validator/, then
#                    "$HOME/Projects/AI Knowledge and Skills Repo/threats")
#   --apply-gradle   append the apply-from line to the app module's build file
#   --force          install into an existing build-validator/ folder that this
#                    installer did not create
set -u

platform=""; from=""; project=""; with_cards=0; apply_gradle=0; force=0
if [ -n "${THREATS_DIR:-}" ]; then
  threats_dir=$THREATS_DIR
elif [ -n "${AI_KNOWLEDGE_REPO:-}" ]; then
  threats_dir=$AI_KNOWLEDGE_REPO/threats
elif [ -d "$(dirname "$0")/../../../threats" ]; then
  threats_dir=$(cd "$(dirname "$0")/../../../threats" && pwd)
else
  threats_dir="$HOME/Projects/AI Knowledge and Skills Repo/threats"
fi

usage() { sed -n '2,17p' "$0" | sed 's/^# \{0,1\}//'; }
die() { printf 'ERROR: %s\n' "$1" >&2; exit 2; }

while [ "$#" -gt 0 ]; do
  case "$1" in
    --platform) platform=${2:-}; shift 2 ;;
    --from) from=${2:-}; shift 2 ;;
    --with-cards) with_cards=1; shift ;;
    --threats-dir) threats_dir=${2:-}; shift 2 ;;
    --apply-gradle) apply_gradle=1; shift ;;
    --force) force=1; shift ;;
    -h|--help) usage; exit 0 ;;
    -*) usage >&2; die "unknown option: $1" ;;
    *) [ -z "$project" ] || die "only one project directory is allowed"; project=$1; shift ;;
  esac
done

case "$platform" in
  android) sets="shared android"; label="Native Android" ;;
  ios) sets="shared ios"; label="Native iOS" ;;
  flutter) sets="shared flutter android ios"; label="Flutter" ;;
  react-native) sets="shared react-native android ios"; label="React Native" ;;
  *) die "--platform must be android, ios, flutter or react-native" ;;
esac
[ -n "$from" ] && [ -f "$from/validate-build.sh" ] || die "--from must be a platform folder with validate-build.sh"
[ -n "$project" ] || { usage >&2; die "missing <project-dir>"; }
[ -d "$project" ] || die "project directory not found: $project"

tools=$(cd "$(dirname "$0")" && pwd)
core=$(cd "$tools/.." && pwd)
project=$(cd "$project" && pwd)
dest=$project/build-validator

# Light sanity check of the project type (warn only).
case "$platform" in
  android) [ -f "$project/settings.gradle" ] || [ -f "$project/settings.gradle.kts" ] || \
             printf 'WARN: no settings.gradle(.kts) in %s; is this the Android project root?\n' "$project" ;;
  ios) ls "$project"/*.xcodeproj >/dev/null 2>&1 || ls "$project"/*.xcworkspace >/dev/null 2>&1 || \
             printf 'WARN: no .xcodeproj/.xcworkspace in %s; is this the iOS project root?\n' "$project" ;;
  flutter) [ -f "$project/pubspec.yaml" ] || printf 'WARN: no pubspec.yaml in %s; is this the Flutter project root?\n' "$project" ;;
  react-native) [ -f "$project/package.json" ] || printf 'WARN: no package.json in %s; is this the React Native project root?\n' "$project" ;;
esac

if [ -d "$dest" ] && [ ! -f "$dest/INSTALLED.md" ] && [ "$force" -eq 0 ]; then
  die "$dest exists and was not created by this installer (use --force to install into it)"
fi

mkdir -p "$dest/core/lib" "$dest/core/rules"
cp "$core/threat-scan.sh" "$dest/core/"
cp "$core"/lib/*.sh "$dest/core/lib/"
cp "$core"/rules/*.rules "$core/rules/threat-index.tsv" "$dest/core/rules/"
for f in "$from"/*; do
  b=$(basename "$f")
  [ "$b" = install.sh ] && continue
  cp "$f" "$dest/$b"
done
chmod +x "$dest"/*.sh "$dest/core/threat-scan.sh" 2>/dev/null || true

cards_note="not vendored (findings show the card path; pass --with-cards to include them)"
if [ "$with_cards" -eq 1 ]; then
  if [ -d "$threats_dir" ]; then
    n=0
    for s in $sets; do cut -f8 "$core/rules/$s.rules" | grep -v '^#' | grep '\.md$'; done | sort -u > "$dest/.cards.tmp"
    while IFS= read -r card; do
      [ -f "$threats_dir/$card" ] || { printf 'WARN: card not found in library: %s\n' "$card"; continue; }
      mkdir -p "$dest/core/threats/$(dirname "$card")"
      cp "$threats_dir/$card" "$dest/core/threats/$card"
      n=$((n + 1))
    done < "$dest/.cards.tmp"
    rm -f "$dest/.cards.tmp"
    cards_note="$n cards vendored into core/threats/ from $threats_dir"
  else
    printf 'WARN: threat library not found at %s; cards were not vendored (use --threats-dir)\n' "$threats_dir"
  fi
fi

cat > "$dest/INSTALLED.md" <<INFO
# Mobile build validator (installed copy)

- platform: $platform ($label)
- installed: $(date +%Y-%m-%d)
- source: $from
- rule sets: $sets
- threat cards: $cards_note
- informed_by: AI Knowledge and Skills Repo threat library (threats/android, threats/ios, threats/shared). The rules cite card ids; the library is not a runtime dependency.

Commit this folder so every developer and CI build runs the same gate.
Re-run the installer to update it. Accepted findings go in ../.build-validator-ignore with a reason.
INFO

if [ ! -f "$project/.build-validator-ignore" ]; then
  cat > "$project/.build-validator-ignore" <<'IGN'
# Accepted findings for the mobile build validator. One per line, tab-separated:
#   RULE_ID<TAB>path glob (relative to the project root, * = any characters, ? = one)<TAB>reason
# A reason with real text is mandatory, and RULE_ID must name one rule ("all" is not
# accepted here); other lines are ignored and the finding stays active.
# Example:
#   AND-WV-001	app/src/main/java/com/example/help/*	bridge only serves bundled offline help pages (SEC-123)
IGN
fi

gradle_apply=""
case "$platform" in
  android) gradle_rel='$rootDir/build-validator/build-validator.gradle'; app_dir=$project/app ;;
  flutter|react-native) gradle_rel='$rootDir/../build-validator/build-validator.gradle'; app_dir=$project/android/app ;;
  *) gradle_rel=""; app_dir="" ;;
esac
if [ -n "$gradle_rel" ]; then
  if [ -f "$app_dir/build.gradle.kts" ]; then build_file=$app_dir/build.gradle.kts; gradle_apply="apply(from = \"$gradle_rel\")"
  elif [ -f "$app_dir/build.gradle" ]; then build_file=$app_dir/build.gradle; gradle_apply="apply from: \"$gradle_rel\""
  else build_file=""; gradle_apply="apply from: \"$gradle_rel\"   (Kotlin DSL: apply(from = \"$gradle_rel\"))"
  fi
  if [ "$apply_gradle" -eq 1 ]; then
    if [ -z "$build_file" ]; then
      printf 'WARN: --apply-gradle: no build.gradle(.kts) in %s; add the line yourself\n' "$app_dir"
    elif grep -q 'build-validator.gradle' "$build_file"; then
      printf 'Gradle hook already present in %s\n' "$build_file"
    else
      printf '\n// Mobile build validator: stop the build on unfixed security findings\n%s\n' "$gradle_apply" >> "$build_file"
      printf 'Added the Gradle hook to %s\n' "$build_file"
    fi
  fi
fi

printf '\nInstalled the %s build gate into %s\n\n' "$label" "$dest"
printf 'Wire it into the build:\n'
step=1
if [ -n "$gradle_rel" ]; then
  if [ "$apply_gradle" -eq 1 ] && [ -n "$build_file" ]; then
    printf '  %s. Android: done (%s)\n' "$step" "${build_file#$project/}"
  else
    printf '  %s. Android: add to %s, after the plugins block:\n       %s\n' "$step" "${app_dir#$project/}/build.gradle(.kts)" "$gradle_apply"
  fi
  step=$((step + 1))
fi
case "$platform" in
  ios|flutter|react-native)
    phase='sh "${SRCROOT}/build-validator/xcode-run-script.sh"'
    [ "$platform" != ios ] && phase='sh "${SRCROOT}/../build-validator/xcode-run-script.sh"'
    printf '  %s. iOS: in Xcode, app target > Build Phases > + New Run Script Phase,\n' "$step"
    printf '       move it above "Compile Sources", uncheck "Based on dependency analysis",\n'
    printf '       body: %s\n' "$phase"
    printf '       and set User Script Sandboxing (ENABLE_USER_SCRIPT_SANDBOXING) = No for the target.\n'
    step=$((step + 1)) ;;
esac
case "$platform" in
  react-native) printf '  %s. npm: merge build-validator/package-scripts.json into package.json "scripts".\n' "$step" ;;
  flutter) printf '  %s. CI (optional): sh build-validator/flutter-build.sh build apk --release --obfuscate --split-debug-info=build/symbols\n' "$step" ;;
esac
printf '  Commit build-validator/ and .build-validator-ignore.\n\n'

printf 'Baseline scan (report only, nothing is blocked yet):\n\n'
sh "$dest/validate-build.sh" --threshold none --no-fix-detail || true
