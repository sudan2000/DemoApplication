package com.susu.baselibrary.utils.base;

import android.util.Log;

/**
 * Author : sudan
 * Time : 2021/2/3
 * Description:
 */
public class LogUtils {

    private static boolean LOG_DEBUG = true;

    public static void initLOGDEBUG(boolean isDebug) {
        LOG_DEBUG = isDebug;
    }

    public static void d(String msg) {
        if (msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.d("Test----------", msg);
        }
    }

    public static void print(String msg) {
        if (msg == null) {
            return;
        }
        System.out.println(msg);
    }

    public static void d(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.d(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.e(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.w(tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (tag == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.w(tag, tr);
        }
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.wtf(tag, msg, tr);
        }
    }

    public static void wtf(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String tag, Throwable tr) {
        if (tag == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.wtf(tag, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void LogException(String tag, String msg, Throwable tr) {
        if (tag == null || msg == null || tr == null) {
            return;
        }
        if (LOG_DEBUG) {
            Log.e(tag, msg, tr);
        }
    }
}
