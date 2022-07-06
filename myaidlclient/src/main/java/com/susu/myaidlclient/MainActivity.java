package com.susu.myaidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.susu.demoapplication.IMyAidlInterface;

/**
 * Author : sudan
 * Time : 2021/11/15
 * Description:
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtNum1;
    private EditText mEtNum2;
    private TextView mTvResult;
    private IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_connect).setOnClickListener(this);
        findViewById(R.id.tv_get_result).setOnClickListener(this);
        mEtNum1 = findViewById(R.id.et_num1);
        mEtNum2 = findViewById(R.id.et_num2);
        mTvResult = findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_connect) {
            connect();

        } else if (view.getId() == R.id.tv_get_result) {
            getResult();

        }

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("aidl test---", "onServiceConnected()...");
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("aidl test---", "onServiceDisconnected()...");
            iMyAidlInterface = null;
        }
    };

    private void getResult() {
        int num1 = 0;
        int num2 = 0;
        try {
            num1 = Integer.parseInt(mEtNum1.getText().toString());
            num2 = Integer.parseInt(mEtNum2.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            int result = iMyAidlInterface.add(num1, num2);
            mTvResult.setText("" + num1 + " + " + num2 + " = " + result);
        } catch (RemoteException e) {
            Log.i("aidl test---", e.toString());
            mTvResult.setText("远程服务出错了...");
        }
    }

    private void connect() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.susu.myapplication", "com.susu.myapplication.aidl.MyAIDLService"));
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
