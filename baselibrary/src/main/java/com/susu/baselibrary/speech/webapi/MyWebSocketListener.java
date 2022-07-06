package com.susu.baselibrary.speech.webapi;

import com.susu.baselibrary.utils.base.LogUtils;
import java.io.IOException;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Author : sudan
 * Time : 2021/10/17
 * Description:
 */
public class MyWebSocketListener extends WebSocketListener {

    private static final String TAG = "test---";
    //    private IWebApiListener mIWebApiListener;
    private InnerWebApiListener mInnerWebApiListener;

    public MyWebSocketListener(InnerWebApiListener innerWebApiListener) {
//        mIWebApiListener = webApiListener;
        mInnerWebApiListener = innerWebApiListener;
    }


    private WebApiResponseData.Decoder mDecoder = new WebApiResponseData.Decoder();


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        LogUtils.d(TAG, "webSocket: onOpen---------------------");

    }


    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        LogUtils.d(TAG, "webSocket: onMessage--text:" + text);

        WebApiUtils.handleResult(text, mInnerWebApiListener);
    }



    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        LogUtils.d(TAG, "webSocket: onFailure t:" + t.getMessage() + " response: " + response);
        String errorMsg = "";
        try {
            if (null != response) {
                int code = response.code();
                errorMsg = response.body() == null ? "" : response.body().string();
                LogUtils.d(TAG, "webSocket: onFailure code:" + code + "--- body:" + errorMsg);
                if (101 != code) {
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            LogUtils.e(TAG, e.getMessage());
        } finally {
            if (mInnerWebApiListener != null) {
                mInnerWebApiListener.onError(errorMsg);
            }
            webSocket.close(1000, "");
            if (mInnerWebApiListener != null) {
                mInnerWebApiListener.onClose();
            }
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        LogUtils.d(TAG, "webSocket: onClosed -------------------");
        if (mInnerWebApiListener != null) {
            mInnerWebApiListener.onClose();
        }
    }
}
