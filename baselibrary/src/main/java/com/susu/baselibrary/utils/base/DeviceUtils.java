package com.susu.baselibrary.utils.base;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2020/12/11
 * Description:
 */
public class DeviceUtils {

    public interface DeviceInfoCallBack {
        void onGetDeviceInfoSucceed(List<String> var1);

        void onGetDeviceInfoFailed();
    }

    /**
     * 获取如影设备id
     *
     * @param context
     * @return
     */
    public static List<String> getDeviceId(Context context) {
        final List<String> ret = new ArrayList();
        getAllDevicesInHome(context, new DeviceInfoCallBack() {
            @Override
            public void onGetDeviceInfoSucceed(List<String> list) {
                ret.addAll(list);
            }

            @Override
            public void onGetDeviceInfoFailed() {

            }
        });

        return ret;
    }

    public static void getAllDevicesInHome(Context context, DeviceInfoCallBack callBack) throws IllegalStateException {
        ArrayList list = new ArrayList<>();
        String deviceSN = getDeviceSN();
        if (isIs13(context)) {
            list.add(deviceSN);
        }

        if (callBack != null) {
            callBack.onGetDeviceInfoSucceed(list);
        } else {
            throw new IllegalArgumentException("Error: DeviceInfoCallBack not set!!!");
        }
    }

    public static String getDeviceSN() {
        String var0 = null;

        String var7;
        label42:
        {
            Exception var10000;
            label37:
            {
                Class var5;
                boolean var10001;
                try {
                    var5 = Class.forName("android.os.SystemProperties");
                } catch (Exception var4) {
                    var10000 = var4;
                    var10001 = false;
                    break label37;
                }

                Class var1 = var5;
                String var6 = "get";

                Class[] var10002;
                try {
                    var10002 = new Class[1];
                } catch (Exception var3) {
                    var10000 = var3;
                    var10001 = false;
                    break label37;
                }

                Class[] var10003 = var10002;
                byte var10004 = 0;

                try {
                    var10003[var10004] = String.class;
                    var7 = (String) var5.getMethod(var6, var10002).invoke(var1, "ro.serialno");
                    break label42;
                } catch (Exception var2) {
                    var10000 = var2;
                    var10001 = false;
                }
            }

            var10000.printStackTrace();
            return var0;
        }

        return var7;
    }

    public static boolean isIs13(Context var0) {
        if (Build.DEVICE.equals("inSight13")) {
            return true;
        } else {
            return getDisplayMetrics(var0).widthPixels != 1280;
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context var0) {
        WindowManager wm = (WindowManager) var0.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        }

        return displayMetrics;
    }

    public static String getDeviceIdSN() {
        return getDeviceSN();
    }
}
