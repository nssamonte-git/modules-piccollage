# core-logging

A tiny logging facade for Android apps. You install one or more sinks (Logcat, a file, a crash reporter), then log through `Logger`. Nothing is logged until a sink is installed, and messages are lambdas that are only built when their level is enabled, so logging costs almost nothing in builds that install no sink. Plain Kotlin with one Android sink; no Compose.

## When to use it

- You want one logging call site for the whole app, with the output decided at startup (Logcat in debug, nothing or a crash reporter in release).
- You want log messages that are not built at all when logging is off.
- Not for analytics or structured telemetry: it passes a level, a tag, a message and an optional `Throwable`, nothing else.

## Add it

```kotlin
implementation("noel.libs:core-logging:0.2.1")
```

It has no `api` dependencies.

## Quick start

```kotlin
import noel.libs.logging.LogcatSink
import noel.libs.logging.Logger

// Application.onCreate (or your first Activity): log to Logcat in debug builds only.
if (BuildConfig.DEBUG) Logger.install(LogcatSink())

Logger.d("Sync") { "fetched ${items.size} items" }
Logger.e("Sync", error) { "sync failed" }
```

Your own sink:

```kotlin
import noel.libs.logging.LogLevel
import noel.libs.logging.LogSink
import noel.libs.logging.Logger

Logger.install(LogSink { level, tag, message, throwable ->
    if (level >= LogLevel.WARN) crashReporter.log("$tag: $message", throwable)
})
```

## Main types

| Type | What it does |
|---|---|
| `Logger` | App-wide object. `install(vararg sink)`, `uninstallAll()`, `minLevel`, `isEnabled(level)`, `log(...)`, and the shortcuts `v`, `d`, `i`, `w`, `e`. |
| `LogSink` | `fun interface` that receives `(level, tag, message, throwable)`. Implement it to send logs anywhere. |
| `LogcatSink` | Sends messages to Logcat with `android.util.Log.println`, appending the stack trace when there is a `Throwable`. |
| `LogLevel` | `VERBOSE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, lowest first. |

## What your app provides

- Decide which sinks to install and when, usually once at startup. `install` adds to the current sinks; it does not replace them.
- Keep credentials, tokens and personal data out of messages. Logcat is readable on the device, and the build validator flags credentials in logs.
- Sinks are called on the thread that logs. A slow sink (file, network) should hand the work to its own thread.

## Limits and behaviour

- `minLevel` defaults to `DEBUG`. Messages below it, or logged with no sink installed, are dropped without calling the message lambda.
- `install` and `uninstallAll` are synchronized; logging reads a snapshot of the sink list, so it is safe from any thread.
- No tag length handling, formatting, or buffering. A sink gets the text exactly as built.

## Tests

`LoggerTest` checks that nothing is built or sent without a sink, that every installed sink receives each message, and that messages below `minLevel` are dropped without being built.

```bash
./gradlew :core-logging:testDebugUnitTest
```

## See also

- [Library README](../../README.md) for how apps add the library.
- The sample app installs `LogcatSink` in debug builds: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt`.
