package com.susu.baselibrary.speech.upper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.utils.base.StringUtils;
import com.susu.baselibrary.view.customerview.AudioInputVoiceAnimateView;

/**
 * 语音识别动画
 */
public class ASRDialogView extends RelativeLayout {
    private Context mContext;
    private TextView mTopStatusTv;
    private AudioInputVoiceAnimateView mLeftView;
    private AudioInputVoiceAnimateView mRightView;

    public ASRDialogView(Context context) {
        super(context);
        initView(context);
    }

    public ASRDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ASRDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setStatus(String text) {
        if (StringUtils.isNull(text)) {
            return;
        }
        mTopStatusTv.setText(text);
    }

    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.base_asr_view, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, params);
        mTopStatusTv = view.findViewById(R.id.asr_layout_toptext);
        mLeftView = view.findViewById(R.id.asr_animate_left);
        mRightView = view.findViewById(R.id.asr_animate_right);
    }

    // 显示录音时间和动画
    public void showRecordTimeAndAnimate(int voiceValue) {
        int voice = 1;
        if (voiceValue < 11) {
            voice = 1;
        } else if (voiceValue < 22) {
            voice = 2;
        } else if (voiceValue < 33) {
            voice = 3;
        } else if (voiceValue < 44) {
            voice = 4;
        } else if (voiceValue < 55) {
            voice = 5;
        } else if (voiceValue < 66) {
            voice = 6;
        } else if (voiceValue < 77) {
            voice = 7;
        } else {
            voice = 8;
        }
        mLeftView.setVoice(voice);
        mRightView.setVoice(voice);
    }

}
