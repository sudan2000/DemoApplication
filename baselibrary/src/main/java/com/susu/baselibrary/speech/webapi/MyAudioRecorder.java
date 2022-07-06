package com.susu.baselibrary.speech.webapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.susu.baselibrary.speech.SpeechConstants;
import com.susu.baselibrary.utils.base.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author : sudan
 * Time : 2021/10/18
 * Description:
 */
@SuppressLint("methodCount")
public class MyAudioRecorder {


    public static final String TAG = "test---";

    public static final int DEFAULT_TIME = 60;
    public static final int MIN_TIME = 1;

    public static final int STATUS_IDLE = 0;
    public static final int STATUS_RECORDING = 1;

    private int mStatus = STATUS_IDLE;

    private Context mContext;
    private AudioRecord mAudioRecord;

    private String mFilePath = null;

    private RecordListener mRecordListener = null;


    //16K采集率
    private static final int SAMPLE_RATE_IN_HZ = 16000;
    //单声道
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    //16Bit
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    private int mBufferSizeInBytes;
    private int mSendStatus;
    int mVolume;


    public MyAudioRecorder(Context context) {
        mContext = context;
        createAudio();
    }


    private void createAudio() throws IllegalStateException {
        // 获得缓冲区字节大小
        mBufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_ENCODING);
//        LogUtils.d(TAG, "mBufferSizeInBytes:" + mBufferSizeInBytes);
        if (mBufferSizeInBytes <= 0) {
            throw new IllegalStateException("AudioRecord is not available " + mBufferSizeInBytes);
        }
        mAudioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_ENCODING, mBufferSizeInBytes);

        int state = mAudioRecord.getState();
//        LogUtils.d(TAG, "createAudio state:" + state + ", initialized:" + (state == AudioRecord.STATE_INITIALIZED));

    }

    public Context getContext() {
        return mContext;
    }

    public int getStatus() {
        return mStatus;
    }


    public void startRecord(final File file) {
        if (mAudioRecord == null) {
            throw new IllegalStateException("录音尚未初始化");
        }
        if (mStatus == STATUS_RECORDING) {
            throw new IllegalStateException("正在录音...");
        }
        LogUtils.d(TAG, "===startRecord===");
        mVolume = 0;
        mAudioRecord.startRecording();
        mStatus = STATUS_RECORDING;
        new Thread(new Runnable() {
            @Override
            public void run() {

                writeAudioDataToFile(file);
            }
        }).start();
        if (mRecordListener != null) {
            mRecordListener.onStart();
        }
    }

    private void writeAudioDataToFile(File file) {
        OutputStream bos = null;
        int totalSize = 0;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] audioData = new byte[mBufferSizeInBytes];
            mSendStatus = 0;
            while (mStatus == STATUS_RECORDING) {
                int readSize = mAudioRecord.read(audioData, 0, mBufferSizeInBytes);
                if (readSize >= 0) { //某个华为手机有问题
                    totalSize += readSize;
                    try {
                        bos.write(audioData, 0, readSize);

                        if (mRecordListener != null) {
                            mRecordListener.send(audioData, readSize);
                        }
                        if (mVolume != doubleCalculateVolume(audioData)) {
                            mVolume = doubleCalculateVolume(audioData);
                            if (mRecordListener != null) {
                                mRecordListener.onVolumeChange(mVolume);
                            }
                        }
                    } catch (IOException e) {
                        LogUtils.e(TAG, "writeAudioDataToFileError :", e);
                        if (mRecordListener != null) {
                            mRecordListener.sendLastFrame();
                        }
                    }
                } else {
                    LogUtils.w(TAG, "writeAudioDataToFile finished -- totalSize: " + totalSize);
                }
            }
            bos.flush();

        } catch (IOException e) {
            LogUtils.d(TAG, e.getMessage());
        } finally {
            if (bos != null) {
                try {
                    bos.close();// 关闭写入流
                    LogUtils.d(TAG, "finally: sendLastFrame");
                    if (mRecordListener != null) {
                        mRecordListener.sendLastFrame();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int doubleCalculateVolume(byte[] buffer) {
        double sumVolume = 0.0;
        double avgVolume = 0.0;
        double volume = 0.0;
        for (int i = 0; i < buffer.length; i += 2) {
            int v1 = buffer[i] & 0xFF;

            int v2 = buffer[i + 1] & 0xFF;

            int temp = v1 + (v2 << 8);// 小端

            if (temp >= 0x8000) {
                temp = 0xffff - temp;

            }
            sumVolume += Math.abs(temp);
        }
        avgVolume = sumVolume / buffer.length / 2;
        volume = Math.log10(1 + avgVolume) * 10;
        return (int) volume;
    }


    public void stopRecord() {
        stop();
        if (mStatus == STATUS_RECORDING) {
            if (mRecordListener != null) {
                mRecordListener.onCompleted(this, mFilePath);
                mRecordListener.sendLastFrame();
            }
        }
    }


    private void stop() {
        LogUtils.d(TAG, "===stopRecord===");
        if (mStatus == STATUS_RECORDING) {
            mStatus = STATUS_IDLE;
            mAudioRecord.stop();
        }
    }

    public void release() {
        LogUtils.d(TAG, "===releaseRecord===");
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    public interface RecordListener {
        void onCompleted(MyAudioRecorder audio, String filePath);

        void onError(MyAudioRecorder audio, Exception e);

        void onStart();

        void sendLastFrame();

        void send(byte[] buffer, int len);

        void onVolumeChange(int volume);
    }


    public void setRecordListener(RecordListener l) {
        mRecordListener = l;
    }

    public RecordListener getRecordListener() {
        return mRecordListener;
    }

    protected void dispatchError(Exception e) {
        if (mRecordListener != null) {
            mRecordListener.onError(this, e);
        }
    }

    public int getDefaultMaxRecordTime() {
        return DEFAULT_TIME;
    }


    public File getAudioFile() {
        return new File(SpeechConstants.FOLDER);
    }


}