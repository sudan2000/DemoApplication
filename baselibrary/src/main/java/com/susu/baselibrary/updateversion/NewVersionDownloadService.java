package com.susu.baselibrary.updateversion;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.stream.DownloadUtils;
import com.susu.baselibrary.utils.system.CoreUtils;
import com.susu.baselibrary.utils.ui.NotificationUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;


/**
 * app更新，下载服务
 */
public class NewVersionDownloadService extends IntentService {
    private static final String TAG = "NewVersionDownloadService";
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILED = -1;

    public static final String DOWNLOAD_LINK = "downloadLink";
    public static final String APK_DIGEST = "ApkDigest";
    public static final String SHOW_PROGRESS = "showProgress";
    public static final String AUTO_INSTALL = "mAutoInstall";


    private String mLink = "";
    private String mDigest = "";
    private boolean mShowProgress = true;
    private boolean mAutoInstall = true;
    private NotificationUtils manager;
    private static DownLoadServiceListener mServiceListener;
    private DownloadUtils.PointContinueDownLoadListener mDownLoadListener;
    private ServiceDisConnectReceiver mReceiver;
    private boolean mIsCanceled = false;
    private static boolean mIsShowToast = true;


    public NewVersionDownloadService() {
        super("Download");
    }

    public static Intent createIntent(Activity activity, String url, String digest, boolean showProgress, boolean autoInstall) {
        Intent intent = new Intent(activity, NewVersionDownloadService.class);
        intent.putExtra(DOWNLOAD_LINK, url);
        intent.putExtra(APK_DIGEST, digest);
        intent.putExtra(SHOW_PROGRESS, showProgress);
        intent.putExtra(AUTO_INSTALL, autoInstall);
        return intent;
    }

    public static void setServiceListener(DownLoadServiceListener listener) {
        mServiceListener = listener;
    }

    public static void setToast(boolean isShowToast) {
        mIsShowToast = isShowToast;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initListener();
    }


    private void initListener() {

        if (mDownLoadListener == null) {
            mDownLoadListener = new DownloadUtils.PointContinueDownLoadListener() {
                @Override
                public boolean canceled() {
                    return mIsCanceled;
                }

                @Override
                public void progress(int count, int length) {
                    updateProgress(length, count);
                }

                @Override
                public void success() {
                    dispatchResult(RESULT_SUCCESS);
                    new Thread() {
                        @Override
                        public void run() {
                            UpdateVersionUtils.ApkSHA1Check(NewVersionDownloadService.this, mAutoInstall, mLink, mDigest);
                        }
                    }.start();
                }

                @Override
                public void failed() {
                    dispatchResult(RESULT_FAILED);
                    if (manager != null) {
                        manager.sendDownloadNotify(UpdateVersionUtils.NOTIFICATION_ID, "下载更新失败");
                    }
                }
            };
        }
    }


    private void createNotification() {
        manager = NotificationUtils.getInstance(this);
        manager.initDownloadBuilder(UpdateVersionUtils.NOTIFY_ICON, "正在下载更新", "微医正在下载更新...");
    }

    /**
     * @param length apk总大小
     * @param count  已下载大小
     */
    private void updateProgress(int length, int count) {
        if (mShowProgress) {
            manager.sendDownloadNotify(UpdateVersionUtils.NOTIFICATION_ID, String.format("已下载(%dKB/%dKB)...",
                    count / 1024, length / 1024), length, count);
        }
        LogUtils.i(TAG, String.format("已下载(%dKB/%dKB)...",
                count / 1024, length / 1024));

        dispatchProgress(count, length);
    }


    @Override
    protected void onHandleIntent(Intent arg0) {
        if (mServiceListener != null) {
            mServiceListener.onHandleIntentService();
        }
        registerBroadcast();
        createNotification();
        mLink = arg0.getStringExtra(DOWNLOAD_LINK);
        mDigest = arg0.getStringExtra(APK_DIGEST);
        mShowProgress = arg0.getBooleanExtra(SHOW_PROGRESS, true);
        mAutoInstall = arg0.getBooleanExtra(AUTO_INSTALL, true);

        if (mIsShowToast) {
            ToastUtils.show(CoreUtils.getApp(), "开始下载apk，请勿切换网络");
        }
        DownloadUtils.downloadHttpByPointContinue(UpdateVersionUtils.getApkPath(mLink), mLink, mDownLoadListener);
        if (manager != null) {
            manager.cancel(UpdateVersionUtils.NOTIFICATION_ID);
        }
    }

    private final RemoteCallbackList<IDownloadApkCallback> mCallbacks = new RemoteCallbackList<IDownloadApkCallback>();

    private final IDownloadApkService.Stub mServiceStub = new IDownloadApkService.Stub() {

        @Override
        public void unregisterCallback(IDownloadApkCallback cb) throws RemoteException {
            mCallbacks.unregister(cb);
        }

        @Override
        public void injectAutoInstall(boolean autoInstall) throws RemoteException {
            mAutoInstall = autoInstall;
        }

        @Override
        public void injectShowProgress(boolean showProgress) throws RemoteException {
            mShowProgress = showProgress;
        }

        @Override
        public void registerCallback(IDownloadApkCallback cb) throws RemoteException {
            mCallbacks.register(cb);
        }

    };

    @Override
    public Binder onBind(Intent intent) {
        return mServiceStub;
    }

    @Override
    public void onDestroy() {
        if (mServiceListener != null) {
            mServiceListener.onDestroyService();
        }
        mCallbacks.kill();
        unRegisterBroadcast();
        super.onDestroy();
    }

    private void dispatchProgress(long count, long total) {
        final int N = mCallbacks.beginBroadcast();
        final int percent = (int) ((count + 0.5) / total * 100);

        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i)
                        .onProgress(percent, count, total);
            } catch (RemoteException e) {
                LogUtils.LogException(TAG, e.getMessage(), e);
            }
        }

        mCallbacks.finishBroadcast();
    }

    private void dispatchResult(int code) {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).onResult(code);
            } catch (RemoteException e) {
                LogUtils.LogException(TAG, e.getMessage(), e);
            }
        }
        mCallbacks.finishBroadcast();
    }


    private void registerBroadcast() {
        mReceiver = new ServiceDisConnectReceiver();
        IntentFilter filter = new IntentFilter(UpdateVersionUtils.SERVICE_DISCONNECT_BROADCAST);
        registerReceiver(mReceiver, filter);
    }


    private void unRegisterBroadcast() {
        unregisterReceiver(mReceiver);
        mReceiver = null;
    }


    /**
     * 网络变化，
     */
    public class ServiceDisConnectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mIsCanceled = true;
            LogUtils.i(TAG, "停止下载apk");
        }
    }

}
