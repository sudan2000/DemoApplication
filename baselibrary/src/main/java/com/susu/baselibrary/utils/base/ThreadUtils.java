package com.susu.baselibrary.utils.base;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class ThreadUtils {

    private static final String TAG = "ThreadUtils";

    private static ThreadPoolExecutor sThreadPoolExecutor;

    private static Handler sMainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * 获取当前可用的线程池对象
     */
    public static ThreadPoolExecutor getDefaultThreadPool() {
        if (null == sThreadPoolExecutor) {
            sThreadPoolExecutor = new ThreadPoolExecutor(
                    0,
                    Integer.MAX_VALUE,
                    60L,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    new MyThreadFactory());
            // 始终存在Ncpu 个线程，除非设置了允许CoreThread超时
            sThreadPoolExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
            // 设置空闲线程允许存活的时间，防止空闲线程过多导致资源浪费
            sThreadPoolExecutor.setKeepAliveTime(10L, TimeUnit.SECONDS);
        }
        return sThreadPoolExecutor;
    }

    public static Handler getMainHandler() {
        return sMainThreadHandler;
    }

    public static boolean isMainThread() {
        Thread thread = Thread.currentThread();
        return thread == Looper.getMainLooper().getThread();
    }

    /**
     * 执行任务
     *
     * @param task
     */
    public static void execute(Runnable task) {
        if (null == task) {
            return;
        }
        ThreadPoolExecutor executor = getDefaultThreadPool();
        if (null != executor) {
            try {
                executor.execute(task);
            } catch (Exception e) {
                LogUtils.d(TAG, "ThreadUtils execute error：" + e.getMessage());
            }
        }

    }

    public static Future submit(Runnable task) {
        if (null == task) {
            return null;
        }
        ThreadPoolExecutor executor = getDefaultThreadPool();
        if (null != executor) {
            try {
                return executor.submit(task);
            } catch (Exception e) {
                LogUtils.d(TAG, "ThreadUtils submit error：" + e.getMessage());
            }
        }
        return null;
    }

    public static void runOnMainThread(Runnable runnable) {
        sMainThreadHandler.post(runnable);
    }

    public static void runOnMainThread(Runnable runnable, long delayMillis) {
        sMainThreadHandler.postDelayed(runnable, delayMillis);
    }


    /**
     * 移除任务
     */
    public static void removeTask(Runnable task) {
        if (null == task) {
            return;
        }
        ThreadPoolExecutor executor = getDefaultThreadPool();
        if (null != executor) {
            try {
                executor.remove(task);
                executor.purge();
            } catch (Exception e) {
                LogUtils.d(TAG, "ThreadUtils removeTask error：" + e.getMessage());
            }
        }

    }


    private static class MyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public MyThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "MyThreadPool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
