package com.susu.baselibrary.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    //饿汉式 保证速度
    private static final ThreadPoolManager mInstance = new ThreadPoolManager();

    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime;
    private TimeUnit mUnit;
    private ThreadPoolExecutor mExecutor;

    private ThreadPoolManager() {
        mCorePoolSize = Runtime.getRuntime().availableProcessors() + 1;
        //用不到，但是需要赋值，否则报错
        mMaximumPoolSize = mCorePoolSize;
        mKeepAliveTime = 1;
        mUnit = TimeUnit.MINUTES;
        mExecutor = new ThreadPoolExecutor(
                mCorePoolSize,
                mMaximumPoolSize,
                mKeepAliveTime,
                mUnit,
                new LinkedBlockingQueue<Runnable>(128),
                Executors.defaultThreadFactory()
        );
        // keepAliveTime同样作用于核心线程
        mExecutor.allowCoreThreadTimeOut(true);
    }

    public static ThreadPoolManager getInstance() {
        return mInstance;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) return;
        mExecutor.execute(runnable);
    }

    public void remove(Runnable runnable) {
        if (runnable == null) return;
        mExecutor.remove(runnable);
    }

}
