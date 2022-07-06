package com.susu.demoapplication.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.demoapplication.IMyAidlInterface;

/**
 * Author : sudan
 * Time : 2021/11/12
 * Description:
 */
public class MyAIDLService extends Service {

    private static final String TAG = "MyAIDLService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    private IBinder iBinder = new IMyAidlInterface.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            LogUtils.d(TAG, "收到客户端请求（num1：" + num1 + "  num2: " + num2 + "）");
            return num1 + num2;
        }
    };
}
