package com.susu.baselibrary.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.susu.baselibrary.R;

/**
 * Author : sudan
 * Time : 2021/8/18
 * Description:
 */
public class WebViewActivity extends Activity {

    private final String TAG = "WebViewActivity";

    public static final String INTENT_URL = "url";

    private WebView webView;


    public static void goWeb(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_webview);

        initView();
        String url = getIntent().getStringExtra(INTENT_URL);
        initHandler();

        webView.loadUrl(url);

    }

    private void initView() {
        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

//        WYLogUtils.i(TAG, "User Agent:" + webView.getSettings().getUserAgentString());
//        webView.getSettings().setUserAgentString("MWYBrowser");
//        WYLogUtils.i(TAG, "User Agent:" + webView.getSettings().getUserAgentString());

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

        });
    }

    private void initHandler() {
        webView.addJavascriptInterface(new AndroidHandler(webView, this), "wyAndroid");

    }


}
