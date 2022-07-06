package com.susu.baselibrary.utils.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.susu.baselibrary.utils.base.CollectionUtils;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.StringUtils;
import com.susu.baselibrary.utils.fileProvider.FileProviderUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class SystemUtils {
    private static final String TAG = "SystemUtils";

    public static String getSystemLanguage() {
        try {
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            if (StringUtils.isNotNull(language)) {
                return language + "-" + locale.getCountry();
            }
        } catch (Exception e) {
        }

        return "";
    }


    /**
     * 根据路径，apk安装
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        if (StringUtils.isNotNull(apkPath)) {
            File apk = new File(apkPath);
            installApk(context, apk);
        }
    }

    /**
     * apk安装
     *
     * @param context
     * @param apk
     */
    public static void installApk(Context context, File apk) {
        try {
            // 适配7.0
            Uri uri = FileProviderUtils.getFileUri(context, apk);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 必须要加
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.show(context, "APK安装失败，您可选择卸载后从市场安装。");
            LogUtils.LogException(TAG, e.getMessage(), e);
        }
    }

    /**
     * 得到当前的进程名
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        String processName = null;
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
            }
        }
        return processName;
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param context   context
     * @param className 判断的服务名字：包名+类名; NewVersionDownloadService.class.getName()
     * @return true 在运行, false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (StringUtils.isNotNull(className) && CollectionUtils.isNotEmpty(serviceList)) {
            for (int i = 0; i < serviceList.size(); i++) {
                ActivityManager.RunningServiceInfo info = serviceList.get(i);
                if (info != null
                        && info.service != null
                        && className.equals(info.service.getClassName())) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }
}
