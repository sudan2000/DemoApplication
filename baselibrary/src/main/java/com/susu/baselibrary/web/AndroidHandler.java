package com.susu.baselibrary.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.StringUtils;

import org.json.JSONObject;

/**
 * Author : sudan
 * Time : 2021/8/18
 * Description:
 */
public class AndroidHandler {

    private static final String TAG = "AndroidHandler";
    private WebView mWebView;
    private Activity mActivity;

    public AndroidHandler(WebView webView, Activity activity) {
        mWebView = webView;
        mActivity = activity;
    }

    @JavascriptInterface
    public void testHandler(String data) {
        if (StringUtils.isNotNull(data)) {
            LogUtils.d(TAG, "testHandler：data" + data);
            try {


                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String callBack = "javascript:testHandlerCallback('success')";
                        mWebView.loadUrl(callBack);
                    }
                });


            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
    }
}
