package com.susu.baselibrary.view.customerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.susu.baselibrary.utils.ui.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/5/10
 * Description:
 */
public class NumberFlipView extends View {

    private Context mContext;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private int mFlipNumber = 1;
    private int mOutterFlipNumber = mFlipNumber;
    private Rect textRect = new Rect();
    private float mMaxMoveHeight;
    private float mCurrentMoveHeight;
    private float mOutterMoveHeight;
    private float mCurrentAlphaValue;

    private List<String> flipNumberList = new ArrayList<>();
    private List<String> flipOutterNumberList = new ArrayList<>();

    public NumberFlipView(Context context) {
        this(context, null, 0);
    }

    public NumberFlipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberFlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        paint.setColor(Color.WHITE);
        int fontSize = DisplayUtils.sp2px(mContext, 14);
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.STROKE);
        mMaxMoveHeight = DisplayUtils.dip2px(mContext, 10);
    }

    public void setNumberWithoutFlip(int number) {
        mOutterFlipNumber = number;
        mFlipNumber = number;
        invalidate();
    }

    public void setNumberFlip(int number) {
        if (mFlipNumber == number) {
            return;
        }
        mOutterFlipNumber = mFlipNumber;
        mFlipNumber = number;
        jumpNumber();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        flipNumberList.clear();
        flipOutterNumberList.clear();

        String flipNumberString = String.valueOf(mFlipNumber);
        for (int i = 0; i < flipNumberString.length(); i++) {
            flipNumberList.add(String.valueOf(flipNumberString.charAt(i)));
        }

        String flipOutterNumberString = String.valueOf(mOutterFlipNumber);
        for (int i = 0; i < flipOutterNumberString.length(); i++) {
            flipOutterNumberList.add(String.valueOf(flipOutterNumberString.charAt(i)));
        }
        if (flipOutterNumberString.length() < flipNumberString.length()) {
            for (int i = 0; i < flipNumberString.length() - flipOutterNumberString.length(); i++) {
                flipOutterNumberList.add(0, "0");
            }
        }

        paint.getTextBounds(String.valueOf(mFlipNumber), 0, String.valueOf(mFlipNumber).length(), textRect);
        final int textWidth = textRect.width() + 80;

        float curTextWidth = 0;

        for (int i = 0; i < flipNumberList.size(); i++) {
            paint.getTextBounds(flipNumberList.get(i), 0, 1, textRect);
            final int numWidth = textRect.width();

            float x = getWidth() * 1.0f / 2 - textWidth * 1.0f / 2 + curTextWidth;
            float y = getHeight() * 1.0f / 2 + textRect.height() * 1.0f / 2;

            if (flipNumberList.get(i).equals(flipOutterNumberList.get(i))) {
                paint.setAlpha(255);
                canvas.drawText(flipNumberList.get(i), x, y, paint);

            } else {
                paint.setAlpha((int) (255 * (1 - mCurrentAlphaValue)));
                canvas.drawText(flipOutterNumberList.get(i), x, mOutterMoveHeight + y, paint);

                paint.setAlpha((int) (255 * mCurrentAlphaValue));
                canvas.drawText(flipNumberList.get(i), x, mCurrentMoveHeight + y, paint);
            }

            curTextWidth += (numWidth + 20);
        }
    }

    private void jumpNumber() {

        ValueAnimator animator = ValueAnimator.ofFloat(mMaxMoveHeight, 0);
        animator.setDuration(1000);
        ValueAnimator animator1 = ValueAnimator.ofFloat(0, 1);
        animator1.setDuration(1000);
        ValueAnimator animator2 = ValueAnimator.ofFloat(0, -mMaxMoveHeight);
        animator2.setDuration(1000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentMoveHeight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAlphaValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOutterMoveHeight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.start();
        animator1.start();
        animator2.start();
    }
}
