# core-logging

A tiny logging facade. Nothing is logged until a sink is installed, and messages are lambdas that are only built when their level is enabled.

## Use

```kotlin
implementation("noel.libs:core-logging:0.2.0")
```

```kotlin
// Application.onCreate: log to Logcat in debug builds only.
if (BuildConfig.DEBUG) Logger.install(LogcatSink())

Logger.d("Sync") { "fetched ${items.size} items" }
Logger.e("Sync", error) { "sync failed" }
```

Add your own sink (a file, a crash reporter) by implementing `LogSink`. Never log credentials, tokens or personal data.
