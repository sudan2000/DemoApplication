package com.susu.baselibrary.view.customerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.susu.baselibrary.R;

import java.util.ArrayList;

/**
 * Created by yingjp on 2016/10/19.
 * 录音音量动画view
 */
public class AudioInputVoiceAnimateView extends View {
    public static final int DIRECTION_LEFT_TO_RIGHT = 1;
    public static final int DIRECTION_RIGHT_TO_LEFT = 2;
    private int mItemCount = 7;
    private float mItemWidth;
    private float mItemSpace;
    private float mItemMaxHeight;
    private float mItemMinHeight;
    private float mItemHeight;
    private int mItemColor;
    private int mDirection;
    private int mVoiceGrade = 5;

    private Paint mPaint;

    private ArrayList<Integer> mVoiceList = new ArrayList<>();

    public AudioInputVoiceAnimateView(Context context) {
        this(context, null);
    }

    public AudioInputVoiceAnimateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioInputVoiceAnimateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();
    }

    /**
     * 设置音量：1,2,3,4,5
     *
     * @param voice
     */
    public void setVoice(int voice) {
        if (voice < 1) {
            voice = 1;
        } else if (voice > mVoiceGrade) {
            voice = mVoiceGrade;
        }
        mVoiceList.add(0, voice);
        mVoiceList.remove(mVoiceList.size() - 1);
        invalidate();
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AudioInputVoiceAnimateView, defStyle, 0);
        mItemCount = a.getInt(R.styleable.AudioInputVoiceAnimateView_item_count, 7);
        mItemWidth = a.getDimension(R.styleable.AudioInputVoiceAnimateView_item_width,
                getDimen(R.dimen.base_audio_input_view_default_item_width));
        mItemSpace = a.getDimension(R.styleable.AudioInputVoiceAnimateView_item_space,
                getDimen(R.dimen.base_audio_input_view_default_item_space));
        mItemMaxHeight = a.getDimension(R.styleable.AudioInputVoiceAnimateView_item_max_height,
                getDimen(R.dimen.base_audio_input_view_default_item_max_height));
        mItemMinHeight = a.getDimension(R.styleable.AudioInputVoiceAnimateView_item_min_height,
                getDimen(R.dimen.base_audio_input_view_default_item_min_height));
        mItemColor = a.getColor(R.styleable.AudioInputVoiceAnimateView_item_color,
                getColor(R.color.color_orange));
        mDirection = a.getInt(R.styleable.AudioInputVoiceAnimateView_direction, 1);
        mVoiceGrade = a.getInt(R.styleable.AudioInputVoiceAnimateView_voice_grade, 5);
        mItemHeight = mItemMaxHeight - mItemMinHeight;
        initVoiceList();
        setSoftwareLayer();
        a.recycle();
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    private int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    private void setSoftwareLayer() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void initVoiceList() {
        mVoiceList = new ArrayList<>();
        for (int i = 0; i < mItemCount; i++) {
            mVoiceList.add(1);
        }
    }

    private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = 0, paddingEnd = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            paddingStart = getPaddingStart();
            paddingEnd = getPaddingEnd();
        }
        int maxPadding = Math.max(paddingLeft, Math.max(paddingTop,
                Math.max(paddingRight, Math.max(paddingBottom, Math.max(paddingStart, paddingEnd)))));
        setPadding(maxPadding, maxPadding, maxPadding, maxPadding);
    }

    private void initPaints() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mItemColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;
        for (int i = 0; i < mItemCount; i++) {
            float halfHeight = mItemHeight * mVoiceList.get(i) / (mVoiceGrade * 2f);//一半的高度
            if (mDirection == DIRECTION_LEFT_TO_RIGHT) {
                float xDeviation = i * (mItemWidth + mItemSpace);
                canvas.drawRect(xDeviation, centerY - halfHeight, xDeviation + mItemWidth, centerY + halfHeight, mPaint);
            } else {
                float xDeviation = getWidth() - i * (mItemWidth + mItemSpace);
                canvas.drawRect(xDeviation - mItemWidth, centerY - halfHeight, xDeviation, centerY + halfHeight, mPaint);
            }
        }
    }
}
