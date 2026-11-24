#!/bin/sh
# Mobile build validator: Flutter (Dart + android/ + ios/) build gate.
#
# Installed layout (run by the build hooks):
#   <project>/build-validator/validate-build.sh   scans <project>
# From this repository:
#   sh "Flutter/validate-build.sh" --root /path/to/project
#
# Exit codes: 0 build may continue, 1 blocking findings, 2 configuration error.
# Extra arguments are passed to core/threat-scan.sh (see --help).
set -u

PLATFORM=flutter
# Findings at or above this severity stop the build: critical | high | medium | low.
# Lowering it is a code change that shows up in review.
THRESHOLD=high

here=$(cd "$(dirname "$0")" && pwd)
if [ -f "$here/core/threat-scan.sh" ]; then
  core=$here/core
  root=$(cd "$here/.." && pwd)
elif [ -f "$here/../core/threat-scan.sh" ]; then
  core=$(cd "$here/../core" && pwd)
  root=$(pwd)
else
  printf 'ERROR: build validator engine not found (expected core/threat-scan.sh next to %s)\n' "$0" >&2
  exit 2
fi

exec sh "$core/threat-scan.sh" --platform "$PLATFORM" --threshold "$THRESHOLD" --root "$root" "$@"
