#!/bin/sh
# Mobile build validator: Xcode "Run Script" build phase.
#
# In the app target: Build Phases > + > New Run Script Phase, then
#   1. drag the phase ABOVE "Compile Sources",
#   2. uncheck "Based on dependency analysis" so it runs on every build,
#   3. use this one-line body:
#        sh "${SRCROOT}/../build-validator/xcode-run-script.sh"
#   4. Build Settings > User Script Sandboxing (ENABLE_USER_SCRIPT_SANDBOXING) = No
#      for this target, because the scan reads the whole source tree.
#
# Findings appear in the Issue navigator; blocking ones fail the build.
set -u
here=$(cd "$(dirname "$0")" && pwd)

if [ "${ENABLE_USER_SCRIPT_SANDBOXING:-NO}" = "YES" ]; then
  echo "error: Mobile build validator cannot read the source tree because User Script Sandboxing is on for target ${TARGET_NAME:-app}. Set ENABLE_USER_SCRIPT_SANDBOXING = NO for this target."
  exit 1
fi

report="${DERIVED_FILE_DIR:-${TMPDIR:-/tmp}}/build-validator-report.json"
sh "$here/validate-build.sh" --format xcode --report "$report"
