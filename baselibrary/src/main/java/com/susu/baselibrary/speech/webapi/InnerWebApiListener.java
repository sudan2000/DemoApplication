package com.susu.baselibrary.speech.webapi;

/**
 * Author : sudan
 * Time : 2021/10/20
 * Description:
 */
public interface InnerWebApiListener {

    void onOpen(); //websocket open

    void onStart(); //开始识别

    void onClose();

    void onError(String errorMsg);

    /**
     * 在非UI线程，调用UI修改需转到UI线程
     */
    void onMiddleResult(String result);


    /**
     * 在非UI线程，调用UI修改需转到UI线程
     */
    void onFinalResult(String result);



}
