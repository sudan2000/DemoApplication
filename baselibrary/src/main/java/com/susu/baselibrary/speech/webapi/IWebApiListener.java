package com.susu.baselibrary.speech.webapi;


public interface IWebApiListener {

    void onOpen();

    void onStart();

    /**
     * 在非UI线程，调用UI修改需转到UI线程
     */
    void onMiddleResult(String result);


    /**
     * 在非UI线程，调用UI修改需转到UI线程
     */
    void onFinalResult(String result);


    void onError(String errorMessage);


    void onClose();


    void onVolume(int volume);
}
