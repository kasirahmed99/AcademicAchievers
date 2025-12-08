package com.example.academicachievers

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView = findViewById<WebView>(R.id.webview)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()


        val path = intent.getStringExtra("path") ?: "/"


        val baseUrl = "http://172.20.10.2:5000"

        webView.loadUrl(baseUrl + path)
    }

    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.webview)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
