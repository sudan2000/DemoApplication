package com.susu.baselibrary.updateversion;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.susu.baselibrary.permission.PermissionManager;
import com.susu.baselibrary.permission.listener.Action;
import com.susu.baselibrary.utils.base.CollectionUtils;
import com.susu.baselibrary.utils.base.EncryptUtils;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.StringUtils;
import com.susu.baselibrary.utils.stream.FileUtils;
import com.susu.baselibrary.utils.system.SystemUtils;
import com.susu.baselibrary.utils.ui.DialogUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;

import java.io.File;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/6/12
 * Description: 安装apk、配置信息、显示更新弹窗相关
 */
public class UpdateVersionUtils {

    private static final String TAG = "UpdateVersionUtils";
    private static final String DOWNLOAD_SERVICE = NewVersionDownloadService.class.getName();

    public static String APPLICATION_ID;

    /**
     * 消息通知
     */
    public static int NOTIFICATION_ID = 345738740;

    /**
     * 手机网络切换成非wifi时，接收的变更的广播
     */
    public static String SERVICE_DISCONNECT_BROADCAST = "";


    /**
     * apk文件名
     */
    private static String APK_NAME = "";

    /**
     * apk文件名前缀
     */
    public static String APK_NAME_START = "susu-";

    /**
     * 保存apk的文件夹
     */
    public static String SAVE_DIR = "";

    /**
     * 通知栏显示的app图标
     */
    public static int NOTIFY_ICON;


    /**
     * 在调用更新功能之前，需要初始化配置信息；
     * 防止手机上存在多个app(医生版、用户版)，下面4个参数得外部传入；
     *
     * @param notificationId
     * @param saveDir        保存apk的文件夹
     * @param notifyIcon     R.drawable.ic_launcher
     * @param applicationId
     */
    public static void setUpdateConfigInfo(int notificationId, String saveDir, int notifyIcon, String applicationId) {
        NOTIFICATION_ID = notificationId;
        SAVE_DIR = saveDir;
        NOTIFY_ICON = notifyIcon;
        APPLICATION_ID = applicationId;
        SERVICE_DISCONNECT_BROADCAST = APPLICATION_ID + ".ServiceDisConnectReceiver";
    }


    /**
     * 获取apk保存路径
     *
     * @param downUrl 下载apk的url
     */
    public static String getApkPath(String downUrl) {
        return SAVE_DIR + getApkName(downUrl);
    }


    /**
     * 获取apk文件名
     * 每个版本的apk URL是不一样的，防止在上个版本的temp文件上继续去下载
     * old文件删除根据"susu-"来判断，
     *
     * @param downUrl
     * @return
     */
    public static String getApkName(String downUrl) {
        if (StringUtils.isEmpty(APK_NAME)) {
            String md5 = EncryptUtils.stringToMD5(downUrl);
            APK_NAME = APK_NAME_START + md5 + ".apk";
        }
        return APK_NAME;
    }


//----------------------->

    /**
     * 需要在异步线程中运行
     * apk SHA1完整性校验，通过则安装，否则apk删除
     */
    public static boolean ApkSHA1Check(final Context context, final boolean isAutoInstall, final String url, final String digest) {
        boolean hasDownload = false;
        try {
            File saveFile = new File(UpdateVersionUtils.getApkPath(url));
            if (saveFile.exists()) {
                hasDownload = EncryptUtils.SHA1StrVerifier(UpdateVersionUtils.getApkPath(url), digest);
                if (hasDownload) {
                    if (isAutoInstall) {
                        installAPK(context, url);
                    }
                } else {
                    FileUtils.deleteDir(UpdateVersionUtils.getApkPath(url));
                }
            }

        } catch (Exception e) {
            LogUtils.LogException(TAG, e.getMessage(), e);
            FileUtils.deleteDir(UpdateVersionUtils.getApkPath(url));
        }
        return hasDownload;
    }


    /**
     * 安装apk
     */
    public static void installAPK(final Context context, final String url) {
        if (context != null) {
            PermissionManager
                    .with(context)
                    .install()
                    .requestPermissionListener(new Action<Void>() {
                        @Override
                        public void onGranted(Void data) {
                            SystemUtils.installApk(context, UpdateVersionUtils.getApkPath(url));
                        }

                        @Override
                        public void onDenied(Void data) {
                            ToastUtils.showToast("安装应用授权失败");
                        }
                    })
                    .request();
        }
    }


//----------------------->

    /**
     * 显示对应的升级对话框
     */
    public static void showUpdateVersionDialog(Activity activity, VersionInfo info, DownLoadServiceListener listener, FragmentManager fragmentManager) {
        switch (info.cueLevel) {
            case VersionInfo.LEVEL_MUST:
                showUpdateDialog(info, activity, listener, fragmentManager, ApkSHA1CheckTask.MUST_UPDATE);
                break;
            case VersionInfo.LEVEL_SUGGEST:
                showUpdateDialog(info, activity, listener, null, ApkSHA1CheckTask.SUGGEST_UPDATE);
                break;
            case VersionInfo.LEVEL_NONE:
            default:
                break;
        }
    }


    /**
     * 判断本地是否存在apk，存在则进行apk校验，然后显示对应的弹窗
     */
    public static void showUpdateDialog(VersionInfo info,
                                        Activity activity,
                                        DownLoadServiceListener listener,
                                        FragmentManager fragmentManager,
                                        int type) {

        new ApkSHA1CheckTask(info, activity, listener, fragmentManager, type).execute();
    }


    /**
     * 强制升级对话框
     *
     * @param info
     * @param activity
     * @param listener
     * @param fragmentManager
     */
    public static void showMustUpdateDialog(final VersionInfo info,
                                            final Activity activity,
                                            final DownLoadServiceListener listener,
                                            final FragmentManager fragmentManager) {
        if (activity != null) {
            DialogUtils.showCommonDialogCancelable(
                    activity,
                    "发现新版本",
                    info.desc,
                    info.hasDownload ? "立即安装" : "立即更新",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMustUpdateProgressDialog(activity, info, listener, fragmentManager);
                        }
                    },
                    "关闭",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.finish();
                        }
                    },
                    false
            );
        }
    }


    /**
     * 强制升级的下载进度条dialog
     *
     * @param info
     * @param listener
     * @param fragmentManager
     */
    public static void showMustUpdateProgressDialog(Activity activity,
                                                    VersionInfo info,
                                                    DownLoadServiceListener listener,
                                                    FragmentManager fragmentManager) {

        if (info.hasDownload) {
            installAPK(activity, info.downUrl);
        } else {
            DownloadAPKFragment f = DownloadAPKFragment.newInstance(info);
            f.setCancelable(false);
            f.setServiceListener(listener);
            f.show(fragmentManager, null);
        }
    }


//----------------------->


    public static boolean isServiceRunning(Context context) {
        return isServiceRunning(context, DOWNLOAD_SERVICE);
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param context   context
     * @param className 判断的服务名字：包名+类名
     * @return true 在运行, false 不在运行
     */
    private static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (CollectionUtils.isNotEmpty(serviceList)) {
            for (int i = 0; i < serviceList.size(); i++) {
                if (className.equals(serviceList.get(i).service.getClassName())) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }
}
