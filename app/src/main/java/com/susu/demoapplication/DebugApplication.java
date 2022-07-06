package com.susu.demoapplication;

import android.app.Application;


import com.baidu.mapapi.SDKInitializer;

import okhttp3.EventListener;

public class DebugApplication extends MainApplication {

    private EventListener.Factory eventListenerFactory;


    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }


    public Application getApplication() {
        return this;
    }
}
