package com.susu.baselibrary.speech.asr;

import android.os.Bundle;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.susu.baselibrary.utils.base.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Author : sudan
 * Time : 2021/11/3
 * Description:
 */
public class SSRecognizerListener implements RecognizerListener {

    private static final String TAG = "test---";
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    private IRecognizerListener mListener;
    private StringBuffer mResultBuffer;

    public SSRecognizerListener(IRecognizerListener listener) {
        this.mListener = listener;
    }


    @Override
    public void onVolumeChanged(int volume, byte[] data) {
        LogUtils.d(TAG, "WYRecognizerListener:onVolumeChanged--音量大小 = " + volume + " 返回音频数据 = " + data.length);
        if (mListener != null) {
            mListener.onAsrVolume(volume, volume);
        }
    }

    @Override
    public void onBeginOfSpeech() {
        // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        LogUtils.d(TAG, "WYRecognizerListener:onBeginOfSpeech--开始说话");
        mIatResults.clear();
        if (mListener != null) {
            mListener.onAsrBegin();
        }
    }

    @Override
    public void onEndOfSpeech() {
        LogUtils.d(TAG, "WYRecognizerListener:onEndOfSpeech--停止说话");
        if (mListener != null) {
            mListener.onAsrEnd();
        }

    }

    @Override
    public void onResult(RecognizerResult results, boolean isLast) {
        LogUtils.d(TAG, "WYRecognizerListener:onResult:" + results.getResultString());
        handleResults(results);
        if (isLast) {
            LogUtils.d(TAG, "WYRecognizerListener：onResult 结束");
            if (mListener != null) {
                mListener.onAsrFinalResult(mResultBuffer.toString(), null);
            }
        } else {
            if (mListener != null) {
                mListener.onAsrPartialResult(mResultBuffer.toString());
            }
        }
    }

    private void handleResults(RecognizerResult results) {
        String text = SpeechJsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            LogUtils.e(TAG, e.getMessage());
        }

        mIatResults.put(sn, text);

        mResultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            mResultBuffer.append(mIatResults.get(key));
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        int error = speechError.getErrorCode();
        //todo 转换错误码
        int errorCode = ErrorTranslation.transformCode(error);
        String errorMsg = ErrorTranslation.transformMsg(error);
        LogUtils.d(TAG, "WYRecognizerListener--onError--errorCode: " + errorCode + " errorMsg: " + errorMsg);
        if (mListener != null) {
            mListener.onAsrFinishError(errorCode, errorMsg, speechError.getErrorDescription());
        }
    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle bundle) {
        LogUtils.d(TAG, "WYRecognizerListener:onEvent--eventType:" + eventType + " arg1:" + arg1 + " arg2:" + arg2 + " bundle:" + bundle);
        // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        // 若使用本地能力，会话id为null
//        	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
        //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
        //		Log.d(TAG, "session id =" + sid);
        //	}
    }

}
