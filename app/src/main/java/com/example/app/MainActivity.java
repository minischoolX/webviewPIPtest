package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.activity_main_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Inject JavaScript code into the main page
                String mainPageJsCode = "var videos = document.getElementsByTagName('video');" +
                        "for (var i = 0; i < videos.length; i++) {" +
                        "    videos[i].setAttribute('webkit-playsinline', 'true');" +
                        "    videos[i].setAttribute('playsinline', 'true');" +
                        "    videos[i].setAttribute('x-webkit-airplay', 'allow');" +
                        "    videos[i].setAttribute('x5-video-player-type', 'h5');" +
                        "    videos[i].setAttribute('x5-video-player-fullscreen', 'true');" +
                        "}";

                // Inject JavaScript code into the iframe's content
                String iframeJsCode = "var iframes = document.getElementsByTagName('iframe');" +
                        "for (var i = 0; i < iframes.length; i++) {" +
                        "    var iframe = iframes[i];" +
                        "    var iframeDoc = iframe.contentDocument || iframe.contentWindow.document;" +
                        "    var videos = iframeDoc.getElementsByTagName('video');" +
                        "    for (var j = 0; j < videos.length; j++) {" +
                        "        videos[j].setAttribute('webkit-playsinline', 'true');" +
                        "        videos[j].setAttribute('playsinline', 'true');" +
                        "        videos[j].setAttribute('x-webkit-airplay', 'allow');" +
                        "        videos[j].setAttribute('x5-video-player-type', 'h5');" +
                        "        videos[j].setAttribute('x5-video-player-fullscreen', 'true');" +
                        "    }" +
                        "}";

                // Execute JavaScript code in the iframe after a slight delay to ensure its content is loaded
                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:(function() { " + iframeJsCode + " })()");
                    }
                }, 500);

                // Create a MutationObserver instance to observe mutations in the document
                String mutationObserverJsCode = "var observer = new MutationObserver(function(mutationsList, observer) {" +
                        "    for (var i = 0; i < mutationsList.length; i++) {" +
                        "        var mutation = mutationsList[i];" +
                        "        if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {" +
                        "            for (var j = 0; j < mutation.addedNodes.length; j++) {" +
                        "                var node = mutation.addedNodes[j];" +
                        "                if (node.tagName && node.tagName.toLowerCase() === 'video') {" +
                        "                    node.setAttribute('webkit-playsinline', 'true');" +
                        "                    node.setAttribute('playsinline', 'true');" +
                        "                    node.setAttribute('x-webkit-airplay', 'allow');" +
                        "                    node.setAttribute('x5-video-player-type', 'h5');" +
                        "                    node.setAttribute('x5-video-player-fullscreen', 'true');" +
                        "                }" +
                        "            }" +
                        "        }" +
                        "    }" +
                        "});" +
                        "observer.observe(document, { childList: true, subtree: true });";

                webView.loadUrl("javascript:(function() { " + mainPageJsCode + " })()");
                webView.loadUrl("javascript:(function() { " + mutationObserverJsCode + " })()");
            }
        });

        webView.loadUrl("https://google.com");
        // REMOTE RESOURCE
        // mWebView.loadUrl("https://example.com");

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
