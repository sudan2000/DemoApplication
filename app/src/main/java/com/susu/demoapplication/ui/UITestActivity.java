package com.susu.demoapplication.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.susu.baselibrary.activitybase.base.BaseActivity;
import com.susu.demoapplication.R;

/**
 * Author : sudan
 * Time : 2021/12/12
 * Description:
 */
public class UITestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_test);
    }
}
