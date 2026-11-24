#!/bin/sh
# Installs the Native Android build gate into a project.
# Usage: sh "Native Android/install.sh" <project-dir> [--with-cards] [--apply-gradle] [--threats-dir <dir>] [--force]
# See core/tools/install.sh for the options.
here=$(cd "$(dirname "$0")" && pwd)
exec sh "$here/../core/tools/install.sh" --platform android --from "$here" "$@"
