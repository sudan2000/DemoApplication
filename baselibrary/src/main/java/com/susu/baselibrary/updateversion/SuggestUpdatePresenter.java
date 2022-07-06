package com.susu.baselibrary.updateversion;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;

import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.NetworkUtils;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class SuggestUpdatePresenter {
    public static final String TAG = "SuggestUpdatePresenter";

    /**
     * 是否设置了wifi环境下自动下载最新安装包
     */
    public static final String IS_AUTO_DOWNLOAD_APK = "isAutoDownloadAPK";

    public static final String KEY_AUTO_DOWNLOAD = "download";

    private Activity mContext;
    private VersionInfo mInfo;
    private IDownloadApkService mService;
    private DownLoadServiceListener mServiceListener;
    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IDownloadApkService.Stub.asInterface(service);
            try {
                mService.injectShowProgress(true);
                mService.injectAutoInstall(true);
            } catch (RemoteException e) {
                LogUtils.LogException(TAG, e.getMessage(), e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };


    public SuggestUpdatePresenter(Activity mContext, VersionInfo info, DownLoadServiceListener listener) {
        this.mContext = mContext;
        this.mInfo = info;
        this.mServiceListener = listener;
    }


    public boolean autoDownload() {
        SharedPreferences sp = mContext.getSharedPreferences(IS_AUTO_DOWNLOAD_APK, Context.MODE_PRIVATE);
        LogUtils.i(TAG, "SAVE:" + sp.getBoolean(KEY_AUTO_DOWNLOAD, true));
        LogUtils.i(TAG, "download:" + mInfo.hasDownload);
        LogUtils.i(TAG, "wifi:" + NetworkUtils.isWifiConnected(mContext));
        return sp.getBoolean(KEY_AUTO_DOWNLOAD, true) && !mInfo.hasDownload
                && NetworkUtils.isWifiConnected(mContext);
    }

    public String message() {
        return mInfo.desc;
    }

    public boolean hasDownload() {
        return mInfo.hasDownload;
    }

    public void sureClick() {
        if (hasDownload()) {
            UpdateVersionUtils.installAPK(mContext, mInfo.downUrl);
        } else {
            if (UpdateVersionUtils.isServiceRunning(mContext)) {
                mContext.bindService(new Intent(mContext, NewVersionDownloadService.class), conn, 0);
            } else {
                mContext.startService(NewVersionDownloadService.createIntent(
                        mContext, mInfo.downUrl, mInfo.messageDigest, true, true));
            }
            NewVersionDownloadService.setServiceListener(mServiceListener);
        }
    }

    public void cancelClick() {
        if (autoDownload()) {
            mContext.startService(NewVersionDownloadService.createIntent(
                    mContext, mInfo.downUrl, mInfo.messageDigest, false, false));
            NewVersionDownloadService.setServiceListener(mServiceListener);
            NewVersionDownloadService.setToast(false);
        }
    }
}
