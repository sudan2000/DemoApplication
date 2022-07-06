package com.susu.demoapplication;

import android.app.Application;

import com.susu.baselibrary.utils.system.CoreUtils;


public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CoreUtils.init(this);

    }


}
