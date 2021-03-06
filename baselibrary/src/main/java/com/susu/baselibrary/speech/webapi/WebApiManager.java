package com.susu.baselibrary.speech.webapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonObject;
import com.susu.baselibrary.speech.SpeechConstants;
import com.susu.baselibrary.utils.base.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Author : sudan
 * Time : 2021/10/15
 * Description:
 */
@SuppressLint("methodCount")
public class WebApiManager {

    private static final String TAG = "test---";

    public static final int STATUS_FIRST_FRAME = 0;
    public static final int STATUS_CONTINUE_FRAME = 1;
    public static final int STATUS_LAST_FRAME = 2;
    private Context mContext;
    private File mAudioFile;
    private OkHttpClient mClient;
    private Request mRequest;
    private WebSocket mWebSocket;

    private int mStatus;
    private MyAudioRecorder mAudio;
    private MyAudioRecorder.RecordListener mRecordListener;
    private IWebApiListener mIWebApiListener;
    private MyWebSocketListener mListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    public WebApiManager(Context context, final IWebApiListener webAPiListener) {
        mContext = context;
        mIWebApiListener = webAPiListener;
        mListener = new MyWebSocketListener(new InnerWebApiListener() {
            @Override
            public void onOpen() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        makeAudioFile();
                        mAudio.startRecord(mAudioFile);
                        if (mIWebApiListener != null) {
                            mIWebApiListener.onOpen();
                        }
                    }
                });
            }

            @Override
            public void onStart() {
                if (mIWebApiListener != null) {
                    mIWebApiListener.onStart();
                }
            }

            @Override
            public void onClose() { //????????????????????????websocket
                if (mIWebApiListener != null) {
                    mIWebApiListener.onClose();
                }
                mWebSocket.close(1000,"");
            }

            @Override
            public void onError(String errorMsg) {
                if (mIWebApiListener != null) {
                    mIWebApiListener.onError(errorMsg);
                }
            }

            @Override
            public void onMiddleResult(String result) {
//                if (mIWebApiListener != null) {
//                    mIWebApiListener.onMiddleResult(result);
//                }
            }

            @Override
            public void onFinalResult(String result) {
                if (mIWebApiListener != null) {
                    mIWebApiListener.onFinalResult(result);
                }
            }
        });
        buildClient();
    }

    private void buildClient() {
        // ????????????url
        mClient = new OkHttpClient.Builder().build();

    }

    private void initWebSocket() {
        String authUrl = WebApiUtils.getAuthUrl(SpeechConstants.HOST_URL, SpeechConstants.API_KEY, SpeechConstants.API_SECRET);
        //???url?????? schema http://???https://???????????????ws:// ??? wss://
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        mRequest = new Request.Builder().url(url).build();
        mWebSocket = mClient.newWebSocket(mRequest, mListener);
    }


    private void initWebSocketNew() {
        String authUrl = WebApiUtils.getUrl(SpeechConstants.APP_ID, SpeechConstants.API_KEY_WEB);
        mRequest = new Request.Builder().url(authUrl).build();
        mWebSocket = mClient.newWebSocket(mRequest, mListener);
    }

    //????????????
    public void startTransport() {
//        initWebSocket();
        initWebSocketNew();
        initAudio();
    }

    private void makeAudioFile() {
        File path = new File(SpeechConstants.FOLDER);
        if (!path.exists()) {
            path.mkdirs();
        }
        mAudioFile = new File(path, UUID.randomUUID() + ".pcm");
        if (mAudioFile.exists()) {
            mAudioFile.delete();
        }
        try {
            mAudioFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopTransport() {
        mAudio.stopRecord();

    }

    public void release() {
        if (mWebSocket != null) {
            mWebSocket.close(1000, "");
        }
        if (mAudio != null) {
            mAudio.release();
        }
    }

    public void cancel() {
        mWebSocket.cancel();
    }


    private void initAudio() {
        if (mAudio == null) {
            mAudio = new MyAudioRecorder(mContext);
            mRecordListener = new MyAudioRecorder.RecordListener() {

                @Override
                public void onError(MyAudioRecorder audio, Exception e) {

                }

                @Override
                public void onStart() {
                    LogUtils.d(TAG, "record---onStart---filePath:" + mAudioFile.getAbsolutePath());
                }


                @Override
                public void sendLastFrame() {
                    sendEnd();
                }

                @Override
                public void send(byte[] buffer, int len) {
//                    LogUtils.d(TAG, "send:");
                    sendData(buffer, len);
                }

                @Override
                public void onVolumeChange(int volume) {
                    //todo
                    if (mIWebApiListener != null) {
                        mIWebApiListener.onVolume(volume);
                    }
                }

                @Override
                public void onCompleted(MyAudioRecorder audio, String filePath) {
                    LogUtils.d(TAG, "record---onCompleted--------");

                }
            };
            mAudio.setRecordListener(mRecordListener);
        }
    }

    private void sendData(byte[] bytes, int len) {
        byte[] data = Arrays.copyOfRange(bytes, 0, len);
        mWebSocket.send(ByteString.of(bytes));
    }

    private void sendEnd() {
        mWebSocket.send("{\"end\": true}");
    }

    private void handleLastFrame() {
        JsonObject frame2 = new JsonObject();
        JsonObject data2 = new JsonObject();
        data2.addProperty("status", STATUS_LAST_FRAME);
        data2.addProperty("audio", "");
        data2.addProperty("format", "audio/L16;rate=16000");
        data2.addProperty("encoding", "raw");
        frame2.add("data", data2);
        mWebSocket.send(frame2.toString());

//        LogUtils.d(TAG, "handleLastFrame");
    }

    private void handleContinueFrame(byte[] buffer, int len) {
        JsonObject frame1 = new JsonObject();
        JsonObject data1 = new JsonObject();
        data1.addProperty("status", STATUS_CONTINUE_FRAME);
        data1.addProperty("format", "audio/L16;rate=16000");
        data1.addProperty("encoding", "raw");
        data1.addProperty("audio", WebApiUtils.getEncodeString(Arrays.copyOf(buffer, len)));
        frame1.add("data", data1);
        mWebSocket.send(frame1.toString());
//        LogUtils.d(TAG, "handleContinueFrame");
    }


    private void handleFirstFrame(byte[] buffer, int len) {
        JsonObject frame = new JsonObject();
        JsonObject business = new JsonObject();  //?????????????????????
        JsonObject common = new JsonObject();  //?????????????????????
        JsonObject data = new JsonObject();  //?????????????????????
        // ??????common
        common.addProperty("app_id", SpeechConstants.APP_ID);
        //??????business
        business.addProperty("language", "zh_cn");
        business.addProperty("language", "en_us");//??????
        //business.addProperty("language", "ja_jp");//?????????????????????????????????????????????
        //business.addProperty("language", "ko_kr");//?????????????????????????????????????????????
        //business.addProperty("language", "ru-ru");//?????????????????????????????????????????????
        business.addProperty("domain", "iat");
        business.addProperty("accent", "mandarin");//???????????????????????????????????????????????????????????????????????????
        //business.addProperty("nunum", 0);
//        business.addProperty("ptt", 0);//????????????
        //business.addProperty("rlang", "zh-hk"); // zh-cn :???????????????????????????zh-hk :????????????(???????????????????????????????????????????????????)
        //business.addProperty("vinfo", 1);
        business.addProperty("dwa", "wpgs");//????????????(???????????????????????????????????????????????????)
        //business.addProperty("nbest", 5);// ???????????????(???????????????????????????????????????????????????)
        //business.addProperty("wbest", 3);// ???????????????(???????????????????????????????????????????????????)
        //??????data
        data.addProperty("status", STATUS_FIRST_FRAME);
        data.addProperty("format", "audio/L16;rate=16000");
        data.addProperty("encoding", "raw");

        data.addProperty("audio", WebApiUtils.getEncodeString(Arrays.copyOf(buffer, len)));
        //??????frame
        frame.add("common", common);
        frame.add("business", business);
        frame.add("data", data);
        mWebSocket.send(frame.toString());
        mStatus = STATUS_CONTINUE_FRAME;  // ????????????????????????status ??? 1
//        LogUtils.d(TAG, "handleFirstFrame");
    }


}
