#!/bin/sh
# checks-android.sh - AndroidManifest.xml structural checks (rules of type
# "check" with pattern "manifest;;<mode>[;;arg]"). Output: file<TAB>line<TAB>excerpt.
#
# Modes:
#   exported_unprotected;;KINDS   component of KINDS (ERE alternation) with
#                                 android:exported="true" and no permission attribute
#                                 (launcher and BROWSABLE deep-link entry points excluded)
#   implicit_export;;KINDS        component with an <intent-filter> child and no
#                                 android:exported attribute (implicitly exported pre-12)
#   fileprovider_exported         FileProvider <provider> with android:exported="true"
#   backup_default                <application> without android:allowBackup and
#                                 without backup rules (default allows backup)
#   custom_scheme                 <data android:scheme="..."> with a non-http(s) scheme
#   applink_no_autoverify         http(s) VIEW intent-filter without android:autoVerify="true"
#   permission_no_level           <permission> without android:protectionLevel
#   undeclared_permission         enforced permission names never declared in this manifest
check_manifest() {
  parts=$(_split_parts "$2")
  MODE=$(printf '%s\n' "$parts" | sed -n '1p')
  ARG=$(printf '%s\n' "$parts" | sed -n '2p')
  while IFS= read -r f; do
    BV_FILE=$f; export BV_FILE
    [ -n "$f" ] && [ -f "$f" ] || continue
    MODE="$MODE" ARG="$ARG" awk '
      { buf = buf $0 "\n" }
      function lineof(pos,   t) { t = substr(buf, 1, pos - 1); return gsub(/\n/, "", t) + 1 }
      function attr(tag, name,   r, q) {
        if (match(tag, name "[[:space:]]*=[[:space:]]*\"[^\"]*\"")) {
          r = substr(tag, RSTART, RLENGTH); sub(/^[^"]*"/, "", r); sub(/"$/, "", r); return r
        }
        return "\001"
      }
      function has(tag, name) { return attr(tag, name) != "\001" }
      function brief(tag,   t) { t = tag; gsub(/[[:space:]]+/, " ", t); return substr(t, 1, 150) }
      # collect elements: name, start tag, body (until the matching close tag), line
      function collect(   pos, re, s, tagend, tag, name, body, ctag, bstart, cend) {
        n = 0; pos = 1
        re = "<(activity-alias|activity|service|receiver|provider|application|permission|intent-filter|data|uses-permission|uses-sdk)[[:space:]/>]"
        while (match(substr(buf, pos), re)) {
          s = pos + RSTART - 1
          tagend = index(substr(buf, s), ">")
          if (tagend == 0) break
          tag = substr(buf, s, tagend)
          name = tag; sub(/^</, "", name); sub(/[[:space:]\/>].*/, "", name)
          body = ""
          if (tag !~ /\/>$/) {
            ctag = "</" name ">"
            bstart = s + tagend
            cend = index(substr(buf, bstart), ctag)
            if (cend > 0) body = substr(buf, bstart, cend - 1)
          }
          n++; E_name[n] = name; E_tag[n] = tag; E_body[n] = body; E_line[n] = lineof(s)
          pos = s + tagend
        }
      }
      END {
        mode = ENVIRON["MODE"]; arg = ENVIRON["ARG"]
        collect()
        if (mode == "exported_unprotected") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] !~ ("^(" arg ")$")) continue
            if (attr(E_tag[i], "android:exported") != "true") continue
            if (has(E_tag[i], "android:permission") || has(E_tag[i], "android:readPermission") || has(E_tag[i], "android:writePermission")) continue
            # launcher entry points must be exported; deep-link (BROWSABLE) entry
            # points are reported by the deep-link rules instead
            if (E_body[i] ~ /android\.intent\.category\.(LAUNCHER|LEANBACK_LAUNCHER|BROWSABLE)/) continue
            print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" brief(E_tag[i])
          }
        } else if (mode == "implicit_export") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] !~ ("^(" arg ")$")) continue
            if (has(E_tag[i], "android:exported")) continue
            if (index(E_body[i], "<intent-filter") == 0) continue
            print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" brief(E_tag[i])
          }
        } else if (mode == "fileprovider_exported") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] != "provider") continue
            if (index(attr(E_tag[i], "android:name"), "FileProvider") == 0) continue
            if (attr(E_tag[i], "android:exported") == "true") print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" brief(E_tag[i])
          }
        } else if (mode == "backup_default") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] != "application") continue
            if (has(E_tag[i], "android:allowBackup")) continue
            if (has(E_tag[i], "android:dataExtractionRules") || has(E_tag[i], "android:fullBackupContent")) continue
            print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" "<application> has no android:allowBackup (defaults to true) and no backup rules"
          }
        } else if (mode == "custom_scheme") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] != "data") continue
            sch = attr(E_tag[i], "android:scheme")
            if (sch == "\001" || sch ~ /^(http|https)$/) continue
            print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" brief(E_tag[i])
          }
        } else if (mode == "applink_no_autoverify") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] != "intent-filter") continue
            if (attr(E_tag[i], "android:autoVerify") == "true") continue
            if (E_body[i] !~ /android:scheme[[:space:]]*=[[:space:]]*"https?"/) continue
            if (index(E_body[i], "android.intent.action.VIEW") == 0) continue
            print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" "http(s) VIEW intent-filter without android:autoVerify=\"true\""
          }
        } else if (mode == "permission_no_level") {
          for (i = 1; i <= n; i++) {
            if (E_name[i] != "permission") continue
            if (has(E_tag[i], "android:protectionLevel")) continue
            print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" brief(E_tag[i])
          }
        } else if (mode == "undeclared_permission") {
          for (i = 1; i <= n; i++) if (E_name[i] == "permission") declared[attr(E_tag[i], "android:name")] = 1
          for (i = 1; i <= n; i++) {
            if (E_name[i] == "permission" || E_name[i] == "uses-permission") continue
            split("android:permission android:readPermission android:writePermission", keys, " ")
            for (k = 1; k <= 3; k++) {
              v = attr(E_tag[i], keys[k])
              if (v == "\001" || v ~ /^(android\.|com\.android\.|com\.google\.|signature$|signatureOrSystem$|\$\{)/) continue
              if (!(v in declared)) print ENVIRON["BV_FILE"] "\t" E_line[i] "\t" keys[k] "=\"" v "\" is not declared by a <permission> in this manifest"
            }
          }
        }
      }' < "$f"
  done < "$1"
}
