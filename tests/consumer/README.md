# Independent consumer fixtures

These Android library fixtures compile production engine APIs and the optional Compose API independently of the sample. Versions come from the library catalog. Maven coordinates are the actual 0.2.1 artifacts.

From the library root:

```sh
./gradlew -p tests/consumer -PcreativeLibrarySource="$PWD" assembleDebug --max-workers=2
./gradlew publishToMavenLocal --max-workers=2
./gradlew -p tests/consumer assembleDebug --max-workers=2
./gradlew -p tests/consumer :engine-only:dependencies --configuration debugRuntimeClasspath
```

The last report must contain no `androidx.compose` dependency. The first command uses composite substitution; the third uses Maven Local and published POM metadata. The fixtures are not published.
