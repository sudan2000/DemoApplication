package com.susu.baselibrary.speech.asr;

import android.speech.SpeechRecognizer;


public class ErrorTranslation {

    //    -1//语音权限问题
//    -2//录音异常
//    -3//语音太短或没有识别
//    -4//语音过长
//    -5//网络不可用
//    -6//语音识别正在被使用
//    -99//未知异常
    public static int ASR_ERROR_PERMISSION = -1;
    public static int ASR_ERROR_AUDIO = -2;
    public static int ASR_ERROR_NONE = -3;
    public static int ASR_ERROR_BOUND = -4;
    public static int ASR_ERROR_NETWORK = -5;
    public static int ASR_ERROR_BUSY = -6;
    public static int ASR_ERROR_UNKNOW = -99;

    public static int transformCode(int errorCode) {
        int code;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                code = ASR_ERROR_AUDIO;
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                code = ASR_ERROR_BOUND;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                code = ASR_ERROR_PERMISSION;
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
            case SpeechRecognizer.ERROR_NETWORK:
                code = ASR_ERROR_NETWORK;
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                code = ASR_ERROR_NONE;
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                code = ASR_ERROR_BUSY;
                break;

            default:
                code = ASR_ERROR_UNKNOW;
                break;
        }
        return code;
    }

    public static String transformMsg(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "录音异常";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "语音过长";
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "无录音权限";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
            case SpeechRecognizer.ERROR_NETWORK:
                message = "网络不可用";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "语音太短，没有匹配的识别结果";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "语音识别正在被使用";
                break;
            default:
                message = "未知错误" + errorCode;
                break;
        }
        return message;
    }

    public static String wakeupError(int errorCode) {
        String message = null;
        switch (errorCode) {
            case 1:
                message = "参数错误";
                break;
            case 2:
                message = "网络请求发生错误";
                break;
            case 3:
                message = "服务器数据解析错误";
                break;
            case 4:
                message = "网络不可用";
                break;
            default:
                message = "未知错误:" + errorCode;
                break;
        }
        return message;
    }
}
