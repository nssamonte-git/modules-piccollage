#!/bin/sh
# Writes provider-format fake credentials into a fixture copy at test time.
# They are assembled from fragments so this repository never contains
# strings that secret scanners (and push protection) treat as live keys.
# Usage: sh make-secrets.sh <fixture-copy-dir> <android|ios|flutter|react-native>
set -eu
dir=$1; platform=$2
aws="AK""IA""Q3KJ7NZ5HGR2WXYP"
gkey="AI""za""SyD4mF7x9Kq2Lw8Pv3Rt6Yb1Nc5Hj0Ue7Gi"
pem="-----BEGIN ""RSA PRIVATE KEY-----"
case "$platform" in
  android) f="$dir/app/src/main/java/com/example/bad/Keys.kt"
           printf 'package com.example.bad\nobject Keys {\n    const val AWS = "%s"\n    const val MAPS = "%s"\n}\n' "$aws" "$gkey" > "$f" ;;
  ios)     f="$dir/App/Keys.swift"
           printf 'enum Keys {\n    static let aws = "%s"\n    static let maps = "%s"\n}\n' "$aws" "$gkey" > "$f" ;;
  flutter) f="$dir/lib/keys.dart"
           printf "const aws = '%s';\nconst maps = '%s';\n" "$aws" "$gkey" > "$f" ;;
  react-native) f="$dir/src/keys.js"
           printf "export const aws = '%s';\nexport const maps = '%s';\n" "$aws" "$gkey" > "$f" ;;
  *) echo "unknown platform $platform" >&2; exit 2 ;;
esac
printf '%s\nMIIBOgIBAAJBAKj34GkxFhD90vcNLYLInFEX6Ppy1tPf9Cnzj4p4WGeKLs1Pt8Qu\n-----END RSA PRIVATE KEY-----\n' "$pem" > "$dir/server-key.pem"
