package com.susu.baselibrary.speech.asr;

/**
 * Created by fujiayi on 2017/6/14.
 * 识别监听
 */

public interface IRecognizerListener {

    /**
     * 输入事件调用后，引擎准备完毕
     */
    void onAsrReady();

    /**
     * onAsrReady后检查到用户开始说话
     */
    void onAsrBegin();

    /**
     * 检查到用户开始说话停止，或者ASR_STOP
     */
    void onAsrEnd();

    /**
     * 随着用户的说话，返回的临时结果
     *
     * @param result     临时结果
     */
    void onAsrPartialResult(String result);

    /**
     * 随着用户的说话，返回的最终结果
     *
     * @param result     最终结果
     * @param data 语音数据
     */
    void onAsrFinalResult(String result, byte[] data);

    /**
     * 语音识别结束，识别成功
     *
     */
    void onAsrFinish();

    /**
     * 语音识别结束，识别错误，返回错误码和错误信息
     * @param errorCode
     * @param errorMessage
     * @param descMessage
     */
    void onAsrFinishError(int errorCode, String errorMessage, String descMessage);

    /**
     * 识别结束，引擎退出
     */
    void onAsrExit();

    /**
     * 用户主动取消了识别
     */
    void onAsrCancel();

    /**
     * CALLBACK_EVENT_ASR_VOLUME
     * 音量回调
     *
     * @param volumePercent 音量的相对值，百分比，0-100
     * @param volume 音量绝对值
     */
    void onAsrVolume(int volumePercent, int volume);
}
