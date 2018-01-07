package com.example.heegyeong.culture_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Heegyeong on 2017-10-31.
 */
public class BycleSearchActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        final String CoordX = intent.getStringExtra("CoordX");
        final String CoordY = intent.getStringExtra("CoordY");


        webView = (WebView) findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);

        webView.loadUrl("http://map.naver.com/?lat=" + CoordY + "&lng=" + CoordX);


    }
}
