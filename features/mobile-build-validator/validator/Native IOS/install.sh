#!/bin/sh
# Installs the Native IOS build gate into a project.
# Usage: sh "Native IOS/install.sh" <project-dir> [--with-cards] [--threats-dir <dir>] [--force]
# See core/tools/install.sh for the options.
here=$(cd "$(dirname "$0")" && pwd)
exec sh "$here/../core/tools/install.sh" --platform ios --from "$here" "$@"
