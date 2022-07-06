package com.susu.demoapplication.test.recyclerview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.susu.baselibrary.recyclerview.decoration.DividerGridItemDecoration;
import com.susu.baselibrary.activitybase.base.BaseActivity;
import com.susu.demoapplication.R;

/**
 * Author : sudan
 * Time : 2021/11/23
 * Description: 问题：ScrollView中嵌套RecyclerView 在6.0以上系统中显示不全？
 *              还需验证：https://www.jianshu.com/p/3815d36fd371
 *              测试：暂时没发现
 */
public class ScrollRecyclerViewActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_recycler_view);
        mRecyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();

    }

    private void initRecyclerView() {
        TestRecyclerViewAdapter adapter = new TestRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

    }
}
