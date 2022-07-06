package com.susu.baselibrary.speech;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.speech.asr.IRecognizerListener;
import com.susu.baselibrary.speech.asr.SpeechRecognizer;
import com.susu.baselibrary.speech.transform.DecodeEngine;
import com.susu.baselibrary.speech.transform.DecodeOperateInterface;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.ThreadUtils;
import com.susu.baselibrary.utils.stream.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author : sudan
 * Time : 2021/11/3
 * Description:
 */
public class SpeechTestActivity extends Activity implements View.OnClickListener {
    private static String TAG = "test---";

    private SpeechRecognizer mSpeechRecognizer;
    private TextView mResultText;
    private TextView mMiddleText;
    private TextView mStatusText;
    private long startTime;
    private String fileName;
    private Context mContext;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_test_activity_speech_demo);

        findViewById(R.id.iat_recognize).setOnClickListener(this);
        findViewById(R.id.iat_recognize_stream).setOnClickListener(this);
        findViewById(R.id.iat_stop).setOnClickListener(this);
        findViewById(R.id.iat_cancel).setOnClickListener(this);

        mResultText = findViewById(R.id.iat_text);
        mMiddleText = findViewById(R.id.iat_text_middle);
        mStatusText = findViewById(R.id.status);

        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                fileName = FileUtils.copyAssetsFile(SpeechTestActivity.this, "test.amr");
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iat_recognize) {
            mResultText.setText("");
            mStatusText.setText("开始录音。。。");

        } else if (view.getId() == R.id.iat_recognize_stream) { // 音频流识别
            mResultText.setText("");
            mStatusText.setText("开始识别。。。");
            String rootSdpPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mSpeechRecognizer.startFile(rootSdpPath + "/DCIM/iattest.wav");

        } else if (view.getId() == R.id.iat_recognize_stream_example) { // 音频流识别实例
            mResultText.setText("");
            mStatusText.setText("开始识别。。。");
            startFile();

        } else if (view.getId() == R.id.iat_stop) {
            mStatusText.setText("停止录音。。。");
            mSpeechRecognizer.stop();


        } else if (view.getId() == R.id.iat_cancel) {
            cancel();
        }
    }

    private void initSpeechRecognizer() {
        if (mSpeechRecognizer == null) {
            mSpeechRecognizer = new SpeechRecognizer(this, new IRecognizerListener() {
                @Override
                public void onAsrReady() {

                }

                @Override
                public void onAsrBegin() {
                    LogUtils.d(TAG, "onAsrBegin");

                }

                @Override
                public void onAsrEnd() {

                }

                @Override
                public void onAsrPartialResult(String result) {
                    LogUtils.d(TAG, "onAsrPartialResult:result: " + result);
                    mMiddleText.setText(result);
                }

                @Override
                public void onAsrFinalResult(String result, byte[] data) {
                    LogUtils.d(TAG, "onAsrFinalResult:result: " + result);
                    mResultText.setText(result);
                    long end = System.currentTimeMillis();
                    LogUtils.d(TAG, "onAsrFinalResult time: " + (end - startTime));
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
            });
        }
    }

    private void start() {
        initSpeechRecognizer();
        mSpeechRecognizer.start();
    }


    private void startFile() {
        startTime = System.currentTimeMillis();
        initSpeechRecognizer();

        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //先将amr格式音频转换成pcm格式音频
                    DecodeEngine.getInstance().convertMusicFileToPcmFile(
                            fileName,
                            FileUtils.PATH_FOLDER + "test.pcm",
                            new DecodeOperateInterface() {
                                @Override
                                public void updateDecodeProgress(int decodeProgress) {

                                }

                                @Override
                                public void decodeSuccess() {
                                    startAfterTransform();
                                }

                                @Override
                                public void decodeFail() {

                                }
                            }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startAfterTransform(){
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                long end = System.currentTimeMillis();
                LogUtils.d(TAG, "decodeSuccess time: " + (end - startTime));
                mSpeechRecognizer.startFile(FileUtils.PATH_FOLDER + "test.pcm");
            }
        });
    }

    private void cancel() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
        }
    }


    private void reset() {
        mResultText.setText(null);// 清空显示内容
        mStatusText.setText("");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.release();
        }
    }


}