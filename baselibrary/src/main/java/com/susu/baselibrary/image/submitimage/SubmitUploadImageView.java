package com.susu.baselibrary.image.submitimage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.recyclerview.layoutmanager.FullyGridLayoutManager;

/**
 * Author : sudan
 * Time : 2021/11/22
 * Description: 上传图片控件
 */
public class SubmitUploadImageView extends LinearLayout {

    private static final String TAG = "SubmitUploadImageView";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private View mLlAddImg;

    public SubmitUploadImageView(Context context) {
        this(context, null, 0);
    }

    public SubmitUploadImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubmitUploadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.base_view_submit_upload_image, this, true);
        mRecyclerView = findViewById(R.id.rv_image_upload);
        mLlAddImg = findViewById(R.id.ll_add_image);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
    }
}
