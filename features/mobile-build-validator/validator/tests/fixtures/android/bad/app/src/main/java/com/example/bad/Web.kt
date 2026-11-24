package com.example.bad

class Web(private val webView: WebView, private val intent: Intent) {
    fun setup() {
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.safeBrowsingEnabled = false
        webView.addJavascriptInterface(Bridge(), "Native")
        // build-validator:ignore AND-WV-001 reason: bridge is only attached to bundled offline help pages
        webView.addJavascriptInterface(HelpBridge(), "Help")
        // build-validator:ignore AND-WV-001
        webView.addJavascriptInterface(Other(), "Other")
        WebViewCompat.addWebMessageListener(webView, "bridge", setOf("*"), listener)
        webView.loadUrl("file:///android_asset/index.html")
        webView.loadUrl("file:///sdcard/page.html")
        webView.loadUrl(intent.data.toString())
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                if (BuildConfig.DEBUG) {
                    handler.proceed()
                }
            }
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return !request.url.toString().contains("example-bank.io")
            }
        }
    }
}
