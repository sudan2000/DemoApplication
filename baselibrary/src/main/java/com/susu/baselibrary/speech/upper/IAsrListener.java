package com.susu.baselibrary.speech.upper;

public interface IAsrListener {

    void onFinish(String text);

    void onError(int errorCode, String errorMessage, String descMessage);

    void onPartial(String text);
}
