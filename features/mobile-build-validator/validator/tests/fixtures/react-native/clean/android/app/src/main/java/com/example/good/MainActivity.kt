package com.example.good

class MainActivity : ReactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}
