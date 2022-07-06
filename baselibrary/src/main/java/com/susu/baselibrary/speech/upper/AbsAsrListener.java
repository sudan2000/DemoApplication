package com.susu.baselibrary.speech.upper;

/**
 * Author : sudan
 * Time : 2021/10/19
 * Description: 只想部分回调，使用该类
 */
public class AbsAsrListener implements IAsrListener {

    @Override
    public void onFinish(String text) {

    }

    @Override
    public void onError(int errorCode, String errorMessage, String descMessage) {

    }

    @Override
    public void onPartial(String text) {

    }
}
