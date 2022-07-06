package com.susu.baselibrary.utils.system;

import android.content.Context;
import android.os.Build;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public class OsUtils {

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static int getTargetSdkVersion(Context context) {
        return context.getApplicationInfo().targetSdkVersion;
    }
}
