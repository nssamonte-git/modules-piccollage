#!/bin/sh
# Refreshes this kit's copies of the mobile build validator and the threat cards
# from the AI Knowledge and Skills Repo. Maintainers only: the library never
# needs the canonical repo to install or run the validator.
#
# Usage: sh features/mobile-build-validator/sync-from-canonical.sh <canonical-repo-dir>
#
# It copies into a staging folder, runs the validator's test suite there, and
# replaces validator/ and threats/ only when every test passes. It then updates
# the provenance in FEATURE.md. The Gradle plugin (build-validator-plugin/) bundles
# these folders, so run its tests and bump library.version afterwards.
set -u

die() { printf 'ERROR: %s\n' "$1" >&2; exit 2; }

[ "$#" -eq 1 ] || die "usage: sh $0 <canonical-repo-dir>"
src=$1
[ -d "$src/mobile-build-validator/core" ] || die "no mobile-build-validator/ in $src"
[ -d "$src/threats" ] || die "no threats/ in $src"
src=$(cd "$src" && pwd)

kit=$(cd "$(dirname "$0")" && pwd)
stage=$(mktemp -d "${TMPDIR:-/tmp}/bv-sync.XXXXXX") || die "cannot create a staging folder"
trap 'rm -rf "$stage"' EXIT INT TERM

rsync -a --exclude .DS_Store --exclude __pycache__ "$src/mobile-build-validator/" "$stage/validator/" || die "copy failed"
rsync -a --exclude .DS_Store "$src/threats/" "$stage/threats/" || die "copy failed"

printf 'Running the validator tests on the new copy...\n'
if ! sh "$stage/validator/tests/run-tests.sh"; then
  die "tests failed on the canonical copy; nothing was changed"
fi

rsync -a --delete "$stage/validator/" "$kit/validator/"
rsync -a --delete "$stage/threats/" "$kit/threats/"

commit=$(git -C "$src" log -1 --format=%h -- mobile-build-validator threats 2>/dev/null || true)
date=$(git -C "$src" log -1 --format=%ad --date=short -- mobile-build-validator threats 2>/dev/null || true)
[ -n "$commit" ] || commit=unknown
[ -n "$date" ] || date=$(date +%Y-%m-%d)
if [ -n "$(git -C "$src" status --porcelain -- mobile-build-validator threats 2>/dev/null)" ]; then
  commit="$commit + uncommitted changes"
fi

tmp=$kit/FEATURE.md.tmp
sed "s/^- version: .*/- version: $date, canonical commit \`$commit\`/" "$kit/FEATURE.md" > "$tmp" && mv "$tmp" "$kit/FEATURE.md"

printf '\nSynced from %s (%s, %s).\n' "$src" "$commit" "$date"
printf 'Next: ./gradlew -p build-validator-plugin test, bump library.version in gradle.properties,\n'
printf 'review with git diff, then commit. Apps get the new rules with the new plugin version\n'
printf '(copy-in installs: re-run the installer in each app).\n'
