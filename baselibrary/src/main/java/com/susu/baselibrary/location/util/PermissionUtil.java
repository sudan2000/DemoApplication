package com.susu.baselibrary.location.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/12/1
 * Description:
 */
public class PermissionUtil {

    public static void requestBackgroundLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            0);
                }
            }
        }
    }


    public static void requestPermission(Activity activity) {
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(activity, permissions)) {
            activity.requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
    }

    private static boolean isNeedRequestPermissions(Activity activity, List<String> permissions) {
        // 定位精确位置
        addPermission(activity, permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        addPermission(activity, permissions, Manifest.permission.ACCESS_COARSE_LOCATION);
        // 存储权限
        addPermission(activity, permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 前台服务权限
            addPermission(activity, permissions, Manifest.permission.FOREGROUND_SERVICE);
        }

        return permissions.size() > 0;
    }

    private static void addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }
}
