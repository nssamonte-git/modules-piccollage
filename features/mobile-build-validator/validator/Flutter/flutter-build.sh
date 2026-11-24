#!/bin/sh
# Runs the threat gate, then flutter with the same arguments, for teams that
# want the gate before any Flutter tooling starts (CI, scripts):
#   sh build-validator/flutter-build.sh build apk --release --obfuscate --split-debug-info=build/symbols
#   sh build-validator/flutter-build.sh build ipa
# The Gradle and Xcode hooks still run inside the build itself.
# Android release builds (build apk|appbundle without --debug/--profile) must
# pass --obfuscate and --split-debug-info; the Gradle hook enforces the same.
set -u
here=$(cd "$(dirname "$0")" && pwd)
sh "$here/validate-build.sh" || exit $?

if [ "${1:-}" = build ] && { [ "${2:-}" = apk ] || [ "${2:-}" = appbundle ]; }; then
  release=1; obf=0; sdi=0
  for a in "$@"; do
    case $a in
      --debug|--profile) release=0 ;;
      --obfuscate) obf=1 ;;
      --split-debug-info|--split-debug-info=*) sdi=1 ;;
    esac
  done
  if [ "$release" -eq 1 ] && { [ "$obf" -eq 0 ] || [ "$sdi" -eq 0 ]; }; then
    echo "Mobile build validator: release $2 builds must be obfuscated." >&2
    echo "  Add: --obfuscate --split-debug-info=<dir> (keep that folder for crash symbolication)." >&2
    exit 1
  fi
fi
exec flutter "$@"
