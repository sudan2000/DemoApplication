package com.susu.baselibrary.speech.upper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.susu.baselibrary.R;
import com.susu.baselibrary.speech.asr.AbsRecognizerListener;
import com.susu.baselibrary.speech.asr.SpeechRecognizer;
import com.susu.baselibrary.speech.webapi.AbsWebApiListener;
import com.susu.baselibrary.speech.webapi.WebApiManager;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.PermissionUtils;
import com.susu.baselibrary.utils.base.ThreadUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;

import java.util.List;

/**
 * 放在上层，不同app中可以自定义dialog样式，底层替换lib也方便
 * 语音识别dialog
 * 使用方式:new AsrManger(xx,listener).startAsr
 * 为防止内存泄漏，关闭页面时候请调用destroy
 */
@SuppressLint("methodCount")
public class AsrManager {

    private SpeechRecognizer mRecognizer = null;
    private ASRDialogView mView;
    private Context mContext;
    private IAsrListener mAsrListener;
    //问诊类型，打点用
    private int mConsultType;
    private WebApiManager mWebApiManager;
    private Handler mHandler = new Handler();

    public AsrManager(Context context, IAsrListener asrListener) {
        mContext = context;
        mAsrListener = asrListener;
    }

    public void setConsultType(int consultType) {
        mConsultType = consultType;
    }

    private void initView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.BaseCustomDialog);
        final Dialog mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.base_dialog_asr_manger);
        mView = window.findViewById(R.id.asr_dialog_layout_view);
        TextView okBtn = window.findViewById(R.id.dialog_asr_ok);
        TextView cancelBtn = window.findViewById(R.id.dialog_asr_cancle);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                mDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancel();
                mDialog.dismiss();
            }
        });
    }


    /**
     * 语音转文字(实时语音转文字)
     */
    public void startAsr() {
        PermissionUtils.requestPermission(mContext,
                new PermissionUtils.PermissionCallbacks() {
                    @Override
                    public void onPermissionsGranted(List<String> perms) {
                        startWebApi();
                    }

                    @Override
                    public void onPermissionsDenied(List<String> perms) {

                    }

                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                    }
                },
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 语音转文字（语音文件）
     */
    public void startAsrForFile(final String filepath) {
        PermissionUtils.requestPermission(mContext,
                new PermissionUtils.PermissionCallbacks() {
                    @Override
                    public void onPermissionsGranted(List<String> perms) {
                        startForFile(filepath);
                    }

                    @Override
                    public void onPermissionsDenied(List<String> perms) {

                    }

                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                    }
                },
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    private void startForFile(String path) {
        if (mRecognizer == null) {
            mRecognizer = new SpeechRecognizer(mContext, new AbsRecognizerListener() {
                @Override
                public void onAsrReady() {
                }

                @Override
                public void onAsrBegin() {
                }

                @Override
                public void onAsrFinalResult(String result, byte[] data) {
                    if (null != mAsrListener) {
                        mAsrListener.onFinish(result);
                    }
                }

                @Override
                public void onAsrFinishError(int errorCode, String errorMessage, String descMessage) {
                    super.onAsrFinishError(errorCode, errorMessage, descMessage);
                    if (null != mAsrListener) {
                        mAsrListener.onError(errorCode, errorMessage, descMessage);
                    }
                }
            });
        }
        cancel();
        mRecognizer.startFile(path);
    }

    private void startWebApi() {
        initView();
        if (mWebApiManager == null) {
            mWebApiManager = new WebApiManager(mContext, new AbsWebApiListener() {
                @Override
                public void onOpen() {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setStatus("请说话");
                        }
                    });
                }

                @Override
                public void onStart() {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setStatus("正在识别");
                        }
                    });
                }

                @Override
                public void onMiddleResult(final String result) {
                    LogUtils.d("test---", "onMiddleResult:" + result);
                    if (null != mAsrListener) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAsrListener.onFinish(result);
                            }
                        });
                    }
                }

                @Override
                public void onFinalResult(final String result) {

                }

                @Override
                public void onError(String errorMessage) {
                }

                @Override
                public void onClose() {
                }

                @Override
                public void onVolume(final int volume) {
                    if (null == mView) {
                        return;
                    }
                    LogUtils.d("watervoice:", "   volume:" + volume);
                    mView.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.showRecordTimeAndAnimate(volume);
                        }
                    });
                }
            });
        }
        mWebApiManager.startTransport();
    }

    private void start() {
        initView();
        if (mRecognizer == null) {
            mRecognizer = new SpeechRecognizer(mContext, new AbsRecognizerListener() {
                @Override
                public void onAsrReady() {
                    //todo 没有这个回调
                    if (null == mView) {
                        return;
                    }
                    mView.setStatus("请说话");
                }

                @Override
                public void onAsrBegin() {
                    if (null == mView) {
                        return;
                    }
                    mView.setStatus("正在识别");
                }

                @Override
                public void onAsrFinalResult(String result, byte[] data) {
                    if (null != mAsrListener) {
                        mAsrListener.onFinish(result);
                    }
                }

                @Override
                public void onAsrVolume(int volumePercent, int volume) {
                    if (null == mView) {
                        return;
                    }
                    mView.showRecordTimeAndAnimate(volume);
                }
            });
        }
        cancel();
        mRecognizer.start();
    }

    private void finish() {
        if (mRecognizer != null) {
            mRecognizer.stop();
        }
        if (mWebApiManager != null) {
            mWebApiManager.stopTransport();
        }
    }

    private void cancel() {
        if (mRecognizer != null) {
            mRecognizer.cancel();
        }
        if (mWebApiManager != null) {
            mWebApiManager.stopTransport();
//            mWebApiManager.cancel();
        }
    }

    public void destroy() {
        if (mRecognizer != null) {
            mRecognizer.release();
            mRecognizer = null;
        }
        if (mWebApiManager != null) {
            mWebApiManager.release();
            mWebApiManager = null;
        }
    }

}
