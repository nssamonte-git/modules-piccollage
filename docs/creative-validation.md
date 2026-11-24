# Creative library validation

Validation performed on 2026-11-24. The source includes domain/JVM tests, Android instrumentation for GPU/CPU parity, source alpha, encoded stills, MP4 photo crossfade/audio, video trim/overlay, and sample launch. Run the commands below from the project root.

```sh
./gradlew validateThreats testDebugUnitTest :sample:assembleRelease --max-workers=2
./gradlew -p build-validator-plugin test --max-workers=2
./gradlew :sample:connectedDebugAndroidTest --max-workers=2
./gradlew publishToMavenLocal --max-workers=2
```

Use JDK 25 from Android Studio. Encoder, GPU and font behavior needs Android execution; passing a pure domain test does not establish device acceptance. Physical-device vendor behavior, live AI providers, cloud backups, purchase restoration, notifications and printer/share applications require host acceptance.

## Executed device checks

- API 26 ARM emulator: 8/8 PASS, rerun after the timeline muxer change.
- API 37 ARM emulator: 8/8 PASS.
- These execute real Android APIs: OpenGL versus CPU pixel parity, source-alpha preservation, PNG/JPEG decoding, corrupt generated-media rejection, H.264/AAC MP4 creation/playback, decoded audio onset within 50 ms, duration within one frame, trim/overlay, canceled-output cleanup and lifecycle-aware sample launch.
- The API 37 run found a runtime failure in its advertised H.264 decoder. One bounded retry with a platform software decoder recovered successfully. A duration assertion found 63 ms of encoder padding beyond a 3.5-second composition; the muxer now terminates every track on the composition clock without relaxing the tolerance.
- Visual inspection of the actual API 26 and API 37 sample screenshots passed. The retained [API 37 screenshot](images/creative-playground-api37.png) uses original generated media.

## Architecture and provenance

`verifyCreativeModules` passes for 25 registered artifacts, an acyclic dependency graph, no engine-to-Compose edge, no engine implementation coupling, and pure domain/presentation import boundaries. The canonical report-only application audit has discovery false positives for this project's existing `module(...)` helper and vendored Gradle-plugin gate; its actual domain and layer import checks pass.

No fresh decompilation or third-party implementation/assets were imported. Backend, physical-device and real provider behavior is not established by these checks.

## Final build and consumer results

| Check | Result |
|---|---|
| Required security + JVM + R8 release build | PASS |
| JVM unit tests | 48 PASS, zero failures/errors |
| Build-validator plugin regression tests | 4 PASS |
| Security findings | 0 critical/high/medium; 2 existing low warnings; zero blocking findings |
| API 26 instrumentation | 8 PASS |
| API 37 instrumentation | 8 PASS |
| 25-module graph/layer guard | PASS |
| Independent engine and Compose composite consumers | PASS |
| Independent engine and Compose Maven Local consumers | PASS |
| Maven engine runtime dependency report | No `androidx.compose` dependency |

The 0.2.0 artifacts were published to Maven Local for these checks. No remote publication was performed. Build logs and raw emulator diagnostics remain temporary; this record retains only sanitized outcomes.
