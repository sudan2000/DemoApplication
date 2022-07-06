package com.susu.baselibrary.view.customerview.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.susu.baselibrary.R;
import com.susu.baselibrary.image.HeadImageForUserTransformation;
import com.susu.baselibrary.utils.ui.DisplayUtils;
import com.susu.baselibrary.utils.ui.StatusBarUtils;

/**
 * Author : sudan
 * Time : 2021/5/8
 * Description:
 */
public class TitleBarView extends Toolbar implements View.OnClickListener {

    private Context mContext;
    private int mMinimumHeight;
    private LinearLayout mHeadInfoLayout;
    private View mFakeStatusBarView;
    private ImageView mBackgroundIv;
    private ImageView mPlaceholderView;

    //右上角设置
    private TextView mSettingsIv;

    //头像
    private RelativeLayout mPortraitLayout;
    public ImageView mPortraitIv;

    //个人信息
    private TextView mNameTv;
    private RelativeLayout mNameLayout;
    private ImageView mVipIconIv;

    private TextView mVipExchangeTv;

    //标题栏中间 “我“ 字
    private TextView mCenterInfoTv;

    private float percent;

    private HeadImageForUserTransformation mDecorator;

    private TextView mFamilyNumTv;

    public TitleBarView(Context context) {
        this(context, null, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_title_bar, this, false);
        addView(view);
        mFakeStatusBarView = findViewById(R.id.m_personal_home_me_fake_status_bar);
        mBackgroundIv = findViewById(R.id.m_personal_home_me_header_bg);
        mPlaceholderView = findViewById(R.id.personal_titlebar_empty_view);

        mSettingsIv = findViewById(R.id.setting);

        mVipExchangeTv = findViewById(R.id.m_home_personal_exchange_vip_tv);

        mPortraitIv = findViewById(R.id.m_personal_round_header_image);
        mPortraitLayout = findViewById(R.id.person_home_me_header_lay_portrait);

        mNameTv = findViewById(R.id.m_personal_header_info_name);
        mNameLayout = findViewById(R.id.layout_personal_info);
        mCenterInfoTv = findViewById(R.id.m_personal_home_me_title);

//        mPersonIconIv = findViewById(R.id.m_personal_check_icon_iv);
        mVipIconIv = findViewById(R.id.m_personal_vip_iv);

//        mDecorator = new HeadTransformationForUser(context);

        mHeadInfoLayout = findViewById(R.id.m_personal_page_header_info);

        mFamilyNumTv = findViewById(R.id.m_home_personal_info_family_count);

        mSettingsIv.setOnClickListener(this);
        mVipExchangeTv.setOnClickListener(this);
        mPortraitLayout.setOnClickListener(this);
        mNameLayout.setOnClickListener(this);
        mFamilyNumTv.setOnClickListener(this);

    }

    public void setStatusBar() {
        if (mMinimumHeight == 0) {
            mMinimumHeight = ViewCompat.getMinimumHeight(this);
        }
        if (StatusBarUtils.needDealStatusBar()) {
            StatusBarUtils.setTranslucentForImageView((Activity) mContext, mFakeStatusBarView);
            StatusBarUtils.setSystemBarTint((Activity) mContext, Color.TRANSPARENT, true);
            // 设置背景的高
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBackgroundIv.getLayoutParams();
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.m_personal_title_head_info_default_bg);
//            params.height = drawable.getIntrinsicHeight() + StatusBarUtils.getTopHighestHeight((Activity) mContext);
            mBackgroundIv.setLayoutParams(params);
            setMinimumHeight(mMinimumHeight + StatusBarUtils.getTopHighestHeight((Activity) mContext));
        }
        setHeaderMargin();
    }

    private void setHeaderMargin() {
        int minHeightOfToolBar = mMinimumHeight;
        int marginTopValue = (minHeightOfToolBar - DisplayUtils.dip2px(mContext, 30)) / 2;
        RelativeLayout.MarginLayoutParams layoutParams = (RelativeLayout.MarginLayoutParams) mSettingsIv.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, marginTopValue,
                layoutParams.rightMargin, layoutParams.bottomMargin);
        RelativeLayout.MarginLayoutParams lp = (RelativeLayout.MarginLayoutParams) mPortraitLayout.getLayoutParams();
        lp.setMargins(lp.leftMargin, DisplayUtils.dip2px(mContext, 8) + marginTopValue,
                lp.rightMargin, lp.bottomMargin);
    }

    @Override
    public void onClick(View v) {

    }
}
