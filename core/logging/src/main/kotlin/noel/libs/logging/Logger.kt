package noel.libs.logging

/** Severity of a log message, lowest first. */
enum class LogLevel { VERBOSE, DEBUG, INFO, WARN, ERROR }

/** Where log messages go (Logcat, a file, a crash reporter...). */
fun interface LogSink {
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?)
}

/**
 * App-wide logger. Nothing is logged until a sink is installed, so release builds can simply
 * install none. Messages are lambdas and are only built when the level is enabled.
 *
 * ```
 * Logger.install(LogcatSink())          // e.g. in Application.onCreate, debug builds only
 * Logger.d("Sync") { "fetched ${items.size} items" }
 * ```
 *
 * Never log credentials, tokens or personal data: sinks such as Logcat are readable on the device.
 */
object Logger {
    @Volatile
    private var sinks: List<LogSink> = emptyList()

    /** Messages below this level are dropped without building them. */
    @Volatile
    var minLevel: LogLevel = LogLevel.DEBUG

    fun install(vararg sink: LogSink) = synchronized(this) { sinks = sinks + sink }

    fun uninstallAll() = synchronized(this) { sinks = emptyList() }

    fun isEnabled(level: LogLevel): Boolean = level >= minLevel && sinks.isNotEmpty()

    fun log(level: LogLevel, tag: String, throwable: Throwable? = null, message: () -> String) {
        if (!isEnabled(level)) return
        val text = message()
        sinks.forEach { it.log(level, tag, text, throwable) }
    }

    fun v(tag: String, message: () -> String) = log(LogLevel.VERBOSE, tag, null, message)
    fun d(tag: String, message: () -> String) = log(LogLevel.DEBUG, tag, null, message)
    fun i(tag: String, message: () -> String) = log(LogLevel.INFO, tag, null, message)
    fun w(tag: String, throwable: Throwable? = null, message: () -> String) = log(LogLevel.WARN, tag, throwable, message)
    fun e(tag: String, throwable: Throwable? = null, message: () -> String) = log(LogLevel.ERROR, tag, throwable, message)
}
