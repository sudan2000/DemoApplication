package com.susu.baselibrary.activitybase;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.susu.baselibrary.R;
import com.susu.baselibrary.activitybase.base.BaseActivity;


/**
 * Author : sudan
 * Time : 2020/12/14
 * Description:
 */
public abstract class BaseJavaActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_activity_root_container);
        getLayoutInflater().inflate(injectLayoutID(), (ViewGroup) findViewById(R.id.base_activity_root_container_fl), true);


        initView();
        initData();
    }

    public abstract void initView();

    public abstract void initData();


    public abstract @LayoutRes int injectLayoutID();

}
