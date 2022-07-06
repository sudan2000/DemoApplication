package com.susu.baselibrary.activitybase.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;

import androidx.annotation.Nullable;

import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.activitybase.base.loading.ILoadingHandler;
import com.susu.baselibrary.utils.ui.StatusBarUtils;

import java.lang.reflect.Method;


/**
 * Author : sudan
 * Time : 2020/12/10
 * Description:
 */
public class BaseActivity extends DevKitActivity {
    private static final String TAG = "BaseActivity";
    private ILoadingHandler mLoadingHandler;
    private OutLoginReceiver mOutLoginReceiver;
//    private VersionUpdateBroadcast versionUpdateBroadcast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();

    }

    /**
     * 初始化状态栏颜色
     */
    protected void setStatusBar() {
        StatusBarUtils.setSystemBarWhiteColor(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //监听版本过期广播
//        versionUpdateBroadcast = new VersionUpdateBroadcast(this);
//        versionUpdateBroadcast.register();

//        globalMsgBroadcast = new GlobalMsgBroadcast(this);
//        globalMsgBroadcast.register();

        //检查是否（已经）登陆超时，并监听登陆超时广播,仅若干特殊页面不需要
//        if(needCheckLoginTimeOut()){
//            loginTimeoutBroadcast = new LoginTimeoutBroadcast(this);
//            loginTimeoutBroadcast.checkLoginTimeOut();
//            loginTimeoutBroadcast.register();
//        }

        //标记App处于前台，isActive
//        MsgServiceManager.addPushMessageListener(mMsgFilter);

        // 基类里面实时检测存储权限
//        requestNecessaryPermission();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        config.densityDpi = getDefaultDisplayDensity();

        return super.getResources();
    }

    private int getDefaultDisplayDensity() {
        try {
            Class clazz = Class.forName("android.view.WindowManagerGlobal");
            Method method = clazz.getMethod("getWindowManagerService");
            method.setAccessible(true);
            Object iwm = method.invoke(clazz);
            Method getInitialDisplayDensity = iwm.getClass().getMethod("getInitialDisplayDensity", int.class);
            getInitialDisplayDensity.setAccessible(true);
            Object densityDpi = getInitialDisplayDensity.invoke(iwm, Display.DEFAULT_DISPLAY);
            return (int) densityDpi;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (mLoadingHandler != null) {
                mLoadingHandler.dismissLoading();
            }
        } catch (Throwable e) {
            LogUtils.e(TAG, e.getMessage(), e);
        }
        super.onDestroy();
    }


    public boolean isLogin() {
//        if (UserServiceManager.getInstance().getUserService() != null) {
//            return UserServiceManager.getInstance().getUserService().isLogin();
//        }
        return true;
    }

    public class OutLoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaseConstant.LOGIN_TIMEOUT_BROADCAST.equals(intent.getAction())) {
//                ActivityManagerDefault.getInstance().finishAllActivity();
            }
        }
    }

//    @Override
//    public ILoadingHandler getLoadingHandler() {
//        if (mLoadingHandler == null) {
//            mLoadingHandler = new WYTLoadingHandler(this);
//            mLoadingHandler.create(this);
//        }
//        return mLoadingHandler;
//    }

//    @Override
//    public void showLoading() {
//        if (!isFinishing()) {
//            getLoadingHandler().showLoading();
//        }
//    }

//    @Override
//    public void dismissLoading() {
//        if (getLoadingHandler() != null) {
//            getLoadingHandler().dismissLoading();
//        }
//    }

//    @Override
//    public void showToast(int msgId) {
//        WYToastUtils.show(this, msgId);
//    }
//
//    @Override
//    public void showToast(String msg) {
//        WYToastUtils.show(this, msg);
//    }
}
