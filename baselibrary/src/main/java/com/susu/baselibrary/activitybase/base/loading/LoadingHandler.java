package com.susu.baselibrary.activitybase.base.loading;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.susu.baselibrary.BuildConfig;
import com.susu.baselibrary.R;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.ui.DisplayUtils;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.lifecycle.Lifecycle.Event.ON_DESTROY;

public class LoadingHandler implements LifecycleObserver, Handler.Callback, IShowAsyncLoading {
    public static final String TAG = LoadingHandler.class.getSimpleName();
    private static final int MSG_SHOW_DIALOG = 1;
    private static final int MSG_DISMISS_DIALOG = 2;
    private static final int MSG_CREATE = 3;

    private Handler mMainHandler = new Handler(Looper.getMainLooper(), this);
    private WeakReference<Activity> mWeakActivity;
    private volatile AppCompatDialog mDialog;

    private volatile AtomicInteger mShowCount = new AtomicInteger(0);
    private volatile AtomicInteger mDismissCount = new AtomicInteger(0);

    private volatile AtomicBoolean mInvokeCreate = new AtomicBoolean(false);

    private volatile Toast toast;
    private Context mContext;

    public LoadingHandler(Application application) {
        this.mContext = application;
    }

    /**
     * create方法在单页面内, 必须保证它在show/dismiss前被调用
     */
    @Override
    public void create(Activity context) {
        // loadingHandler在网络库使用的时候
        // 是以单例形式使用, 在每次请求的时候调用create
        // 那么存在, A页面跳转B页面, 关闭A页面的情况, 则B#onCreate执行在A#onDestory之前
        // 导致这个时候的dialog还是上个页面的dialog, 会引起崩溃
        // 所以需要判断当前传入的activity是否为之前的activity
        // 1. 如果之前的activity没有或者 当前传入的activity就是之前的activity
        // 则走默认的dialog初始化流程
        // 2. 如果传入的activity不是之前的activity
        // 则不管dialog是否为空, 都重新初始化
        WeakReference<Activity> oldWeak = mWeakActivity;
        mWeakActivity = new WeakReference<>(context);
        boolean isSameAct = isSameActivity(oldWeak);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            innerCreate(isSameAct);
        } else {
            mMainHandler.obtainMessage(MSG_CREATE, isSameAct).sendToTarget();
        }
    }

    /**
     * 当前传入的activity是否之前的activity
     *
     */
    private boolean isSameActivity(WeakReference<Activity> oldWeak) {
        return null == oldWeak || oldWeak.get() == mWeakActivity.get();
    }

    private void innerCreate(boolean isSameActivity) {
        mInvokeCreate.set(true);
        if (null == mWeakActivity || null == mWeakActivity.get()) {
            return;
        }
        bindLifeCycle();
        if (!isSameActivity || null == mDialog) {
            mDialog = new AppCompatDialog(mWeakActivity.get(), R.style.BaseCustomDialog);
            mDialog.setOnCancelListener(null);
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 主线程绑定生命周期
     */
    private void bindLifeCycle() {
        if (null == mWeakActivity || null == mWeakActivity.get()) {
            return;
        }
        Activity weak = mWeakActivity.get();
        if (weak instanceof LifecycleOwner) {
            ((LifecycleOwner) weak).getLifecycle().addObserver(this);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CREATE: {
                boolean isSameActivity = null != msg.obj && (boolean) msg.obj;
                innerCreate(isSameActivity);
                break;
            }
            case MSG_SHOW_DIALOG: {
                if (null != mDialog) {
                    boolean async = null != msg.obj && (boolean) msg.obj;
                    d("SHOW loading !!!!!!!!!!!");
                    try {
                        mDialog.setContentView(R.layout.base_devkit_loading_dialog);
                        mDialog.show();
                    } catch (Exception e) {
                        LogUtils.e(TAG, e.getMessage());
                    }
                }
                break;
            }
            case MSG_DISMISS_DIALOG: {
                if (null != mDialog) {
                    d("DISMISS loading !!!!!!!!!!!");
                    try {
                        mDialog.dismiss();
                    } catch (Exception e) {
                        LogUtils.e(TAG, e.getMessage());
                    }
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public void showAsyncLoading() {
        innerShowLoading(true);
    }

    @Override
    public void showLoading() {
        innerShowLoading(false);
    }

    /**
     * 内部执行loading
     *
     * @param sAsync 主要用来标记dialog布局
     */
    private void innerShowLoading(boolean sAsync) {
        log("invoke showLoading async" + sAsync);
        if (!hasInvokeCreate()) {
            LogUtils.e(TAG, "should run create first in one activity");
            return;
        }
        if (null == mDialog) {
            return;
        }
        mShowCount.incrementAndGet();
        log("showLoading showcount" + mShowCount.get());
        mMainHandler.obtainMessage(MSG_SHOW_DIALOG, sAsync).sendToTarget();
    }

    /**
     * 是否在show/dismiss之前调用过create
     *
     */
    private boolean hasInvokeCreate() {
        return mInvokeCreate.get();
    }

    @Override
    public void dismissLoading() {
        log("invoke dismiss loading");
        if (!hasInvokeCreate()) {
            LogUtils.e(TAG, "should run create first in one activity");
            return;
        }
        if (null == mDialog) {
            return;
        }
        mDismissCount.incrementAndGet();
        log("dismissLoading dismissCount" + mDismissCount.get() +
                "\n showCount " + mShowCount.get());
        if (mDismissCount.get() >= mShowCount.get()) {
            log("dismissloading");
            mMainHandler.obtainMessage(MSG_DISMISS_DIALOG).sendToTarget();

            mDismissCount.set(0);
            mShowCount.set(0);
        }
    }

    @Override
    public void showToast(int msgId) {
        showToast(mContext.getString(msgId));
    }

    @Override
    public void showToast(String msg) {
        final int height = DisplayUtils.getLcdHeight(mContext) / 3;
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, height);
        } else {
            toast.setText(msg);
        }
        try {
            toast.show();
        } catch (IllegalStateException e) {
            // no-op
        }
    }


    /**
     * 当绑定的activity释放的时候, 需要释放对应的dialog
     */
    @OnLifecycleEvent(ON_DESTROY)
    protected void releaseDialog() {
        if (null != mMainHandler) {
            // 移除所有队列上通知
            mMainHandler.removeCallbacksAndMessages(null);
        }
        if (null != mWeakActivity && null != mWeakActivity.get()) {
            ((LifecycleOwner) mWeakActivity.get()).getLifecycle().removeObserver(this);
        }
        clear();

        if (null == mDialog) {
            return;
        }
        if (mDialog.isShowing()) {
            log("releaseDialog invoke dismiss");
            mDialog.dismiss();
        }
        mDialog = null;
    }

    /**
     * 内部计数状态清零
     */
    private void clear() {
        mShowCount.set(0);
        mDismissCount.set(0);
        mInvokeCreate.set(false);
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG + ">>>", message);
        }
    }

    private void d(String msg) {
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG + ">>>", msg);
        }
    }
}
