package com.susu.baselibrary.speech.asr;

import android.content.Context;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.susu.baselibrary.utils.base.LogUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;


public class SpeechRecognizer {

    private static final String TAG = "test---";

    private com.iflytek.cloud.SpeechRecognizer mSpeechRecognizer;

    private RecognizerListener mRecognizerListener;

    private static boolean isOfflineEngineLoaded = false;

    private static boolean isInited = false;


    private Context mContext;


    public SpeechRecognizer(Context context, IRecognizerListener recogListener) {
        this(context, new SSRecognizerListener(recogListener));
    }

    private SpeechRecognizer(Context context, RecognizerListener recognizerListener) {
        mContext = context;
        mRecognizerListener = recognizerListener;
        init();
    }

    private void init() {
        if (isInited) {
            LogUtils.e(TAG, "还未调用release()，请勿新建一个新类");
            throw new RuntimeException("还未调用release()，请勿新建一个新类");
        }
        LogUtils.d(TAG, "SpeechRecognizer初始化------------------");
        isInited = true;
        mSpeechRecognizer = com.iflytek.cloud.SpeechRecognizer.createRecognizer(mContext, mInitListener);
    }


    public void start() {
        setParam(); // 设置参数

        if(mSpeechRecognizer!=null){
            int ret = mSpeechRecognizer.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                LogUtils.d(TAG, "听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }

    }


    /**
     * 提前结束录音等待识别结果。
     */
    public void stop() {
        LogUtils.i(TAG, "停止录音");
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
        }
    }


    public void cancel() {
        LogUtils.d(TAG, "取消识别");
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
        }
    }

    public void startFile(String path) {
        executeStream(path);
    }


    /**
     * 执行音频流识别操作
     */
    private void executeStream(String path) {
        // 设置参数
        setParam();
        // 设置音频来源为外部文件
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
        // mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
        // mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");
        int ret = mSpeechRecognizer.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            LogUtils.d(TAG, "识别失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            return;
        }
        try {
            FileInputStream fs = new FileInputStream(path);
//            InputStream open = mContext.getAssets().open("iattest.wav");
            byte[] buff = new byte[1280];
            while (fs.available() > 0) {
                int read = fs.read(buff);
                mSpeechRecognizer.writeAudio(buff, 0, read);
            }
            mSpeechRecognizer.stopListening();
        } catch (IOException e) {
            mSpeechRecognizer.cancel();
            LogUtils.d(TAG, "读取音频流失败");
        }
    }

    public void setParam() {
        // 清空参数
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "5000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "5000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                mContext.getExternalFilesDir("msc").getAbsolutePath() + "/iat.wav");
    }


    public void release() {
        if (mSpeechRecognizer == null) {
            return;
        }
        mSpeechRecognizer.cancel();
        mSpeechRecognizer.destroy();
        mSpeechRecognizer = null;
        isInited = false;
    }


    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtils.d(TAG, "com.iflytek.cloud.SpeechRecognizer onInit() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                LogUtils.d(TAG, "初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };


    public void loadOfflineEngine(Map<String, Object> params) {
    }

}
