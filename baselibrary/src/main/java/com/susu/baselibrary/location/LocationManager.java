package com.susu.baselibrary.location;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.susu.baselibrary.R;
import com.susu.baselibrary.location.util.CommonUtil;
import com.susu.baselibrary.location.util.NetUtil;
import com.susu.baselibrary.location.util.PermissionUtil;
import com.susu.baselibrary.location.util.SharedPreferenceUtil;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.StringUtils;
import com.susu.baselibrary.utils.system.CoreUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author : sudan
 * Time : 2021/11/29
 * Description: 百度鹰眼
 */
public class LocationManager {

    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private Notification mNotification = null;
    private boolean mIsRegisterReceiver = false;
    private LocRequest mLocRequest = null;
    private LBSTraceClient mClient = null; //轨迹客户端
    private Trace mTrace = null; //轨迹服务
    private String mEntityName = "myTrace";
    private boolean mIsTraceStarted = false; //服务是否开启标识
    private boolean mIsGatherStarted = false; //采集是否开启标识
    private StartTraceListener mStartTraceListener;
    private StopTraceListener mStopTraceListener;
    private static Map<String, LocationManager> mMap = new HashMap<>();

    public synchronized static LocationManager getInstance(String entityName) {
        LocationManager manager = mMap.get(entityName);
        if (manager != null) {
            return manager;
        }
        manager = new LocationManager(entityName);
        mMap.put(entityName, manager);
        return manager;
    }

    private LocationManager(String entityName) {
        mEntityName = entityName;
    }


    /**
     * 关闭服务同时关闭采集
     */
    public void stopTrace(StopTraceListener listener) {
        mStopTraceListener = listener;
        LogUtils.d("test---", "BaiduLocationManager stopTrace-----");
        if (mIsTraceStarted) {
            LogUtils.d("test---", "isTraceStarted-----");
            mClient.stopTrace(mTrace, mTraceListener);
        }
        if (mIsGatherStarted) {
            LogUtils.d("test---", "isGatherStarted-----");
            mClient.stopGather(mTraceListener);
        }
    }

    /**
     * 开启服务同时开启采集
     */
    public void startTrace(Activity activity, StartTraceListener listener) {
        mStartTraceListener = listener;
        createTrace();
        queryPermission(activity);

    }

    private void queryPermission(Activity activity) {
        PermissionUtil.requestPermission(activity);
        if (!mIsTraceStarted) {
            LogUtils.d("test---", "real startTrace-----");
            mClient.startTrace(mTrace, mTraceListener);
        }
    }

    public void createTrace() {
        if (mClient == null) {
            mClient = new LBSTraceClient(CoreUtils.getApp());
            mTrace = new Trace(LocationConstants.BAIDU_TRACK_SERVICE_ID, mEntityName);
            initNotification();
            mTrace.setNotification(mNotification);
            mLocRequest = new LocRequest(LocationConstants.BAIDU_TRACK_SERVICE_ID);
            clearTraceStatus();
        }
    }

    public boolean isRegisterReceiver() {
        return mIsRegisterReceiver;
    }

    public void setIsRegisterReceiver(boolean isRegisterReceiver) {
        mIsRegisterReceiver = isRegisterReceiver;
    }

    public void handleStopTraceSuccess() {
        mIsTraceStarted = false;
        mIsGatherStarted = false;
        // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
        SharedPreferenceUtil.removeTraceAndGather();

    }

    public void handleStopGatherSuccess() {
        mIsGatherStarted = false;
        SharedPreferenceUtil.removeGather();
    }

    public void handleStartTraceSuccess() {
        mIsTraceStarted = true;
        SharedPreferenceUtil.saveStartTrace();
    }

    public void handleStartGatherSuccess() {
        mIsGatherStarted = true;
        SharedPreferenceUtil.saveStartGather();
    }

    public void onlyStartTrace(OnTraceListener traceListener) {
        mClient.startTrace(mTrace, traceListener);
    }

    public void onlyStartGather(OnTraceListener traceListener) {
        mClient.startGather(traceListener);
    }

    public void onlyStopTrace(OnTraceListener traceListener) {
        mClient.stopTrace(mTrace, traceListener);
    }

    public void onlyStopGather(OnTraceListener traceListener) {
        mClient.stopGather(traceListener);
    }

    public void stopRealTimeLoc() {
        mClient.stopRealTimeLoc();
    }

    public boolean isTraceStarted() {
        return mIsTraceStarted;
    }

    public boolean isGatherStarted() {
        return mIsGatherStarted;
    }

    public void setLocationMode(LocationMode locationMode) {
        mClient.setLocationMode(locationMode);
    }

    public void setNeedObjectStorage(boolean needObjectStorage) {
        mTrace.setNeedObjectStorage(needObjectStorage);
    }

    public void setInterval(int gatherInterval, int packInterval) {
        mClient.setInterval(gatherInterval, packInterval);
    }


    private OnTraceListener mTraceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int errorNo, String message) {
            LogUtils.d("test---", "onBindServiceCallback===errorNo: " + errorNo + "===message: " + message);
        }

        @Override
        public void onStartTraceCallback(int errorNo, String message) {
            LogUtils.d("test---", "onStartTraceCallback===errorNo: " + errorNo + "===message: " + message);

            if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                mIsTraceStarted = true;
                SharedPreferenceUtil.saveStartTrace();
                if (!mIsGatherStarted) {
                    LogUtils.d("test---", "startGather-----");
                    mClient.startGather(mTraceListener);
                }
            } else {
                mIsTraceStarted = false;
                mIsGatherStarted = false;
                if (mStartTraceListener != null) {
                    mStartTraceListener.onTraceStartFailed();
                }
            }
        }

        @Override
        public void onStopTraceCallback(int errorNo, String message) {
            LogUtils.d("test---", "onStopTraceCallback===errorNo: " + errorNo + "===message: " + message);
            if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                mIsTraceStarted = false;
                mIsGatherStarted = false;
                // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                SharedPreferenceUtil.removeTraceAndGather();
            }
        }

        @Override
        public void onStartGatherCallback(int errorNo, String message) {
            LogUtils.d("test---", "onStartGatherCallback===errorNo: " + errorNo + "===message: " + message);
            if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                mIsGatherStarted = true;
                SharedPreferenceUtil.saveStartGather();
                if (mStartTraceListener != null) {
                    mStartTraceListener.onTraceStartSuccess();
                }
            } else {
                mIsTraceStarted = false;
                mIsGatherStarted = false;
                if (mStartTraceListener != null) {
                    mStartTraceListener.onTraceStartFailed();
                }
            }
        }

        @Override
        public void onStopGatherCallback(int errorNo, String message) {
            LogUtils.d("test---", "onStopGatherCallback===errorNo: " + errorNo + "===message: " + message);
            if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                handleStopGatherSuccess();
                if (mStopTraceListener != null) {
                    mStopTraceListener.onStopTraceSuccess();
                }
            } else {
                if (mStopTraceListener != null) {
                    mStopTraceListener.onStopTraceFailed();
                }
            }
        }

        @Override
        public void onPushCallback(byte messageType, PushMessage pushMessage) {
            LogUtils.d("test---", "onPushCallback===messageType: " + messageType + "===pushMessage: " + pushMessage);
        }

        @Override
        public void onInitBOSCallback(int errorNo, String message) {
            LogUtils.d("test---", "onInitBOSCallback===errorNo: " + errorNo + "===message: " + message);
        }
    };


    @TargetApi(16)
    public void initNotification() {
        if (mNotification != null) {
            return;
        }
        Notification.Builder builder = new Notification.Builder(CoreUtils.getApp());
        Intent notificationIntent = new Intent(CoreUtils.getApp(), TracingActivity.class);

        Bitmap icon = BitmapFactory.decodeResource(CoreUtils.getApp().getResources(), R.drawable.icon_tracing);

        NotificationManager notificationManager = (NotificationManager) CoreUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(CoreUtils.getApp(), 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent) // 设置PendingIntent
                .setLargeIcon(icon)  // 设置下拉列表中的图标(大图标)
                .setContentTitle("百度鹰眼") // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.icon_tracing) // 设置状态栏内的小图标
                .setContentText("服务正在运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && null != notificationManager) {
            NotificationChannel notificationChannel =
                    new NotificationChannel("trace", "trace_channel",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

            builder.setChannelId("trace"); // Android O版本之后需要设置该通知的channelId
        }

        mNotification = builder.build(); // 获取构建好的Notification
        mNotification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }

    public void onDestroy() {
        CommonUtil.saveCurrentLocation();
        if (CommonUtil.getTrackConfig().contains("is_trace_started")
                && CommonUtil.getTrackConfig().getBoolean("is_trace_started", true)) {
            // 退出app停止轨迹服务时，不再接收回调，将OnTraceListener置空
            mClient.setOnTraceListener(null);
            mClient.stopTrace(mTrace, null);
        } else {
            mClient.clear();
        }
        clearTraceStatus();
    }


    /**
     * 获取当前位置
     */
    public void getCurrentLocation(OnEntityListener entityListener,
                                   OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        SharedPreferences trackConf = CommonUtil.getTrackConfig();
        if (NetUtil.isNetworkAvailable(CoreUtils.getApp())
                && trackConf.contains("is_trace_started")
                && trackConf.contains("is_gather_started")
                && trackConf.getBoolean("is_trace_started", false)
                && trackConf.getBoolean("is_gather_started", false)) {

            LatestPointRequest request = new LatestPointRequest(getTag(), LocationConstants.BAIDU_TRACK_SERVICE_ID, mEntityName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            LogUtils.d("test---", "mClient.queryLatestPoint---------");
            mClient.queryLatestPoint(request, trackListener);
        } else {
            LogUtils.d("test---", "mClient.queryRealTimeLoc---------");
            mClient.queryRealTimeLoc(mLocRequest, entityListener);
        }
    }


    /**
     * 清除Trace状态：初始化app时，判断上次是正常停止服务还是强制杀死进程，根据trackConf中是否有is_trace_started字段进行判断。
     * <p>
     * 停止服务成功后，会将该字段清除；若未清除，表明为非正常停止服务。
     */
    public void clearTraceStatus() {
        SharedPreferences trackConf = CommonUtil.getTrackConfig();
        if (trackConf.contains("is_trace_started") || trackConf.contains("is_gather_started")) {
            SharedPreferences.Editor editor = trackConf.edit();
            editor.remove("is_trace_started");
            editor.remove("is_gather_started");
            editor.apply();
        }
        mIsTraceStarted = false;
        mIsGatherStarted = false;
    }

    /**
     * 获取请求标识
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    private void initCustomData() {
        mClient.setOnCustomAttributeListener(new OnCustomAttributeListener() {
            @Override
            public Map<String, String> onTrackAttributeCallback() {
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }

            @Override
            public Map<String, String> onTrackAttributeCallback(long locTime) {
                System.out.println("onTrackAttributeCallback, locTime : " + locTime);
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }
        });
    }

}
