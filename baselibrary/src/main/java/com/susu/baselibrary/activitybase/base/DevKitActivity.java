package com.susu.baselibrary.activitybase.base;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.susu.baselibrary.sharelogin.ShareLoginManager;
import com.susu.baselibrary.activitybase.base.loading.ILoadingHandler;
import com.susu.baselibrary.activitybase.base.loading.LoadingHandler;


public class DevKitActivity extends AppCompatActivity {

    private static final String TAG = "DevKitActivity";

    private static final int INVALID_DENSITY = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideBottomUIMenu();
        injectMembers();
        injectContentView();
        defaultHideActionBar();
        ShareLoginManager.onActivityResultData(this, this.getIntent());
//        if (needObserveNetwork()) {
//            WYNetworkChangeObserver.registerObserver(this);
//        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        this.proxy.build(intent);
        ShareLoginManager.onActivityResultData(this, intent);
    }

    public void onContentChanged() {
        super.onContentChanged();
        injectViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (needCheckPhoneState()) {
//            PhoneStateListenerManager.getInstance(this).registerPhoneListener(this);
        }

        if (needCheckSdcard()) {
//            sdcardStateReceiver = new SdcardStateReceiver(this);
//            sdcardStateReceiver.checkSdcard();
//            sdcardStateReceiver.register();
        }

//        NetworkChangeManager.getInstance().register(this);
//        CustomBroadcastReceiverManager.getInstance().register();
//        registerNetwork();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        DisplayUtils.resetFontScaleToStandardMode(this, getWindowManager());

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (sdcardStateReceiver != null) {
//            sdcardStateReceiver.unregister();
//        }
//
//        if (needCheckPhoneState()) {
//            PhoneStateListenerManager.getInstance(this).unregisterPhoneListener(this);
//        }
//
//        NetworkChangeManager.getInstance().unregister(this);
//
//        //自定义广播注销
//        CustomBroadcastReceiverManager.getInstance().unRegister();
//
//        if (mNetworkBroadcast != null){
//            mNetworkBroadcast.unregister();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected boolean needCheckPhoneState() {
        return false;
    }

    protected boolean needCheckSdcard() {
        return false;
    }

    private void hideBottomUIMenu() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏虚拟按键，并且全屏，隐藏system bars
        try {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    // 隐藏status bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    /*
                     * 隐藏system bars，提供沉浸体验
                     * 确保SYSTEM_UI_FLAG_HIDE_NAVIGATION不会在用户交互的时候被强制清除，
                     * 确保SYSTEM_UI_FLAG_FULLSCREEN不会在用户从屏幕上方往下滑动的时候被强制清除
                     */
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        } catch (Exception e) {
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        changeStatusBarColor();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        changeStatusBarColor();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        changeStatusBarColor();
    }

    /**
     * 修改状态栏配色
     */
    protected void changeStatusBarColor() {

//        WYStatusBarUtils.setSystemBarTint(this,getResources()
//                .getColor(R.color.gh_cm_bg_color));
    }


    /**
     * 是否需要监听网络变化
     * <p>
     * 默认不需要监听网络变化
     *
     * @return {@code false}不需要监听网络变化 {@code true}需要监听网络变化
     */
    protected boolean needObserveNetwork() {
        return false;
    }


    public boolean isLogin() {
//        IUserService userService = UserServiceManager.getInstance().getUserService();
//        return  userService != null && userService.isLogin();
        return false;
    }

    private void defaultHideActionBar() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 打点 from 2.4.2
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 是否等待数据加载完，再发送页面埋点消息
     */
    public boolean onPageTrackerAfterLoading() {
        return false;
    }

    /**
     * 页面埋点的数据
     */
    public String onPageTrackerWithMsg() {
        return null;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        WYPermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public Resources getResources() {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        return super.getResources();
    }


    protected void injectViews() {
    }

    protected void injectContentView() {
    }

    protected void injectMembers() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == IRouterListener.requestCode && ending != null && ending.getCallback() != null) {
//            ending.getCallback().callBack(this, data);
//            ending = null;
//        }
        ShareLoginManager.onActivityResult(requestCode, resultCode, data);
        ShareLoginManager.onActivityResultData(this, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    ///////////////////////////////////////////////////////////////////////////
    // loading
    ///////////////////////////////////////////////////////////////////////////
    private ILoadingHandler mLoadingHandler;

    public ILoadingHandler getLoadingHandler(){
        if (mLoadingHandler == null){
            mLoadingHandler = new LoadingHandler(getApplication());
            mLoadingHandler.create(this);
        }
        return mLoadingHandler;
    }

    public void showLoading() {
        if (getLoadingHandler() != null){
            getLoadingHandler().showLoading();
        }
    }

    public void dismissLoading() {
        if (getLoadingHandler() != null) {
            getLoadingHandler().dismissLoading();
        }
    }

    public void showToast(int msgId) {
        if (getLoadingHandler() != null) {
            getLoadingHandler().showToast(msgId);
        }
    }

    public void showToast(String msg) {
        if (getLoadingHandler() != null) {
            getLoadingHandler().showToast(msg);
        }
    }

    public boolean supportAsyncLoading() {
        return false;
    }
}
