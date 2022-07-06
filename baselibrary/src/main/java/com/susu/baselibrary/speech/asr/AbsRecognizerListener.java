package com.susu.baselibrary.speech.asr;


public class AbsRecognizerListener implements IRecognizerListener {
    @Override
    public void onAsrReady() {

    }

    @Override
    public void onAsrBegin() {

    }

    @Override
    public void onAsrEnd() {

    }

    @Override
    public void onAsrPartialResult(String result) {

    }

    @Override
    public void onAsrFinalResult(String result, byte[] data) {

    }

    @Override
    public void onAsrFinish() {

    }

    @Override
    public void onAsrFinishError(int errorCode, String errorMessage, String descMessage) {

    }

    @Override
    public void onAsrExit() {

    }

    @Override
    public void onAsrCancel() {

    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {

    }
}
