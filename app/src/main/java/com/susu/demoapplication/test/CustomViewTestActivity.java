package com.susu.demoapplication.test;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.susu.baselibrary.entity.CoinSignInfoEntity;
import com.susu.baselibrary.entity.ViewCoinSignEntity;
import com.susu.baselibrary.view.customerview.CoinView;
import com.susu.baselibrary.view.customerview.NumberFlipView1;
import com.susu.baselibrary.view.customerview.textview.MyTextView;
import com.susu.baselibrary.activitybase.base.BaseActivity;
import com.susu.demoapplication.R;

import java.util.ArrayList;

/**
 * Author : sudan
 * Time : 2021/2/25
 * Description:
 */
public class CustomViewTestActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CustomViewTestActivity";

    private CoinView mCoinView;
    private NumberFlipView1 viewNumberFlip;
    private MyTextView myTextView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_view);

        mCoinView = findViewById(R.id.viewCoin);
        viewNumberFlip = findViewById(R.id.viewNumberFlip);
        myTextView = findViewById(R.id.myTextView);
        mProgressBar = findViewById(R.id.progress_bar);


        findViewById(R.id.tv1).setOnClickListener(this);

        setCoinView();
        setNumberFlipView();
        setProgressBar();

    }

    private void setProgressBar() {
        mProgressBar.setMax(100);
    }

    private void setNumberFlipView() {
        viewNumberFlip.setText("3478");
        viewNumberFlip.setSpeeds(NumberFlipView1.ALL);
        viewNumberFlip.start();
    }

    private void setCoinView() {
        mCoinView.setHealCoinData(500);
        mCoinView.setActivity(this);
        mCoinView.setHealthCoinCanReceiveData(288);
        ViewCoinSignEntity data = new ViewCoinSignEntity();
        data.todaySign = false;
        data.signedDay = 2;
        data.mSignList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            CoinSignInfoEntity info = new CoinSignInfoEntity();
            info.coinNum = 10 + i;
            info.isSigned = false;
            if (i < 5) {
                info.signDate = String.valueOf(4.25f + i * 0.01);
            } else {
                if (i == 5) {
                    info.signDate = "4.30";
                }
                if (i == 6) {
                    info.signDate = "5.1";
                }

            }

            data.mSignList.add(info);
        }
        mCoinView.setHealthCoinSignData(data);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv1) {
//            viewNumberFlip.setNumberFlip(400);
            viewNumberFlip.setText("3654");
            viewNumberFlip.setSpeeds(NumberFlipView1.ALL);
            viewNumberFlip.start();
        }

    }
}
