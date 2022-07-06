package com.susu.baselibrary.utils.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.susu.baselibrary.utils.base.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author : sudan
 * Time : 2021/5/18
 * Description: 刘海屏判断
 */
public class NotchUtils {
    /**
     * 系统属性
     * The constant SYSTEM_PROPERTIES.
     */
    private static final String SYSTEM_PROPERTIES = "android.os.SystemProperties";
    /**
     * 华为刘海
     * The constant NOTCH_HUA_WEI.
     */
    private static final String NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil";
    /**
     * 小米刘海：目前小米的状态栏高度会略高于刘海屏的高度
     * The constant NOTCH_XIAO_MI.
     */
    private static final String NOTCH_XIAO_MI = "ro.miui.notch";
    /**
     * OPPO刘海：目前没提供获取刘海尺寸，而且目前为止刘海屏的机型尺寸规格都是统一的，不排除以后机型会有变化；
     * 显示屏宽度为1080px，高度为2280px；刘海区域则都是宽度为324px, 高度为80px
     * The constant NOTCH_OPPO.
     */
    private static final String NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism";
    /**
     * VIVO刘海：目前没有提供接口获取刘海尺寸；可参考刘海区域：宽100dp,高27dp；
     * The constant NOTCH_VIVO.
     */
    private static final String NOTCH_VIVO = "android.util.FtFeature";
    // 是否有凹槽
    private static final int NOTCH_VIVO_IN_SCREEN = 0x00000020;
    // 是否有圆角
    private static final int NOTCH_VIVO_IN_ROUNDED = 0x00000008;


    /**
     * 判断是否是刘海屏
     * Has notch screen boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean hasNotchScreen(Activity activity) {
        return activity != null && (hasNotchAtXiaoMi(activity) ||
                hasNotchAtHuaWei(activity) ||
                hasNotchAtOPPO(activity) ||
                hasNotchAtVIVO(activity) ||
                hasNotchAtAndroidP(activity));
    }

    /**
     * 获取刘海高度
     *
     * @param activity
     * @return
     */
    public static int getNotchHeightScreen(Activity activity) {
        if (activity == null) {
            return 0;
        }
        if (hasNotchAtHuaWei(activity)) {
            return getNotchSizeAtHuaWei(activity);
        } else if (hasNotchAtXiaoMi(activity)) {
            return getNotchSizeAtXiaoMi(activity);
        } else {
            return 0;
        }
    }

    /**
     * Android P 上应用刘海
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
     *
     * @param activity
     */
    public static void fitsNotchScreenAtAndroidP(Activity activity) {
        if (activity != null && Build.VERSION.SDK_INT >= 28/*Build.VERSION_CODES.P*/) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(lp);
        }
    }

    /**
     * Android P 刘海屏判断
     * Has notch at android p boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean hasNotchAtAndroidP(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                if (activity != null) {
                    Window window = activity.getWindow();
                    if (window != null) {
                        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
                        if (windowInsets != null) {
                            Class windowInsetsClass = windowInsets.getClass();
                            Object displayCutout = windowInsetsClass.getMethod("getDisplayCutout").invoke(windowInsets);
                            LogUtils.d("WYNotchUtils", "displayCutout=" + displayCutout);
                            return displayCutout != null;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 小米刘海屏判断.
     * Has notch at xiao mi int.
     *
     * @param context the context
     * @return the int
     */
    private static boolean hasNotchAtXiaoMi(Context context) {
        int result = 0;
        if ("Xiaomi".equals(Build.MANUFACTURER)) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                @SuppressLint("PrivateApi")
                Class<?> aClass = classLoader.loadClass(SYSTEM_PROPERTIES);
                Method method = aClass.getMethod("getInt", String.class, int.class);
                result = (Integer) method.invoke(aClass, NOTCH_XIAO_MI, 0);

            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException ignored) {
            } catch (ClassNotFoundException ignored) {
            }
        }
        // 为1是刘海屏手机
        return result == 1;
    }

    /**
     * 华为刘海屏判断
     * Has notch at hua wei boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private static boolean hasNotchAtHuaWei(Context context) {
        boolean result = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> aClass = classLoader.loadClass(NOTCH_HUA_WEI);
            Method get = aClass.getMethod("hasNotchInScreen");
            result = (boolean) get.invoke(aClass);
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * VIVO刘海屏判断
     * Has notch at vivo boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private static boolean hasNotchAtVIVO(Context context) {
        boolean result = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressLint("PrivateApi")
            Class<?> aClass = classLoader.loadClass(NOTCH_VIVO);
            Method method = aClass.getMethod("isFeatureSupport", int.class);
            result = (boolean) method.invoke(aClass, NOTCH_VIVO_IN_SCREEN);
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * OPPO刘海屏判断
     * Has notch at oppo boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature(NOTCH_OPPO);
    }

    /**
     * 获取华为刘海屏的参数
     *
     * @param context
     * @return
     */
    private static int getNotchSizeAtHuaWei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class aClass = cl.loadClass(NOTCH_HUA_WEI);
            Method get = aClass.getMethod("getNotchSize");
            ret = (int[]) get.invoke(aClass);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        }
        return ret[1];
    }

    /**
     * 获取小米刘海的高度
     *
     * @param context
     * @return
     */
    private static int getNotchSizeAtXiaoMi(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
