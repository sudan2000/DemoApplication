package com.susu.demoapplication.test.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.susu.baselibrary.base.BaseHandler;
import com.susu.baselibrary.utils.base.CollectionUtils;
import com.susu.baselibrary.utils.ui.DisplayUtils;
import com.susu.baselibrary.utils.base.StringUtils;
import com.susu.baselibrary.view.customerview.NumberRunningTextView;
import com.susu.baselibrary.activitybase.base.BaseActivity;
import com.susu.demoapplication.R;
import com.susu.demoapplication.test.presenter.BeautyNoReportManagerPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/4/30
 * Description:
 */
public class AnimActivity extends BaseActivity implements View.OnClickListener, BeautyNoReportManagerPresenter.IView {
    private static final int GET_PEOPLE_EXPERIENCE = 1;
    private static final int TIME_GET_PEOPLE_EXPERIENCE = 3000;

    private int RQ_NO_REPORT = this.hashCode();
    private NumberRunningTextView mTvPeopleExperienceNum;
    private MyHandler mHandler = new MyHandler(this);
    private long mOldPeopleExperienceNum;
    private TextView mTvExperienceBefore;
    private BeautyNoReportManagerPresenter mPresenter;
    private TextView mTvProvince;
    private TextView mTvDesc1;
    private TextView mTvDesc2;
    private TextView mTvDesc3;
    private List<TextView> mTvDescs;
    private boolean mIsFirstGetCount = true;
    private boolean mStewardLoaded;
    private boolean mCountLoaded;
    private BeautyNoReportManagerPresenter.BeautyStewardNoReportVO mBeautyStewardVo;
    private int mTotalCount;
    private LottieAnimationView mIvBackground;
    private ArrayList<List<Desc>> mDescLists;
    private TextView mTvExperienceAfter;
    private boolean mHasSuccess;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class MyHandler extends BaseHandler<AnimActivity> {
        MyHandler(AnimActivity target) {
            super(target);
        }

        @Override
        public void handleMessage(Message msg, AnimActivity target) {
            switch (msg.what) {
                case GET_PEOPLE_EXPERIENCE:
                    target.getPeopleExperienceNum();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mIvBackground = findViewById(R.id.iv_background);
        mTvProvince = findViewById(R.id.tv_province);
        mTvDesc1 = findViewById(R.id.tv_desc1);
        mTvDesc2 = findViewById(R.id.tv_desc2);
        mTvDesc3 = findViewById(R.id.tv_desc3);
        mTvDescs = new ArrayList<>();
        mTvDescs.add(mTvDesc1);
        mTvDescs.add(mTvDesc2);
        mTvDescs.add(mTvDesc3);
        mTvPeopleExperienceNum = findViewById(R.id.tv_people_experience);
        mTvExperienceBefore = findViewById(R.id.tv_people_experience_before);
        mTvExperienceAfter = findViewById(R.id.tv_experience_after);
        findViewById(R.id.v_try_now).setOnClickListener(this);
        initView();
        requestData();
    }

    public void initView() {
        mIvBackground.enableMergePathsForKitKatAndAbove(true);
        mIvBackground.useHardwareAcceleration(true);
        mIvBackground.setImageAssetsFolder("noreport/images");
        LottieComposition.Factory.fromAssetFileName(this,
                "noreport/data.json", new OnCompositionLoadedListener() {
                    @Override
                    public void onCompositionLoaded(LottieComposition composition) {
                        if (composition != null) {
                            mIvBackground.loop(true);
                            mIvBackground.setComposition(composition);
                            mIvBackground.resumeAnimation();
                        }
                    }
                });
    }


    private void requestData() {
        if (mPresenter == null) {
            mPresenter = new BeautyNoReportManagerPresenter(this);

        }
        mPresenter.getProvinceCode();
        mPresenter.getSkinDetectCount();
    }

    private void getPeopleExperienceNum() {
        mPresenter.getSkinDetectCount();
    }

    @Override
    public void onStewardLoadSuccess(BeautyNoReportManagerPresenter.BeautyStewardNoReportVO data) {
        mStewardLoaded = true;
        mBeautyStewardVo = data;
        showContentView();
    }

    @Override
    public void onStewardLoadFailed() {
    }

    @Override
    public void onSkinDetectCountSuccess(int count) {
        mCountLoaded = true;
        mTotalCount = count;
        if (mIsFirstGetCount) {
            mIsFirstGetCount = false;
            showContentView();
        } else {
            changePeopleExperienceNum();
        }
    }

    @Override
    public void onSkinDetectCountFailed() {

    }

    private void changePeopleExperienceNum() {
        if (mTotalCount == 0 || mBeautyStewardVo == null || mBeautyStewardVo.getPercent() == 0) {
            return;
        }
        int peopleNum = (int) (mTotalCount * mBeautyStewardVo.getPercent() + 1);
        if (peopleNum != mOldPeopleExperienceNum) {
            if (!CollectionUtils.isEmpty(mDescLists)) {
                List<Desc> descs = mDescLists.get(0);
                if (!CollectionUtils.isEmpty(descs) && descs.size() == 3) {
                    Desc desc = descs.get(1);
                    if (desc != null) {
                        desc.text = String.valueOf(peopleNum - 1);
                        desc.numEndIndex = desc.numStartIndex + desc.text.length();
                        SpannableString spannableString = new SpannableString(desc.text);
                        spannableString.setSpan(new AbsoluteSizeSpan(32, true), desc.numStartIndex, desc.numEndIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                        spannableString.setSpan(new CalligraphyTypefaceSpan(MeipuTypefaceUtil.loadAvenvirHeavyTypeface(getContext())),
//                                desc.numStartIndex, desc.numEndIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        desc.text = spannableString;
                    }
                }
            }
            mTvPeopleExperienceNum.setOldContent(String.valueOf(mOldPeopleExperienceNum));
            mTvPeopleExperienceNum.setContent(String.valueOf(peopleNum));
            mOldPeopleExperienceNum = peopleNum;
        }

        Message message = Message.obtain();
        message.what = GET_PEOPLE_EXPERIENCE;
        mHandler.sendMessageDelayed(message, TIME_GET_PEOPLE_EXPERIENCE);
    }

    @Override
    public void onClick(View v) {

    }


    private void showContentView() {
        if (!mCountLoaded || !mStewardLoaded) {
            return;
        }
        mHasSuccess = true;
        mTvExperienceBefore.setVisibility(View.VISIBLE);
        mTvExperienceAfter.setVisibility(View.VISIBLE);
        setProvince();
        changePeopleExperienceNum();
        showDesc();
    }

    private void showDesc() {
        if (mTotalCount == 0 || mBeautyStewardVo == null || mBeautyStewardVo.getPercent() == 0
                || mBeautyStewardVo.getContent() == null || mBeautyStewardVo.getContent().isEmpty()) {
            return;
        }
        int count = (int) (mTotalCount * mBeautyStewardVo.getPercent());
        String desc1 = getString(R.string.beauty_skin_detect_count_now, count);
        mDescLists = new ArrayList<>();
        mDescLists.add(getDescList(desc1));
        for (String content : mBeautyStewardVo.getContent()) {
            if (!StringUtils.isEmpty(content)) {
                List<Desc> descList = getDescList(content);
                if (descList != null) {
                    mDescLists.add(descList);
                }
            }
        }
        startDescAnim(0);
    }


    private List<Desc> getDescList(String descString) {
        if (StringUtils.isEmpty(descString)) {
            return null;
        }
        List<Desc> descs = new ArrayList<>();
        String[] texts = descString.split("\n");
        for (String string : texts) {
            Desc desc = new Desc();
            desc.text = string;
            if (string.matches("[\\s\\S]*[0-9]+%*[\\s\\S]*")) {
                desc.hasNum = true;
                String[] strings = string.split("[0-9]+%*");
                if (strings.length == 0) {
                    desc.numStartIndex = 0;
                    desc.numEndIndex = string.length();
                } else {
                    if (strings.length == 2) {
                        desc.numStartIndex = strings[0].length();
                        desc.numEndIndex = string.length() - strings[0].length() - strings[1].length() + desc.numStartIndex;
                    }
                }
                SpannableString spannableString = new SpannableString(desc.text);
                spannableString.setSpan(new AbsoluteSizeSpan(32, true), desc.numStartIndex, desc.numEndIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                spannableString.setSpan(new CalligraphyTypefaceSpan(MeipuTypefaceUtil.loadAvenvirHeavyTypeface(getContext())),
//                        desc.numStartIndex, desc.numEndIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                desc.text = spannableString;
            }
            descs.add(desc);
        }
        return descs;
    }

    private void startDescAnim(int index) {
        if (CollectionUtils.isEmpty(mDescLists)) {
            return;
        }
        if (index >= mDescLists.size()) {
            index = 0;
        }

        List<Desc> descs = mDescLists.get(index);
        for (int i = 0; i < mTvDescs.size(); ++i) {
            final TextView textView = mTvDescs.get(i);
            textView.setVisibility(View.INVISIBLE);
            if (i >= descs.size()) {
                continue;
            }
            Desc desc = descs.get(i);
            textView.setText(desc.text);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
            alphaAnimator.setDuration(400);
            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(textView, "translationY", 0, -DisplayUtils.dip2px(AnimActivity.this, 13));
            translationAnimator.setDuration(400);
            final AnimatorSet animatorSet = new AnimatorSet();
            AnimatorSet.Builder builder = animatorSet.play(alphaAnimator).with(translationAnimator);
            if (desc.hasNum) {
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(textView, "scaleX", 1.0f, 1.1f, 1.0f);
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(textView, "scaleY", 1.0f, 1.1f, 1.0f);
                scaleXAnimator.setDuration(400);
                scaleYAnimator.setDuration(400);
                builder.with(scaleXAnimator).with(scaleYAnimator);
            }
            builder.after(400 * i);
            textView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (textView != null) {
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }, 400 * i);
            if (i == descs.size() - 1) {
                final int finalIndex = index;
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animatorSet.removeAllListeners();
                        int hideIndex = 0;
                        for (TextView textView : mTvDescs) {
                            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f);
                            alphaAnimator.setDuration(200);
                            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(textView, "translationY", -DisplayUtils.dip2px(AnimActivity.this, 13), 0f);
                            translationAnimator.setDuration(200);
                            final AnimatorSet animatorSet1 = new AnimatorSet();
                            animatorSet1.play(alphaAnimator).with(translationAnimator).after(600);
                            if (hideIndex == 0) {
                                animatorSet1.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        animatorSet1.removeAllListeners();
                                        startDescAnim(finalIndex + 1);
                                    }
                                });
                            }
                            animatorSet1.start();
                            hideIndex++;
                        }
                    }
                });
            }
            animatorSet.start();
        }
    }

    private void setProvince() {
        if (mBeautyStewardVo == null) {
            return;
        }
        String provinceText;
        if (StringUtils.isEmpty(mBeautyStewardVo.getProvince())) {
            provinceText = "全国";
        } else {
            provinceText = mBeautyStewardVo.getProvince();
        }
        mTvProvince.setText(provinceText);
    }


    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(GET_PEOPLE_EXPERIENCE);
    }


    private class Desc {
        CharSequence text;
        boolean hasNum;
        int numStartIndex;
        int numEndIndex;
    }


}
