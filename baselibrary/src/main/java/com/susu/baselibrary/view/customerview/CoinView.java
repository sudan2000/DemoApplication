package com.susu.baselibrary.view.customerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.susu.baselibrary.R;
import com.susu.baselibrary.entity.CoinSignInfoEntity;
import com.susu.baselibrary.entity.ViewCoinSignEntity;
import com.susu.baselibrary.utils.ui.AnimUtils;
import com.susu.baselibrary.utils.ui.DisplayUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/4/30
 * Description: 签到收取金币view
 */
public class CoinView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "CoinView";

    private Context mContext;
    private NumberRunningTextView mTotalNumTv;
    private TextView mProgressBackTv, mProgressFontTv;
    private ImageView mProgressIconIv;
    private TextView mObtainTv;

    private TextView mSignTv, mSignTitleTv, mCanReceiveNumTv;
    private TextView mCoinTv7, signNumTv7;
    //    private View mAddNumLayout;
    private View mAddNumDesc;
    private LinearLayout mSignNumLLayout;
    private LinearLayout mSignLLayout; // 每天签到父layout
    private Drawable mInvalidCoinDrawable, mValidCoinDrawable, mSignedCoinDrawable;

    private boolean mHasSigned = false;
    private boolean mHasReceivedAll = false;
    private int mCanReceiveNum;
    private int mTotalNum;
    private int mTodaySignNum;
    private int mTotalProgressWidth;
    private Activity mActivity;


    public CoinView(Context context) {
        this(context, null, 0);
    }

    public CoinView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_coin, this);
        mObtainTv = view.findViewById(R.id.view_coin_obtain_tv); //健康币一键领取
        mTotalNumTv = view.findViewById(R.id.view_coin_total_num_tv);
//        mAddNumLayout = view.findViewById(R.id.view_coin_add_num_layout);
        mAddNumDesc = view.findViewById(R.id.view_icon_can_receive_num_desc);
        mCanReceiveNumTv = view.findViewById(R.id.view_coin_can_receive_num_tv);

        mProgressBackTv = view.findViewById(R.id.view_coin_progress_bg_tv);
        mProgressFontTv = view.findViewById(R.id.view_coin_progress_front_tv);
        mProgressIconIv = view.findViewById(R.id.view_coin_progress_icon_iv);

        mSignTitleTv = view.findViewById(R.id.view_coin_sign_title_tv);
        mSignNumLLayout = view.findViewById(R.id.view_coin_sign_num_layout);
        mSignLLayout = view.findViewById(R.id.view_coin_sign_layout);
        mSignTv = view.findViewById(R.id.view_coin_sign_tv);

        mCoinTv7 = view.findViewById(R.id.coinTv7);
        signNumTv7 = view.findViewById(R.id.signNumTv7);

        mObtainTv.setOnClickListener(this);
        mSignTv.setOnClickListener(this);

        mInvalidCoinDrawable = getResources().getDrawable(R.drawable.view_coin_sign_icon_gray);
        mValidCoinDrawable = getResources().getDrawable(R.drawable.view_coin_sign_icon_yellow);
        mSignedCoinDrawable = getResources().getDrawable(R.drawable.view_coin_icon_signed);

    }

    public void setHealthCoinCanReceiveData(int canReceiveNum) {
        if (canReceiveNum > 0) {
            mHasReceivedAll = false;
            mObtainTv.setText("一键领取");
            mAddNumDesc.setVisibility(View.VISIBLE);
            mCanReceiveNumTv.setVisibility(View.VISIBLE);
            mCanReceiveNumTv.setText(String.valueOf(canReceiveNum));
            mCanReceiveNum = canReceiveNum;
        } else {
            mHasReceivedAll = true;
            mObtainTv.setText("去兑换");
            mAddNumDesc.setVisibility(View.GONE);
            mCanReceiveNumTv.setVisibility(View.GONE);
        }
    }

    public void setHealthCoinSignData(ViewCoinSignEntity result) {
        if (result.todaySign) {
            invalidSignTv();
        }
        List<CoinSignInfoEntity> signList = result.mSignList;
        if (signList.size() < 7) {
            return;
        }
        String today = getMonthDotDay();
        int now = 6;
        for (int i = 0; i < 7; i++) {
            CoinSignInfoEntity entity = signList.get(i);
            if (today.equals(entity.signDate)) {
                now = i;
            }
            TextView tv = (TextView) mSignLLayout.getChildAt(i);
            if (i < now) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, mValidCoinDrawable, null, null);
                tv.setText(entity.signDate);
            } else if (i == now) {
                mTodaySignNum = entity.coinNum;
                tv.setCompoundDrawablesWithIntrinsicBounds(null, mValidCoinDrawable, null, null);
                tv.setText("今天");
            } else if (i == now + 1) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, mInvalidCoinDrawable, null, null);
                tv.setText("明天");
            } else {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, mInvalidCoinDrawable, null, null);
                tv.setText(entity.signDate);
            }

            TextView numTv = (TextView) mSignNumLLayout.getChildAt(i);
            numTv.setText("+" + entity.coinNum);
        }
    }


    private String getMonthDotDay() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        return month + "." + day;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 初始设置coin值
     *
     * @param originResult 初始值
     */
    public void setHealCoinData(int originResult) {
        mTotalNum = Math.max(originResult, 0);
        mTotalNumTv.setContent(String.valueOf(mTotalNum));
        mTotalNumTv.setOldContent(String.valueOf(mTotalNum));

        final float percent = calculatePercent(mTotalNum);
        ViewTreeObserver vto2 = mProgressBackTv.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mProgressBackTv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mTotalProgressWidth = mProgressBackTv.getMeasuredWidth();
                int finalFrontWidth = (int) (mTotalProgressWidth * percent);
                ViewGroup.LayoutParams lp = mProgressFontTv.getLayoutParams();
                lp.width = Math.max(finalFrontWidth, 1);
                mProgressFontTv.setLayoutParams(lp);
                mProgressIconIv.setTranslationX(finalFrontWidth - mProgressIconIv.getMeasuredWidth() / 2);

            }
        });
    }

    private void setTotalNum() {
        setProgressBar(mTotalNum);
        mTotalNumTv.setContent(String.valueOf(mTotalNum));
        mTotalNumTv.setOldContent(String.valueOf(mTotalNum));
    }

    public void setProgressBar(float value) {
        float percent = calculatePercent(value);
        final int finalFrontWidth = (int) (mTotalProgressWidth * percent);
        final ViewGroup.LayoutParams lp = mProgressFontTv.getLayoutParams();
        int originFontWidth = lp.width;
        float scaleF = (finalFrontWidth * 1.0f) / (originFontWidth * 1.0f);
        ScaleAnimation animation = new ScaleAnimation(1.0f, scaleF, 1.0f, 1.0F, 1, 0F, 1, 0F);
        animation.setDuration(400);
        animation.setFillAfter(false);
        mProgressFontTv.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lp.width = finalFrontWidth;
                mProgressFontTv.setLayoutParams(lp);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        int originIconX = Math.max(0, originFontWidth - mProgressIconIv.getMeasuredWidth() / 2);
        int finalIconX = Math.max(0, finalFrontWidth - mProgressIconIv.getMeasuredWidth() / 2);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(mProgressIconIv, "translationX",
                originIconX, finalIconX);
        translationXAnimator.setDuration(400);
        translationXAnimator.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_coin_obtain_tv) {
            if (!mHasReceivedAll) { // 一键领取 点击不领取签到的金币
                mTotalNum += mCanReceiveNum;
                mHasReceivedAll = true;
                animReceiveAll();
            } else { // 去兑换
            }
        } else if (v.getId() == R.id.view_coin_sign_tv) { //签到
            if (!mHasSigned) {
                mTotalNum += mTodaySignNum;
                invalidSignTv();
                animSign();
            }
        }
    }

    private void animReceiveAll() {
        //一键领取动画
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mCanReceiveNumTv, "scaleX", 1.0f, 1.1f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mCanReceiveNumTv, "scaleY", 1.0f, 1.1f, 1.0f);
        scaleXAnimator.setDuration(200);
        scaleYAnimator.setDuration(200);
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(mCanReceiveNumTv, "translationY",
                0, -DisplayUtils.dip2px(mContext, 12));
        translationYAnimator.setDuration(200);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(mCanReceiveNumTv, "translationX",
                0, -DisplayUtils.dip2px(mContext, 245));
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(3f);
        translationXAnimator.setInterpolator(decelerateInterpolator);
        translationXAnimator.setDuration(600);
        AnimatorSet.Builder builder = animatorSet.play(scaleXAnimator).with(scaleYAnimator).with(translationYAnimator);
        builder.before(translationXAnimator);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mObtainTv.setText("去兑换");
                mAddNumDesc.setVisibility(View.GONE);
                mCanReceiveNumTv.setVisibility(View.GONE);
                setTotalNum();

            }
        });
    }

    private void animSign() {
        //签到动画
        TextView anim1 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_coin_anim_view, (FrameLayout) mActivity.findViewById(android.R.id.content), false);

        AnimUtils.animViewToViewOnWindow(mActivity, anim1, mCoinTv7, mTotalNumTv, 1000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCoinTv7.setCompoundDrawablesWithIntrinsicBounds(null, mSignedCoinDrawable, null, null);
                setTotalNum();
            }
        });

        TextView anim2 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_coin_anim_num_view, (FrameLayout) mActivity.findViewById(android.R.id.content), false);
        anim2.setText("+"+ mTodaySignNum);
        AnimUtils.animViewToViewOnWindow(mActivity, anim2, signNumTv7, mTotalNumTv, 1000, new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                signNumTv7.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void invalidSignTv() {
        mSignTv.setBackgroundResource(R.drawable.view_coin_sign_tv_invalid_bg);
        mSignTv.setEnabled(false);
    }

    //计算健康币占比
    private float calculatePercent(float value) {
        float percent = 0;
        if (value < 0) {
            percent = 0;
        } else if (value >= 0 && value < 100) {
            percent = (float) ((value / 100) * 0.3);
        } else if (value >= 100 && value < 1000) {
            percent = (float) (((value - 100) / (1000 - 100)) * 0.35 + 0.3);
        } else if (value >= 1000 && value < 20000) {
            percent = (float) (((value - 1000) / (20000 - 1000)) * 0.2 + 0.65);
        } else if (value >= 20000 && value < 150000) {
            percent = (float) (((value - 20000) / (150000 - 20000)) * 0.15 + 0.85);
        } else {
            percent = 1;
        }
        return percent;
    }


}
