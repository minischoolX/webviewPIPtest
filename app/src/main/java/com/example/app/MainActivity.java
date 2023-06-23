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
                "// Add a button to the page\r\nvar revealButton = document.createElement(\"button\");\r\nrevealButton.innerHTML = \"Show Page Source\";\r\ndocument.body.appendChild(revealButton);\r\n\r\n// Create an overlay to display the source code\r\nvar overlay = document.createElement(\"div\");\r\noverlay.style.display = \"none\";\r\noverlay.style.position = \"fixed\";\r\noverlay.style.top = \"0\";\r\noverlay.style.left = \"0\";\r\noverlay.style.width = \"100%\";\r\noverlay.style.height = \"100%\";\r\noverlay.style.backgroundColor = \"rgba(0, 0, 0, 0.8)\";\r\noverlay.style.color = \"#fff\";\r\noverlay.style.zIndex = \"9999\";\r\noverlay.style.overflow = \"auto\";\r\noverlay.style.padding = \"20px\";\r\n\r\n// Add a close button to the overlay\r\nvar closeButton = document.createElement(\"button\");\r\ncloseButton.innerHTML = \"Close\";\r\ncloseButton.style.position = \"fixed\";\r\ncloseButton.style.top = \"20px\";\r\ncloseButton.style.right = \"20px\";\r\ncloseButton.style.backgroundColor = \"#fff\";\r\ncloseButton.style.border = \"none\";\r\ncloseButton.style.padding = \"8px\";\r\ncloseButton.style.cursor = \"pointer\";\r\n\r\n// Add a search input box to the overlay\r\nvar searchInput = document.createElement(\"input\");\r\nsearchInput.type = \"text\";\r\nsearchInput.placeholder = \"Search\";\r\nsearchInput.style.marginBottom = \"10px\";\r\nsearchInput.style.padding = \"8px\";\r\n\r\n// Add a container for match count information\r\nvar matchCount = document.createElement(\"div\");\r\nmatchCount.id = \"match-count\";\r\n\r\n// Append elements to the overlay\r\noverlay.appendChild(closeButton);\r\noverlay.appendChild(searchInput);\r\noverlay.appendChild(matchCount);\r\ndocument.body.appendChild(overlay);\r\n\r\n// Add an event listener to the button\r\nrevealButton.addEventListener(\"click\", function() {\r\n  // Display the overlay\r\n  overlay.style.display = \"block\";\r\n\r\n  // Populate the overlay with the source code\r\n  var sourceCode = document.createElement(\"pre\");\r\n  sourceCode.innerHTML = escapeHtml(document.documentElement.outerHTML);\r\n  overlay.appendChild(sourceCode);\r\n\r\n  // Add an event listener to the close button\r\n  closeButton.addEventListener(\"click\", function() {\r\n    // Clear the overlay and hide it\r\n    overlay.innerHTML = \"\";\r\n    overlay.style.display = \"none\";\r\n  });\r\n\r\n  // Add an event listener to the search input\r\n  searchInput.addEventListener(\"input\", function() {\r\n    var searchTerm = searchInput.value;\r\n    var matches = sourceCode.innerHTML.match(new RegExp(searchTerm, \"g\"));\r\n\r\n    // Highlight the search term occurrences\r\n    sourceCode.innerHTML = sourceCode.innerHTML.replace(new RegExp(searchTerm, \"g\"), \"<span class=\'highlight\'>\" + searchTerm + \"</span>\");\r\n    sourceCode.scrollTop = 0;\r\n\r\n    // Display the number of matches\r\n    if (matches) {\r\n      matchCount.innerHTML = matches.length + \" matches found\";\r\n    } else {\r\n      matchCount.innerHTML = \"No matches found\";\r\n    }\r\n  });\r\n\r\n  // CSS styles for overlay and highlighting search term\r\n  overlay.style.fontFamily = \"monospace\";\r\n  overlay.style.fontSize = \"14px\";\r\n  overlay.style.lineHeight = \"1.5\";\r\n  closeButton.style.color = \"#000\";\r\n  closeButton.style.fontWeight = \"bold\";\r\n  closeButton.style.fontSize = \"16px\";\r\n  closeButton.style.borderRadius = \"4px\";\r\n  searchInput.style.width = \"100%\";\r\n  searchInput.style.marginBottom = \"10px\";\r\n  searchInput.style.border = \"none\";\r\n  searchInput.style.padding = \"8px\";\r\n  searchInput.style.fontSize = \"14px\";\r\n  searchInput.style.borderRadius = \"4px\";\r\n  matchCount.style.marginBottom = \"10px\";\r\n  matchCount.style.fontSize = \"14px\";\r\n  matchCount.style.fontStyle = \"italic\";\r\n});\r\n\r\n// Function to escape HTML special characters\r\nfunction escapeHtml(unsafe) {\r\n  return unsafe.replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");\r\n}\r\n" +
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
