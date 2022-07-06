package com.susu.demoapplication.test.recyclerview;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.susu.baselibrary.activitybase.base.BaseActivity;
import com.susu.demoapplication.R;
import com.susu.baselibrary.recyclerview.decoration.DividerItemDecoration;

/**
 * Author : sudan
 * Time : 2021/2/3
 * Description:
 */
public class RecyclerViewActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRecyclerView = findViewById(R.id.recyclerView);
        listView = findViewById(R.id.listView);
        initRecyclerView();

    }

    private void initRecyclerView() {
        TestRecyclerViewAdapter adapter = new TestRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));

    }

    private void initListView(){

    }
}
