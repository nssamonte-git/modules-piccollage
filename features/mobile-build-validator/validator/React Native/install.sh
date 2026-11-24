#!/bin/sh
# Installs the React Native build gate into a project.
# Usage: sh "React Native/install.sh" <project-dir> [--with-cards] [--apply-gradle] [--threats-dir <dir>] [--force]
# See core/tools/install.sh for the options.
here=$(cd "$(dirname "$0")" && pwd)
exec sh "$here/../core/tools/install.sh" --platform react-native --from "$here" "$@"
