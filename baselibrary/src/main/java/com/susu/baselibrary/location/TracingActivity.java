package com.susu.baselibrary.location;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.susu.baselibrary.R;
import com.susu.baselibrary.location.model.CurrentLocation;
import com.susu.baselibrary.location.util.BitmapUtil;
import com.susu.baselibrary.location.util.CommonUtil;
import com.susu.baselibrary.location.util.MapUtil;
import com.susu.baselibrary.location.util.PermissionUtil;
import com.susu.baselibrary.location.util.SharedPreferenceUtil;
import com.susu.baselibrary.location.util.ViewUtil;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.system.CoreUtils;
import com.susu.baselibrary.utils.ui.DialogUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/11/29
 * Description:
 */
public class TracingActivity extends Activity implements View.OnClickListener {

    private NotificationManager notificationManager = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = null;

    private static final String ENTITY_NAME = "MyTrace";

    /**
     * 地图工具
     */
    private MapUtil mMapUtil = null;

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener mTraceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable = null;

    private boolean isRealTimeRunning = true;

    private int notifyId = 0;

    private List<LatLng> realTrackList = new ArrayList<>();


    /**
     * 打包周期
     */
    public int packInterval = LocationConstants.DEFAULT_PACK_INTERVAL;

    private LocationManager mLocationManager = LocationManager.getInstance(ENTITY_NAME);
    private TextView mTvTrace;
    private TextView mTvTraceStop;
    private TextView mTvGather;
    private TextView mTvGatherStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_activity_baidu_tracing);
        BitmapUtil.init();
        initView();
    }

    private void initView() {
        initListener();

        mMapUtil = MapUtil.getInstance();
        mMapUtil.init((MapView) findViewById(R.id.tracing_mapView));
        mMapUtil.setCenter();
        startRealTimeLoc(LocationConstants.LOC_INTERVAL);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        mTvTrace = findViewById(R.id.btn_trace);
        mTvTrace.setOnClickListener(this);
        mTvTraceStop = findViewById(R.id.btn_trace_stop);
        mTvTraceStop.setOnClickListener(this);
        mTvGather = findViewById(R.id.btn_gather);
        mTvGather.setOnClickListener(this);
        mTvGatherStop = findViewById(R.id.btn_gather_stop);
        mTvGatherStop.setOnClickListener(this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // 点击home键广播，由系统发出
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeAndLockReceiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_trace) {
            mLocationManager.createTrace();
            LogUtils.d("test---", "click button Trace-----");
            boolean isAccessPermission = SharedPreferenceUtil.isPrivacyAccept();
            if (!isAccessPermission) {
                ViewUtil.showPrivacyDialog(this);
            } else {
                if (!mLocationManager.isTraceStarted()) {
                    LogUtils.d("test---", "startTrace-----");
                    mLocationManager.onlyStartTrace(mTraceListener);
                    if (LocationConstants.DEFAULT_PACK_INTERVAL != packInterval) {
                        stopRealTimeLoc();
                        startRealTimeLoc(packInterval);
                    }
                }
            }

        } else if (view.getId() == R.id.btn_trace_stop) {
            LogUtils.d("test---", "click button trace stop-----");
            if (mLocationManager.isTraceStarted()) {
                LogUtils.d("test---", "isTraceStarted-----");
                mLocationManager.onlyStopTrace(mTraceListener);
                stopRealTimeLoc();
            }
        } else if (view.getId() == R.id.btn_gather) {
            LogUtils.d("test---", "click button gather-----");
            if (!mLocationManager.isGatherStarted()) {
                LogUtils.d("test---", "startGather-----");
                mLocationManager.onlyStartGather(mTraceListener);
            }
        } else if (view.getId() == R.id.btn_gather_stop) {
            LogUtils.d("test---", "click button gather stop-----");
            if (mLocationManager.isGatherStarted()) {
                LogUtils.d("test---", "isGatherStarted-----");
                mLocationManager.onlyStopGather(mTraceListener);
            }
        }
    }


    /**
     * 实时定位任务
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                mLocationManager.getCurrentLocation(entityListener, trackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    /**
     * 开始实时定位
     */
    public void startRealTimeLoc(int interval) {
        isRealTimeRunning = true;
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    /**
     * 停止实时定位
     */
    public void stopRealTimeLoc() {
        isRealTimeRunning = false;
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        mLocationManager.stopRealTimeLoc();
    }


    private void initListener() {
        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                LogUtils.d("test---", "onLatestPointCallback===response: " + response.toString());
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    return;
                }

                LatestPoint point = response.getLatestPoint();
                if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                        .getLongitude())) {
                    return;
                }

                LatLng currentLatLng = mMapUtil.convertTrace2Map(point.getLocation());

                if (null == currentLatLng) {
                    return;
                }
                realTrackList.add(currentLatLng);
                if (realTrackList != null && realTrackList.size() > 1) {
                    mMapUtil.drawPolyline(realTrackList);
                }
                CurrentLocation.locTime = point.getLocTime();
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mMapUtil) {
                    mMapUtil.updateStatus(currentLatLng, true);
                }
            }
        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                LogUtils.d("test---", "onReceiveLocation=== " + location.status + " -- " + location.getLatitude() + " -- " + location.getLongitude());
                if (StatusCodes.SUCCESS != location.getStatus() ||
                        CommonUtil.isZeroPoint(location.getLatitude(), location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mMapUtil.convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mMapUtil) {
                    mMapUtil.updateStatus(currentLatLng, true);
                }
            }
        };

        mTraceListener = new OnTraceListener() {

            /**
             * 绑定服务回调接口
             * @param errorNo  状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>1：失败</pre>
             */
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                LogUtils.d("test---", "onBindServiceCallback===errorNo: " + errorNo + "===message: " + message);
            }

            /**
             * 开启服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>10000：请求发送失败</pre>
             *                <pre>10001：服务开启失败</pre>
             *                <pre>10002：参数错误</pre>
             *                <pre>10003：网络连接失败</pre>
             *                <pre>10004：网络未开启</pre>
             *                <pre>10005：服务正在开启</pre>
             *                <pre>10006：服务已开启</pre>
             */
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                LogUtils.d("test---", "onStartTraceCallback===errorNo: " + errorNo + "===message: " + message);
                mTvTrace.setText(message);
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    mLocationManager.handleStartTraceSuccess();
                    registerReceiver();
                }
            }

            /**
             * 停止服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>11000：请求发送失败</pre>
             *                <pre>11001：服务停止失败</pre>
             *                <pre>11002：服务未开启</pre>
             *                <pre>11003：服务正在停止</pre>
             */
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                LogUtils.d("test---", "onStopTraceCallback===errorNo: " + errorNo + "===message: " + message);
                mTvTraceStop.setText(message);
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    mLocationManager.handleStopTraceSuccess();
                    unregisterPowerReceiver();
                }
            }

            /**
             * 开启采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>12000：请求发送失败</pre>
             *                <pre>12001：采集开启失败</pre>
             *                <pre>12002：服务未开启</pre>
             */
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                LogUtils.d("test---", "onStartGatherCallback===errorNo: " + errorNo + "===message: " + message);
                mTvGather.setText(message);
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    mLocationManager.handleStartGatherSuccess();
                }
            }

            /**
             * 停止采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             * 0：成功     13000：请求发送失败      13001：采集停止失败      13002：服务未开启
             */
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                LogUtils.d("test---", "onStopGatherCallback===errorNo: " + errorNo + "===message: " + message);
                mTvGatherStop.setText(message);
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    mLocationManager.handleStopGatherSuccess();
                }
            }

            /**
             * 推送消息回调接口
             *
             * @param messageType 状态码
             * @param pushMessage 消息
             * 0x01：配置下发        0x02：语音消息       0x03：服务端围栏报警消息      0x04：本地围栏报警消息
             * 0x05~0x40：系统预留   0x41~0xFF：开发者自定义
             */
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {
                LogUtils.d("test---", "onPushCallback===messageType: " + messageType + "===pushMessage: " + pushMessage);
                if (messageType < 0x03 || messageType > 0x04) {
                    ToastUtils.showToast(pushMessage.getMessage());
                    return;
                }
                FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
                if (null == alarmPushInfo) {
                    return;
                }
                StringBuffer alarmInfo = new StringBuffer();
                alarmInfo.append("您于")
                        .append(CommonUtil.getHMS(alarmPushInfo.getCurrentPoint().getLocTime() * 1000))
                        .append(alarmPushInfo.getMonitoredAction() == MonitoredAction.enter ? "进入" : "离开")
                        .append(messageType == 0x03 ? "云端" : "本地")
                        .append("围栏：").append(alarmPushInfo.getFenceName());

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    Notification notification = new Notification.Builder(CoreUtils.getApp())
                            .setContentTitle("百度鹰眼报警推送")
                            .setContentText(alarmInfo.toString())
                            .setSmallIcon(R.drawable.icon_baidu_trace)
                            .setWhen(System.currentTimeMillis()).build();
                    notificationManager.notify(notifyId++, notification);
                }
            }

            @Override
            public void onInitBOSCallback(int errorNo, String message) {
                LogUtils.d("test---", "onInitBOSCallback===errorNo: " + errorNo + "===message: " + message);
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data) {
            return;
        }

        if (data.hasExtra("locationMode")) {
            LocationMode locationMode = LocationMode.valueOf(data.getStringExtra("locationMode"));
            mLocationManager.setLocationMode(locationMode);
        }

        if (data.hasExtra("isNeedObjectStorage")) {
            boolean isNeedObjectStorage = data.getBooleanExtra("isNeedObjectStorage", false);
            mLocationManager.setNeedObjectStorage(isNeedObjectStorage);
        }

        if (data.hasExtra("gatherInterval") || data.hasExtra("packInterval")) {
            int gatherInterval = data.getIntExtra("gatherInterval", LocationConstants.DEFAULT_GATHER_INTERVAL);
            int packInterval = data.getIntExtra("packInterval", LocationConstants.DEFAULT_PACK_INTERVAL);
            TracingActivity.this.packInterval = packInterval;
            mLocationManager.setInterval(gatherInterval, packInterval);
        }

    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 注册广播（电源锁、GPS状态）
     */
    private void registerReceiver() {
        if (LocationManager.getInstance(ENTITY_NAME).isRegisterReceiver()) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myApplication:track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        CoreUtils.getApp().registerReceiver(trackReceiver, filter);
        mLocationManager.setIsRegisterReceiver(true);

    }

    private void unregisterPowerReceiver() {
        if (!mLocationManager.isRegisterReceiver()) {
            return;
        }
        if (null != trackReceiver) {
            CoreUtils.getApp().unregisterReceiver(trackReceiver);
        }
        mLocationManager.setIsRegisterReceiver(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        PermissionUtil.requestPermission(this);

        startRealTimeLoc(packInterval);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapUtil.onResume();
        PermissionUtil.requestBackgroundLocationPermission(this);
        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        powerWhiteList();
    }

    private void powerWhiteList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = CoreUtils.getApp().getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    /**
     * 监听是否点击了home键将客户端退到后台
     */
    private BroadcastReceiver mHomeAndLockReceiver = new BroadcastReceiver() {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String MESSAGE = " 根据相关法律法规规定，切换到后台后，若无必要可不必收集用户信息。";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (TextUtils.equals(reason, SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    if (mLocationManager.isGatherStarted()) {
                        ToastUtils.showToast(MESSAGE);
                    }
                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    if (mLocationManager.isGatherStarted()) {
                        ToastUtils.showToast(MESSAGE);
                    }
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mMapUtil.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRealTimeLoc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapUtil.clear();
        realTrackList.clear();
        realTrackList = null;
        unregisterReceiver(mHomeAndLockReceiver);
        stopRealTimeLoc();

        mLocationManager.onDestroy();
        BitmapUtil.clear();
    }

    @Override
    public void onBackPressed() {
        if (mLocationManager.isGatherStarted()) {
            DialogUtils.showCommonDialogCancelable(
                    this,
                    "提示",
                    getResources().getString(R.string.background_privacy_desc),
                    "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TracingActivity.super.onBackPressed();
                        }
                    }, "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mLocationManager.isGatherStarted()) {
                                mLocationManager.onlyStartGather(mTraceListener);
                                mLocationManager.onlyStopTrace(mTraceListener);
                                stopRealTimeLoc();
                                TracingActivity.super.onBackPressed();
                            }
                        }
                    }, false);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 设置服务按钮样式
     */
//    private void setTraceBtnStyle() {
//        boolean isTraceStarted = CommonUtil.getTrackConfig().getBoolean("is_trace_started", false);
//        if (isTraceStarted) {

//            traceBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color
//                    .white, null));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                traceBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_sure, null));
//            } else {
//                traceBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_sure, null));
//            }
//        } else {
//            traceBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.layout_title, null));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                traceBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_cancel, null));
//            } else {
//                traceBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_cancel, null));
//            }
//        }
//    }

    /**
     * 设置采集按钮样式
     */
//    private void setGatherBtnStyle() {
//        boolean isGatherStarted = CommonUtil.getTrackConfig().getBoolean("is_gather_started", false);
//        if (isGatherStarted) {
//            gatherBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                gatherBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_sure, null));
//            } else {
//                gatherBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_sure, null));
//            }
//        } else {
//            gatherBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.layout_title, null));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                gatherBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_cancel, null));
//            } else {
//                gatherBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.mipmap.bg_btn_cancel, null));
//            }
//        }
//    }


}
