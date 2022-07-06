package com.susu.baselibrary.updateversion;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.susu.baselibrary.utils.base.LogUtils;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class DownloadAPKFragment extends DialogFragment {
    public static final String TAG = DownloadAPKFragment.class.getSimpleName();
    private static final String KEY_VERSIONINFO = "DownloadAPKFragment.VersionInfo";

    private IDownloadApkService mService;
    private VersionInfo mInfo;
    private DownLoadServiceListener mServiceListener;


    public static DownloadAPKFragment newInstance(VersionInfo info) {
        DownloadAPKFragment f = new DownloadAPKFragment();
        Bundle b = new Bundle();
        b.putSerializable(KEY_VERSIONINFO, info);
        f.setArguments(b);
        f.setStyle(DialogFragment.STYLE_NORMAL, 0);
        return f;
    }

    public void setServiceListener(DownLoadServiceListener listener) {
        this.mServiceListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("下载新版本");
        try {
            dialog.setProgressNumberFormat("%dKB/%dKB");
        } catch (Exception e) {
            LogUtils.e(TAG, "error while create dialog!");
        }
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        Bundle bundle = getArguments();
        if (activity != null && bundle != null) {
            mInfo = (VersionInfo) bundle.getSerializable(KEY_VERSIONINFO);
            final Intent i = NewVersionDownloadService.createIntent(activity, mInfo.downUrl, mInfo.messageDigest, true, true);
            if (!UpdateVersionUtils.isServiceRunning(activity)) {
                activity.startService(i);
            }
            activity.bindService(i, conn, 0);
            NewVersionDownloadService.setServiceListener(mServiceListener);
        } else {
            dismiss();
        }
    }


    @Override
    public void onDestroyView() {
        try {
            if (mService != null) {
                mService.unregisterCallback(mCallback);
                mService = null;
            }
        } catch (RemoteException e) {
            LogUtils.LogException(TAG, e.getMessage(), e);
        }
        getActivity().unbindService(conn);
        super.onDestroyView();
    }

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IDownloadApkService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                LogUtils.LogException(TAG, e.getMessage(), e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

    };

    private final IDownloadApkCallback.Stub mCallback = new IDownloadApkCallback.Stub() {

        @Override
        public void onResult(final int code) throws RemoteException {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (code != NewVersionDownloadService.RESULT_SUCCESS) {
                        LogUtils.e(TAG, "download new version failed:" + code);
                    }
                    dismissAllowingStateLoss();
                }
            });

        }

        @Override
        public void onProgress(final int percent, final long count, final long total)
                throws RemoteException {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ProgressDialog d = (ProgressDialog) getDialog();
                    if (d != null) {
                        d.setMax((int) total / 1024);
                        d.setProgress((int) count / 1024);
                    }
                }
            });

        }
    };


    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            if (manager != null) {
                super.show(manager, tag);
            }
        } catch (Exception e) {
            LogUtils.LogException(TAG, e.getMessage(), e);
        }
    }
}
