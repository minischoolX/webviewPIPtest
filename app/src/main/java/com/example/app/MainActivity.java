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

                injectJavaScript();
                
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

        private void injectJavaScript() {
        String javascriptCode = "(function() {\n" +
                "// Add a button to the page\r\nvar revealButton = document.createElement(\"button\");\r\nrevealButton.innerHTML = \"Show Page Source\";\r\nrevealButton.style.position = \"fixed\";\r\nrevealButton.style.top = \"20px\";\r\nrevealButton.style.left = \"20px\";\r\nrevealButton.style.padding = \"10px\";\r\nrevealButton.style.backgroundColor = \"#3498db\";\r\nrevealButton.style.color = \"#fff\";\r\nrevealButton.style.border = \"none\";\r\nrevealButton.style.borderRadius = \"5px\";\r\ndocument.body.appendChild(revealButton);\r\n\r\n// Add an event listener to the button\r\nrevealButton.addEventListener(\"click\", function() {\r\n  // Create a new window or iframe to display the source code\r\n  var sourceWindow = window.open(\"\", \"_blank\");\r\n  sourceWindow.document.write(\"<pre><code>\" + escapeHtml(document.documentElement.outerHTML) + \"</code></pre>\");\r\n\r\n  // Add a close button\r\n  var closeButton = sourceWindow.document.createElement(\"button\");\r\n  closeButton.innerHTML = \"Close\";\r\n  closeButton.style.padding = \"10px\";\r\n  closeButton.style.backgroundColor = \"#e74c3c\";\r\n  closeButton.style.color = \"#fff\";\r\n  closeButton.style.border = \"none\";\r\n  closeButton.style.borderRadius = \"5px\";\r\n  sourceWindow.document.body.appendChild(closeButton);\r\n\r\n  // Add an event listener to the close button\r\n  closeButton.addEventListener(\"click\", function() {\r\n    sourceWindow.close();\r\n  });\r\n\r\n  // Add a search input box to the source code container\r\n  var searchInput = sourceWindow.document.createElement(\"input\");\r\n  searchInput.type = \"text\";\r\n  searchInput.placeholder = \"Search\";\r\n  searchInput.style.padding = \"10px\";\r\n  searchInput.style.marginTop = \"20px\";\r\n  searchInput.style.border = \"1px solid #ccc\";\r\n  sourceWindow.document.body.appendChild(searchInput);\r\n\r\n  // Add a container for match count information\r\n  var matchCount = sourceWindow.document.createElement(\"div\");\r\n  matchCount.id = \"match-count\";\r\n  matchCount.style.marginTop = \"10px\";\r\n  sourceWindow.document.body.appendChild(matchCount);\r\n\r\n  // CSS styles for highlighting search term\r\n  var styles = sourceWindow.document.createElement(\"style\");\r\n  styles.innerHTML = \".highlight { background-color: yellow; }\";\r\n  sourceWindow.document.head.appendChild(styles);\r\n});\r\n\r\n// Function to escape HTML special characters\r\nfunction escapeHtml(unsafe) {\r\n  return unsafe.replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");\r\n}\r\n" +
                "})();";

        webView.evaluateJavascript(javascriptCode, null);
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
