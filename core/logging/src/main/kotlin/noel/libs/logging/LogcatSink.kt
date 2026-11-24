package noel.libs.logging

import android.util.Log

/** Sends messages to Logcat. Install it in debug builds only. */
class LogcatSink : LogSink {
    override fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val priority = when (level) {
            LogLevel.VERBOSE -> Log.VERBOSE
            LogLevel.DEBUG -> Log.DEBUG
            LogLevel.INFO -> Log.INFO
            LogLevel.WARN -> Log.WARN
            LogLevel.ERROR -> Log.ERROR
        }
        val text = if (throwable == null) message else message + "\n" + Log.getStackTraceString(throwable)
        Log.println(priority, tag, text)
    }
}
