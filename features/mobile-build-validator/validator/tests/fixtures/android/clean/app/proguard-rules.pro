# Narrow keep rules that must not fire AND-RE-005 / AND-RE-006.
-keepclasseswithmembers class * { native <methods>; }
-keep class * extends android.app.Activity
-keep,allowobfuscation class com.example.good.** { *; }
-keepattributes Signature
