package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.help_activity.*

class HelpActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_activity)

        progressBar = findViewById(R.id.progressbar)
        progressBar.max = 100

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, newProgress: Int){
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressbar.visibility - View.VISIBLE
                    progressbar.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                supportActionBar?.subtitle = title
            }


        }

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://wildlifewarriors.org.au/conservation-projects/koala-conservation")
    }

    override fun onBackPressed() {

        if(webView.canGoBack()) {
            webView.goBack()
        } else{
            super.onBackPressed()
        }
    }

}
