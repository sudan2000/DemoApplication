package com.susu.baselibrary.updateversion;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;

import java.io.File;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class ApkSHA1CheckTask extends AsyncTask<String, Integer, Boolean> {

    public static final int SUGGEST_UPDATE = 1;
    public static final int MUST_UPDATE = 2;

    private VersionInfo mInfo;
    private Activity mActivity;
    private DownLoadServiceListener mListener;
    private FragmentManager mFragmentManager;
    private int mType;

    public ApkSHA1CheckTask(VersionInfo info,
                            Activity activity,
                            DownLoadServiceListener listener,
                            FragmentManager fragmentManager, int type) {
        mInfo = info;
        mActivity = activity;
        mListener = listener;
        mFragmentManager = fragmentManager;
        mType = type;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        deleteOldApk();
        boolean hasDownload = UpdateVersionUtils.ApkSHA1Check(mActivity, false, mInfo.downUrl, mInfo.messageDigest);
        mInfo.hasDownload = hasDownload;
        return hasDownload;
    }

    @Override
    protected void onPostExecute(Boolean hasDownload) {
        super.onPostExecute(hasDownload);

        switch (mType) {
            case SUGGEST_UPDATE:
                //显示建议升级弹窗
                if (mActivity != null) {
                    SuggestUpdatePresenter presenter = new SuggestUpdatePresenter(mActivity, mInfo, mListener);
                    SuggestUpdateHolder holder = new SuggestUpdateHolder(presenter, mActivity);
                    holder.showDialog();
                }
                break;
            case MUST_UPDATE:
                //启动下载，显示强制升级的下载进度条dialog
                UpdateVersionUtils.showMustUpdateDialog(mInfo, mActivity, mListener, mFragmentManager);
                break;
            default:
                break;
        }
    }


    /**
     * 删除历史文件
     */
    private void deleteOldApk() {
        File file = new File(UpdateVersionUtils.SAVE_DIR);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getAbsolutePath();
                if (fileName.contains(UpdateVersionUtils.APK_NAME_START)
                        && fileName.contains(".apk")
                        && !fileName.contains(UpdateVersionUtils.getApkName(mInfo.downUrl))) {
                    //删除老的apk及apk.temp，不能删除目标apk及apk.temp
                    files[i].delete();
                }
            }
        }
    }

}
