#!/bin/sh
# Installs the Flutter build gate into a project.
# Usage: sh "Flutter/install.sh" <project-dir> [--with-cards] [--apply-gradle] [--threats-dir <dir>] [--force]
# See core/tools/install.sh for the options.
here=$(cd "$(dirname "$0")" && pwd)
exec sh "$here/../core/tools/install.sh" --platform flutter --from "$here" "$@"
