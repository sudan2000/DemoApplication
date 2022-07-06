package com.susu.baselibrary.permission.support;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.susu.baselibrary.permission.support.manufacturer.HUAWEI;
import com.susu.baselibrary.permission.support.manufacturer.MEIZU;
import com.susu.baselibrary.permission.support.manufacturer.OPPO;
import com.susu.baselibrary.permission.support.manufacturer.PermissionsPage;
import com.susu.baselibrary.permission.support.manufacturer.Protogenesis;
import com.susu.baselibrary.permission.support.manufacturer.VIVO;
import com.susu.baselibrary.permission.support.manufacturer.XIAOMI;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class PermissionsPageManager {
    /**
     * Build.MANUFACTURER
     */
    static final String MANUFACTURER_HUAWEI = "HUAWEI";
    static final String MANUFACTURER_XIAOMI = "XIAOMI";
    static final String MANUFACTURER_OPPO = "OPPO";
    static final String MANUFACTURER_VIVO = "vivo";
    static final String MANUFACTURER_MEIZU = "meizu";
    static final String manufacturer = Build.MANUFACTURER;

    /**
     * 返回制造商
     *
     * @return
     */
    public static String getManufacturer() {
        return manufacturer;
    }

    /**
     * 跳转权限设置页面
     *
     * @param activity
     * @param requestCode
     */
    public static void startSettingPage(@NonNull Activity activity, int requestCode) {
        try {
            Intent intent = getSettingIntent(activity);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转悬浮窗设置页面
     *
     * @param activity
     * @param requestCode
     */
    public static void startOverlayPage(@NonNull Activity activity, int requestCode) {
        try {
            Intent intent = getOverlayIntent(activity);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            startProtogenesisOverlay(activity, requestCode);
        }
    }

    /**
     * 跳转安装应用设置页面
     *
     * @param activity
     * @param requestCode
     */
    public static void startInstallPage(@NonNull Activity activity, int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * start write settings page
     *
     * @param context
     * @return
     */
    public static Intent startWriteSettingsPage(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        return intent;
    }

    /**
     * 应用通知设置页跳转
     * 在部分手机中无法精确的跳转到APP对应的通知设置界面，就直接跳转到APP信息界面
     *
     * @param context
     */
    public static void startNotificationPage(Activity context, int requestCode) {
        try {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //这种方案适用于 API 26, 这个Action是 API 26 后增加的 即8.0（含8.0）以上可以用
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", context.getPackageName());
                intent.putExtra("app_uid", context.getApplicationInfo().uid);
            } else {
                intent = getSettingIntent(context);
            }
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivityForResult(getSettingIntent(context), requestCode);
        }
    }

    public static Intent getIntent(Activity activity) {
        PermissionsPage permissionsPage = new Protogenesis(activity);
        try {
            if (MANUFACTURER_HUAWEI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new HUAWEI(activity);
            } else if (MANUFACTURER_OPPO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new OPPO(activity);
            } else if (MANUFACTURER_VIVO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new VIVO(activity);
            } else if (MANUFACTURER_XIAOMI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new XIAOMI(activity);
            } else if (MANUFACTURER_MEIZU.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new MEIZU(activity);
            }

            return permissionsPage.settingIntent();
        } catch (Exception e) {
            Log.e("PermissionsPageManager", "手机品牌为：" + manufacturer + "异常抛出，：" + e.getMessage());
            permissionsPage = new Protogenesis(activity);
            return ((Protogenesis) permissionsPage).settingIntent();
        }
    }

    public static Intent getSettingIntent(Activity activity) {
        return new Protogenesis(activity).settingIntent();
    }

    public static Intent getOverlayIntent(Activity context) {
        PermissionsPage permissionsPage = new Protogenesis(context);
        try {
            if (MANUFACTURER_HUAWEI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new HUAWEI(context);
            } else if (MANUFACTURER_OPPO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new OPPO(context);
            } else if (MANUFACTURER_VIVO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new VIVO(context);
            } else if (MANUFACTURER_XIAOMI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new XIAOMI(context);
            } else if (MANUFACTURER_MEIZU.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new MEIZU(context);
            }
            return permissionsPage.overlayIntent();
        } catch (Exception e) {
            Log.e("PermissionsPageManager", "手机品牌为：" + manufacturer + "异常抛出，：" + e.getMessage());
            permissionsPage = new Protogenesis(context);
            return ((Protogenesis) permissionsPage).overlayIntent();
        }
    }

    public static boolean isXIAOMI() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_XIAOMI);
    }

    public static boolean isOPPO() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_OPPO);
    }

    public static boolean isMEIZU() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_MEIZU);
    }

    /**
     * 跳转原生悬浮窗设置页面
     *
     * @param activity
     * @param requestCode
     */
    public static void startProtogenesisOverlay(Activity activity, int requestCode) {
        try {
            Intent intent = new Protogenesis(activity).overlayIntent();
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
