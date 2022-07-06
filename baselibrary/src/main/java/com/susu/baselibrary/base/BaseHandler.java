package com.susu.baselibrary.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class BaseHandler<T> extends Handler {

    private WeakReference<T> mReference;

    public BaseHandler(T target) {
        mReference = new WeakReference<>(target);
    }

    @Override
    public void handleMessage(Message msg) {
        T target = mReference.get();
        if (target == null) {
            return;
        }
        handleMessage(msg, target);
    }

    public abstract void handleMessage(Message msg, T target);
}
