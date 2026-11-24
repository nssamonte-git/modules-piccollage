package noel.libs.sample

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import noel.libs.logging.LogcatSink
import noel.libs.logging.Logger

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Logger.install(LogcatSink())
        Logger.i("Sample") { "MainActivity created" }
        setContentView(TextView(this).apply { text = "Android Libraries sample" })
    }
}
