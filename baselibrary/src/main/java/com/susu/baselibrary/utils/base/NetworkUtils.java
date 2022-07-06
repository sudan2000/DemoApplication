package com.susu.baselibrary.utils.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.susu.baselibrary.utils.system.CoreUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class NetworkUtils {

    public static final String TAG = "NetWorkUtils";
    public static final String COOKIE_TITLE = "__susu__";
    /**
     * 网络类型
     */
    public static String NETWORK_TYPE = "";

    /**
     * 判断是否有网络连接
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isConnected();
                }
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
        return false;
    }

    public static boolean isNetworkConnected() {
        if (CoreUtils.getApp() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) CoreUtils.getApp()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != mConnectivityManager) {
                try {
                    @SuppressLint("MissingPermission") NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                    if (mNetworkInfo != null) {
                        return mNetworkInfo.isConnected();
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                }
            }

        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    return mWiFiNetworkInfo.isConnected();
                }
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
        return false;

    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mMobileNetworkInfo != null) {
                    return mMobileNetworkInfo.isConnected();
                }
            }
            return false;
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                NetworkInfo mNetworkInfo = mConnectivityManager
                        .getActiveNetworkInfo();
                if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                    return mNetworkInfo.getType();
                }
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
                return -1;
            }
        }
        return -1;
    }

    public static void setCookieWithCheck(Context context, String url, String cookie) {
        try {
            //TODO 医生版没有getHost，直接把url传入 setCookie();
            String path = new URL(url).getHost();
            if (checkUrl(url)) {
                setCookie(context, path, cookie);
            }
        } catch (MalformedURLException e) {
        }
    }

    private static boolean checkUrl(String url) {
        return url.contains("susu.com") ||
                url.contains("bblink.cn") || url.contains("wu.gov.cn") || url.contains("h.gov.cn") ||
                url.contains("d.gov.cn") || url.contains("jinxiang.com");
    }

    public static void setCookie(Context context, String url, String cookie) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }


    /**
     * ToolUtil
     **/

    //获取手机的网络类型
    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "WIFI";
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String _strSubTypeName = networkInfo.getSubtypeName();

                    Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                    // TD-SCDMA   networkType is 17
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                            strNetworkType = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                        case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                        case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                            strNetworkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                            strNetworkType = "4G";
                            break;
                        default:
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if ("TD-SCDMA".equalsIgnoreCase(_strSubTypeName) || "WCDMA".equalsIgnoreCase(_strSubTypeName) || "CDMA2000".equalsIgnoreCase(_strSubTypeName)) {
                                strNetworkType = "3G";
                            } else {
                                strNetworkType = _strSubTypeName;
                            }

                            break;
                    }

                    Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
                }
            }

            Log.e("cocos2d-x", "Network Type : " + strNetworkType);
            NETWORK_TYPE = strNetworkType;
        } catch (Exception e) {
        }
        return strNetworkType;
    }

    /**
     * 获取运营商名字
     */
    public static String getOperatorName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        String operatorName = "";
        if (operator != null) {
            if ("46000".equals(operator) || "46002".equals(operator)) {
                operatorName = "中国移动";
            } else if ("46001".equals(operator)) {
                operatorName = "中国联通";
            } else if ("46003".equals(operator)) {
                operatorName = "中国电信";
            }
        }
        return operatorName;
    }
    /** ToolUtil **/

    /**
     * NetStateUtil
     **/
    public static boolean isWifyConnect(Context context) {

        return isTypeConnect(context, ConnectivityManager.TYPE_WIFI);
    }

    private static boolean isTypeConnect(Context context, int type) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo info = manager.getNetworkInfo(type);
            return info != null && info.isConnectedOrConnecting();
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            return false;
        }

    }

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return (info != null && info.isAvailable());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            return false;
        }

    }

}
