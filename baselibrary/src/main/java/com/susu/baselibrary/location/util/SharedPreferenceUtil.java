package com.susu.baselibrary.location.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.susu.baselibrary.location.LocationConstants;
import com.susu.baselibrary.utils.system.CoreUtils;

public class SharedPreferenceUtil {

    // 文件名称
    private static final String FILLNAME = "share_data";
    private static SharedPreferences mSharedPreferences = null;

    /**
     * 单例模式
     */
    private static synchronized SharedPreferences getInstance() {
        if (mSharedPreferences == null) {
            mSharedPreferences = CoreUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public static boolean isPrivacyAccept() {
        return getBoolean(LocationConstants.PERMISSIONS_DESC_KEY, false);
    }

    public static void saveStartTrace() {
        SharedPreferences.Editor editor = CommonUtil.getTrackConfig().edit();
        editor.putBoolean("is_trace_started", true);
        editor.apply();
    }

    public static void removeTraceAndGather() {
        SharedPreferences.Editor editor = CommonUtil.getTrackConfig().edit();
        editor.remove("is_trace_started");
        editor.remove("is_gather_started");
        editor.apply();
    }

    public static void saveStartGather() {
        SharedPreferences.Editor editor = CommonUtil.getTrackConfig().edit();
        editor.putBoolean("is_gather_started", true);
        editor.apply();
    }

    public static void removeGather() {
        SharedPreferences.Editor editor = CommonUtil.getTrackConfig().edit();
        editor.remove("is_gather_started");
        editor.apply();
    }

    public static void putString(String key, String value) {
        getInstance().edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return getInstance().getString(key, defValue);
    }

    public static void putBoolean(String key, Boolean value) {
        getInstance().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, Boolean defValue) {
        return getInstance().getBoolean(key, defValue);
    }

    public static boolean contains(String key) {
        return getInstance().contains(key);
    }
}
