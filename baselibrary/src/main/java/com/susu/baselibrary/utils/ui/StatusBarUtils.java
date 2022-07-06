package com.susu.baselibrary.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.ui.NotchUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author : sudan
 * Time : 2021/5/18
 * Description:
 */
public class StatusBarUtils {
    private static final String TAG = "StatusBarUtils";
    private static final int TAG_KEY_PADDING_OFFSET = -123;
    private static final int TAG_KEY_MARGIN_OFFSET = -125;

    /**
     * 状态栏
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 状态栏+底部导航栏
     *
     * @param context
     * @return
     */
    public static int getAllBarsHeight(Context context) {
        int height = getStatusBarHeight(context);
        if (checkDeviceHasNavigationBar(context)) {
            height += getNavigationBarHeight(context);
        }
        return height;
    }

    /**
     * 底部导航栏
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    /**
     * 启用 透明状态栏
     *
     * @param activity
     */
    public static void enableFullscreen(Activity activity) {
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 是否达到需要处理状态栏的要求（对6.0以上进行处理）
     *
     * @return
     */
    public static boolean needDealStatusBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 兼容刘海屏：获取状态栏和刘海的最高高度
     *
     * @return
     */
    public static int getTopHighestHeight(Activity activity) {
        int height = 0;
        if (NotchUtils.hasNotchScreen(activity)) {
            height = NotchUtils.getNotchHeightScreen(activity);
        }
        return Math.max(height, getStatusBarHeight(activity));
    }

    /**
     * 设置状态栏白底黑字：全屏
     *
     * @param activity
     * @param padding  是否增加padding
     */
    public static void setSystemBarWhiteColorFullScreen(Activity activity, boolean padding) {
        if (needDealStatusBar()) {
            setSystemBarColor(activity, Color.WHITE);
            setStatusBarMode(activity, true, true);
            if (padding) {
                addPaddingRootView(activity);
            }
        }
    }

    /**
     * 设置状态栏白底黑字：全屏
     *
     * @param activity
     */
    public static void setSystemBarWhiteColorFullScreen(Activity activity) {
        if (needDealStatusBar()) {
            setSystemBarColor(activity, Color.WHITE);
            setStatusBarMode(activity, true, true);
            addPaddingRootView(activity);
        }
    }

    /**
     * 设置状态栏白底黑字：非全屏
     *
     * @param activity
     */
    public static void setSystemBarWhiteColor(Activity activity) {
        if (needDealStatusBar()) {
            setSystemBarColor(activity, Color.WHITE);
            setStatusBarMode(activity, true, false);
        }
    }

    /**
     * 设置状态栏颜色：全屏
     *
     * @param activity
     * @param color
     */
    public static void setSystemBarTintFullScreen(Activity activity, int color) {
        if (needDealStatusBar()) {
            setSystemBarColor(activity, color);
            setStatusBarMode(activity, false, true);
            addPaddingRootView(activity);
        }
    }

    /**
     * 设置状态栏颜色：非全屏
     *
     * @param activity
     * @param color    状态栏颜色
     */
    public static void setSystemBarTint(Activity activity, int color) {
        setSystemBarTint(activity, color, false);
    }

    /**
     * 设置状态栏模式颜色：非全屏
     *
     * @param activity
     * @param color    状态栏颜色
     * @param dark     状态栏模式
     */
    public static void setSystemBarTint(Activity activity, int color, boolean dark) {
        if (needDealStatusBar()) {
            setSystemBarColor(activity, color);
            setStatusBarMode(activity, dark, false);
        }
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param activity
     */
    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        if (activity == null || needOffsetView == null || !needDealStatusBar()) {
            return;
        }
        clearPaddingRootView(activity);
        // 透明状态栏
        setTransparentForWindow(activity);
        setStatusBarMode(activity, false, false);
        // 设置距离
        setViewMarginTop(activity, needOffsetView);
    }

    /**
     * 透明背景，白色字体
     *
     * @param activity
     */
    public static void setTranslucentForImageView(Activity activity, boolean dark) {
        if (activity == null || !needDealStatusBar()) {
            return;
        }
        clearPaddingRootView(activity);
        // 透明状态栏
        setTransparentForWindow(activity);
        setStatusBarMode(activity, dark, false);
    }

    /**
     * 设置 view padding top 状态栏高度
     *
     * @param activity
     * @param needOffsetView
     */
    public static void setViewPaddingTop(Activity activity, View needOffsetView) {
        if (activity == null || needOffsetView == null || !needDealStatusBar()) {
            return;
        }
        Object haveSetOffset = needOffsetView.getTag(TAG_KEY_PADDING_OFFSET);
        if (haveSetOffset != null && (Boolean) haveSetOffset) {
            return;
        }
        needOffsetView.setPadding(needOffsetView.getPaddingLeft(),
                needOffsetView.getPaddingTop() + getTopHighestHeight(activity),
                needOffsetView.getPaddingRight(),
                needOffsetView.getPaddingBottom());
        needOffsetView.setTag(TAG_KEY_PADDING_OFFSET, true);
    }

    /**
     * 设置 view margin top 状态栏高度
     *
     * @param activity
     * @param needOffsetView
     */
    public static void setViewMarginTop(Activity activity, View needOffsetView) {
        if (activity == null || needOffsetView == null || !needDealStatusBar()) {
            return;
        }
        Object haveSetOffset = needOffsetView.getTag(TAG_KEY_MARGIN_OFFSET);
        if (haveSetOffset != null && (Boolean) haveSetOffset) {
            return;
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getTopHighestHeight(activity),
                layoutParams.rightMargin, layoutParams.bottomMargin);
        needOffsetView.setTag(TAG_KEY_MARGIN_OFFSET, true);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     */
    private static void setSystemBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置状态栏透明、内容延伸到状态栏上
     *
     * @param activity
     */
    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 设置状态栏模式：只在Android 6.0及以上有效
     *
     * @param activity
     * @param dark
     */
    private static void setStatusBarMode(Activity activity, boolean dark, boolean fullScreen) {
        if (activity == null || !needDealStatusBar()) {
            return;
        }
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        int oldVis = window.getDecorView().getSystemUiVisibility();
        // 是否全屏
        int newVis = oldVis;
        if (fullScreen) {
            newVis |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        if (dark) {
            newVis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            newVis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        if (newVis != oldVis) {
            window.getDecorView().setSystemUiVisibility(newVis);
        }
        setMIUIStatusBarDarkIcon(activity, dark);
        setMeizuStatusBarDarkIcon(activity, dark);
    }

    /**
     * 设置 paddingTop = statusHeight
     *
     * @param activity
     */
    private static void addPaddingRootView(Activity activity) {
        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        rootView.setPadding(0, getTopHighestHeight(activity), 0, 0);
    }

    /**
     * 清除 paddingTop
     *
     * @param activity
     */
    private static void clearPaddingRootView(Activity activity) {
        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        if (rootView.getPaddingTop() != 0) {
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 修改 MIUI V6 以上状态栏颜色
     */
    private static void setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        if (activity == null || !"XIAOMI".equalsIgnoreCase(Build.MANUFACTURER)) {
            return;
        }
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private static void setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        if (activity == null || !"meizu".equalsIgnoreCase(Build.MANUFACTURER)) {
            return;
        }
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 获取状态栏高度，同时处理魅族FlymeOS4.x/Android4.4.4的获取状态栏的问题
     */
    public static int getHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d(TAG, "statusBarHeight--->" + statusBarHeight);
        if (isFlymeOs4x()) {
            return 2 * statusBarHeight;
        }
        return statusBarHeight;
    }

    public static boolean isFlymeOs4x() {
        String sysVersion = android.os.Build.VERSION.RELEASE;
        if ("4.4.4".equals(sysVersion)) {
            String sysIncrement = android.os.Build.VERSION.INCREMENTAL;
            String displayId = android.os.Build.DISPLAY;
            if (!TextUtils.isEmpty(sysIncrement)) {
                return sysIncrement.contains("Flyme_OS_4");
            } else {
                return displayId.contains("Flyme OS 4");
            }
        }
        return false;
    }

}
